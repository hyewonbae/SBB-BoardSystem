package kr.co.ureca.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.ureca.dto.AnswerForm;
import kr.co.ureca.dto.QuestionForm;
import kr.co.ureca.entity.Question;
import kr.co.ureca.entity.SiteUser;
import kr.co.ureca.service.QuestionService;
import kr.co.ureca.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;


@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

	private final QuestionService questionService;

	private final UserService userService;

/*
	@GetMapping("/list")
	public String list( Model model ) {
		List<Question> qList = questionService.getList();
		model.addAttribute("q_list", qList);
		return "question_list";
	}
*/

	@GetMapping("/list")
	public String list( Model model
						, @RequestParam(value = "page", defaultValue = "0") int page
						, @RequestParam(value = "kw", defaultValue = "") String kw) {

		Page<Question> paging = questionService.getList(page, kw);

		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);

		return "question_list";
	}

	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {

		Question question = questionService.getQuestion(id);
		model.addAttribute("question", question);

		return "question_detail";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "question_form";
	}
/*
	@PostMapping("/create")
	public String questionCreate( @RequestParam(value = "subject") String subject
									, @RequestParam(value = "content") String content) {
		questionService.create(subject, content);
		return "redirect:/question/list";
	}
*/

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm
									, BindingResult bindingResult
									, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "/question_form";
		}

		SiteUser siteUser = userService.getUser(principal.getName());

		questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

		return "redirect:/question/list";
	} // questionCreate

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm
									, @PathVariable("id") Integer id
									, Principal principal) {
		Question question = questionService.getQuestion(id);

		if( !question.getAuthor().getUsername().equals( principal.getName() ) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음.");
		}

		questionForm.setSubject( question.getSubject() );
		questionForm.setContent( question.getContent() );

		return "question_form";
	} // questionModify

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify( @Valid QuestionForm questionForm
									, BindingResult bindingResult
									, @PathVariable("id") Integer id
									, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "question_form";
		}

		Question question = questionService.getQuestion(id);

		if( !question.getAuthor().getUsername().equals( principal.getName() ) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음.");
		}

		questionService.modify(question
								, questionForm.getSubject()
								, questionForm.getContent());

		return String.format("redirect:/question/detail/%s", id);
	} // questionModify

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(@PathVariable("id") Integer id, Principal principal) {
		Question question = questionService.getQuestion(id);

		if( !question.getAuthor().getUsername().equals( principal.getName() ) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한 없음.");
		}

		questionService.delete(question);

		return "redirect:/";
	} // questionDelete

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote( @PathVariable("id") Integer id, Principal principal ) {
		Question question = questionService.getQuestion(id);
		SiteUser siteUser = userService.getUser( principal.getName() );

		questionService.vote(question, siteUser);

		return String.format("redirect:/question/detail/%s", id);
	} // questionVote

} // class
