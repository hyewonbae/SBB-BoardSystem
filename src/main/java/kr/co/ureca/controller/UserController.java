package kr.co.ureca.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import kr.co.ureca.dto.UserCreateForm;
import kr.co.ureca.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	} // signup

	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm
							, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "signup_form";
		}

		if(	!userCreateForm.getPassword1().equals( userCreateForm.getPassword2() ) ) {
			bindingResult.rejectValue("password2"
										, "passwordInCorrect"
										, "2개의 비밀번호가 일치하지 않음.");
			return "signup_form";
		}

		try {
			userService.create(userCreateForm.getUsername()
								, userCreateForm.getPassword1()
								, userCreateForm.getEmail());
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 존재하는 ID 또는 EMAIL 입니다.");
			return "signup_form";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
		}

		return "redirect:/";
	} // signup

	@GetMapping("/login")
	public String login() {
		return "login_form";
	} // login

} // class
