package com.studymate.web;

import java.util.Optional;

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
	
	@GetMapping("/{id}/form")
	public String updateform(@PathVariable Long id, Model model) {
		User user = userRepository.findById(id).orElse(null);
		model.addAttribute("user", user);
		return "/user/updateForm";
	}
	
	@PostMapping("/update")
	public String update(@RequestParam(value="id") Long id, User updateUser) {
		User user = userRepository.findById(id).orElse(null);
		user.update(updateUser);
		userRepository.save(user);
		return "redirect:/users";
	}
}
