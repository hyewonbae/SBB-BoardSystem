package kr.co.ureca.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import kr.co.ureca.dto.AnswerForm;
import kr.co.ureca.entity.Answer;
import kr.co.ureca.entity.Question;
import kr.co.ureca.entity.SiteUser;
import kr.co.ureca.repository.AnswerRepository;
import kr.co.ureca.service.AnswerService;
import kr.co.ureca.service.QuestionService;
import kr.co.ureca.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

	private final QuestionService questionService;

	private final AnswerService answerService;

	private final UserService userService;

/*
	@PostMapping("/create/{id}")
	public String createAnswer(Model model
								, @PathVariable("id") Integer id
								, @RequestParam(value = "content") String content) {
		Question question = questionService.getQuestion(id);

		answerService.create(question, content);

		return String.format("redirect:/question/detail/%s", id);
	}
*/

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(Model model
								, @PathVariable("id") Integer id
								, @Valid AnswerForm answerFrom, BindingResult bindingResult
								, Principal principal) {

		Question question = questionService.getQuestion(id);

		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "/question_detail";
		}

		SiteUser siteUser = userService.getUser(principal.getName());

		answerService.create(question, answerFrom.getContent(), siteUser);

		return String.format("redirect:/question/detail/%s", id);
	} // createAnswer

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete( @PathVariable("id") Integer id, Principal principal) {
		Answer answer = answerService.getAnswer(id);
		Question question = answer.getQuestion();

		if( !answer.getAuthor().getUsername().equals( principal.getName() ) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한 없음.");
		}

		answerService.delete(answer);

		return String.format("redirect:/question/detail/%s", question.getId());
	} // answerDelete

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify( AnswerForm answerForm
								, @PathVariable("id") Integer id
								, Principal principal) {
		Answer answer = answerService.getAnswer(id);

		if( !answer.getAuthor().getUsername().equals( principal.getName() ) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음.");
		}

		answerForm.setContent(answer.getContent());

		return "answer_form";
	} // answerModify

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify( @Valid AnswerForm answerForm
								, BindingResult bindingResult
								, @PathVariable("id") Integer id
								, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "answer_form";
		}

		Answer answer = answerService.getAnswer(id);

		if( !answer.getAuthor().getUsername().equals( principal.getName() ) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음.");
		}

		answerService.modify(answer, answerForm.getContent());

		return String.format( "redirect:/question/detail/%s", answer.getQuestion().getId() );
	} // answerModify

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String answerVote( @PathVariable("id") Integer id, Principal principal ) {
		Answer answer = answerService.getAnswer(id);
		SiteUser siteUser = userService.getUser(principal.getName());

		answerService.vote(answer, siteUser);

		return String.format( "redirect:/question/detail/%s", answer.getQuestion().getId() );
	} // answerModify

} // class
