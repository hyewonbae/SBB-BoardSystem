package kr.co.ureca;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.ureca.entity.Question;
import kr.co.ureca.repository.QuestionRepository;
import kr.co.ureca.service.QuestionService;

@SpringBootTest
class BootGradleApplicationTests {

	//@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuestionService questionService;

	@Test
	void jpaTest() {

		for(int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터 : [%03d]", i);
			questionService.create(subject, "There is no contents.", null);
		}

	}

	//@Test
	void contextLoads() {
		Question q1 = new Question();
		q1.setSubject("first");
		q1.setContent("first content");
		q1.setCreateDate( LocalDateTime.now() );
		questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("second");
		q2.setContent("second content");
		q2.setCreateDate( LocalDateTime.now() );
		questionRepository.save(q2);
	}

}







