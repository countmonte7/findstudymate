package com.studymate.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.studymate.domain.User;
import com.studymate.domain.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserRepository userRepository;
	

	@GetMapping("/signUp")
	public String signUp() {
		return "/user/form";
	}
	
	@GetMapping("/signIn")
	public String signIn() {
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String signInAction(String userId, String password, HttpSession session) {
		User user = userRepository.findByUserId(userId);
		if(user==null) {
			logger.error("Login Fail");
			return "redirect:/users/signIn";
		}
		if(!user.matchPassword(password)) {
			logger.error("Login Fail");
			return "redirect:/users/signIn";
		}
		
		logger.info("Login Success");
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		if(session.getAttribute("sessionUser")!=null) {
			session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		}
		return "redirect:/";	
	}
	
	@PostMapping("")
	public String create(User user) {
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@GetMapping("")
	public String showList(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "/user/list";
	}
	
	@GetMapping("/profile")
	public String profile(Model model, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "/users/signIn";
		}
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		User user = userRepository.findById(sessionUser.getId()).orElse(null);
		
		model.addAttribute("user", user);
		return "/user/updateForm";
	}
	
	@PostMapping("/profile/update")
	public String update(User updateUser, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "/users/signIn";
		}
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		User user = userRepository.findById(sessionUser.getId()).orElse(null);
		user.update(updateUser);
		userRepository.save(user);
		return "redirect:/users";
	}
}
