package kr.co.ureca.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuestionForm {

	@NotEmpty(message = "제목은 필수 항목 입니다.")
	@Size(max = 200)
	private String subject;

	@NotEmpty(message = "내용은 필수 항목 입니다.")
	private String content;

}
