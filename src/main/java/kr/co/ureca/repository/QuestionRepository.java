package kr.co.ureca.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.ureca.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

	public Page<Question> findAll(Pageable pageable);

	public Page<Question> findAll(Specification<Question> spec, Pageable pageable);

}
