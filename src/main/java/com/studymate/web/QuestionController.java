package com.studymate.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studymate.domain.Question;
import com.studymate.domain.QuestionRepository;
import com.studymate.domain.User;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	
	@Autowired
	private QuestionRepository quesitonRepository;
	
	@GetMapping("/form")
	public String form(HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) return "/users/signIn";
		return "/qna/form";
	}
	
	@PostMapping("")
	public String create(String title, String contents, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) return "/users/signIn";
		
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
	
		Question question = new Question(sessionUser, title, contents);
		quesitonRepository.save(question);
		return "redirect:/";
	}
	
	@GetMapping("/detail/{id}")
	public String quesitonDetail(@PathVariable Long id, Model model) {
		Question question = quesitonRepository.findById(id).orElse(null);
		if(question==null) return "redirect:/";
		model.addAttribute("question", question);
		return "/qna/show";
	}
	
}
