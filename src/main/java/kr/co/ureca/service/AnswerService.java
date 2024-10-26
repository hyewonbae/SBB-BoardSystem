package kr.co.ureca.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.co.ureca.entity.Answer;
import kr.co.ureca.entity.Question;
import kr.co.ureca.entity.SiteUser;
import kr.co.ureca.excption.DataNotFoundException;
import kr.co.ureca.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerRepository answerRepository;

	public void create(Question question, String content, SiteUser siteUser) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(siteUser);

		answerRepository.save(answer);
	} // create

	public Answer getAnswer( Integer id ) {
		Optional<Answer> opAnswer = answerRepository.findById(id);

		if( opAnswer.isPresent() ) {
			return opAnswer.get();
		} else {
			throw new DataNotFoundException("answer not found");
		}
	} // getAnswer

	public void delete(Answer answer) {
		answerRepository.delete(answer);
	}

	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		answerRepository.save(answer);
	} // delete

	public void vote(Answer answer, SiteUser siteUser) {
		answer.getVoter().add(siteUser);
		answer.setModifyDate(LocalDateTime.now());
		answerRepository.save(answer);
	} // vote

} // class
