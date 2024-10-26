package kr.co.ureca.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kr.co.ureca.entity.Answer;
import kr.co.ureca.entity.Question;
import kr.co.ureca.entity.SiteUser;
import kr.co.ureca.excption.DataNotFoundException;
import kr.co.ureca.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

	private final QuestionRepository questionRepository;

	public List<Question> getList() {
		return questionRepository.findAll();
	}

	public Page<Question> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add( Sort.Order.desc("createDate") );

//		Pageable pageable = PageRequest.of( page, 10, Sort.by(Sort.Order.desc("createDate")) );
		Pageable pageable = PageRequest.of( page, 10, Sort.by(sorts) );

		Specification<Question> spec = search(kw);

		return questionRepository.findAll(spec, pageable);
	} // getList

	public Question getQuestion( Integer id ) {
		Optional<Question> option = questionRepository.findById(id);
		if( option.isPresent() ) {
			return option.get();
		} else {
			throw new DataNotFoundException("question not found");
		}
	} // getQuestion

	public void create(String subject, String content, SiteUser siteUser) {
		Question question = new Question();

		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());
		question.setAuthor(siteUser);

		questionRepository.save(question);
	} // create

	public void modify( Question question, String subject, String content ) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());

		questionRepository.save(question);
	} // modify

	public void delete(Question question) {
		questionRepository.delete(question);
	} // delete

	public void vote(Question question, SiteUser siteUser) {
		question.getVoter().add(siteUser);
		questionRepository.save(question);
	} // vote

	public Specification<Question> search( String kw ) {

		return new Specification<Question>() {

			@Override
			public Predicate toPredicate(Root<Question> q
											, CriteriaQuery<?> query
											, CriteriaBuilder cb) {

				query.distinct(true);
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);

				return cb.or(
							cb.like(q.get("subject"), "%" + kw + "%")
							, cb.like(q.get("content"), "%" + kw + "%")
							, cb.like(u1.get("username"), "%" + kw + "%")
							, cb.like(a.get("content"), "%" + kw + "%")
							, cb.like(u2.get("username"), "%" + kw + "%")
						); // or
			}

		}; // new

	} // search

} // class














