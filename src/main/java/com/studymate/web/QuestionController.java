package com.studymate.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		if (!HttpSessionUtils.isLoginUser(session))
			return "redirect:/users/signIn";
		return "/qna/form";
	}

	@PostMapping("")
	public String create(String title, String contents, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session))
			return "redirect:/users/signIn";

		User sessionUser = HttpSessionUtils.getUserFromSession(session);

		Question question = new Question(sessionUser, title, contents);
		quesitonRepository.save(question);
		return "redirect:/";
	}

	@GetMapping("/{id}")
	public String quesitonDetail(@PathVariable Long id, Model model) {
		Question question = quesitonRepository.findById(id).orElse(null);
		if (question == null)
			return "redirect:/";
		model.addAttribute("question", question);
		return "/qna/show";
	}

	@GetMapping("/{id}/form")
	public String questionUpdateForm(@PathVariable Long id, Model model, HttpSession session,
			RedirectAttributes rdAttributes) {
		Question question = quesitonRepository.findById(id).orElse(null);
		try {
			hasPermission(session, question);
			model.addAttribute("question", question);
			return "/qna/updateForm";
		} catch (IllegalStateException e) {
			String errorMessage = e.getMessage();
			rdAttributes.addFlashAttribute("errorMessage", errorMessage);
			if (errorMessage.contains("글쓴이")) {
				return String.format("redirect:/questions/%d", id);
			}
			return "redirect:/users/signIn";
		}
	}

	private boolean hasPermission(HttpSession session, Question question) {
		if (!HttpSessionUtils.isLoginUser(session))
			throw new IllegalStateException("로그인이 필요합니다.");

		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if (!question.isSameWriter(loginUser))
			throw new IllegalStateException("해당 글쓴이가 아닙니다.");
		return true;
	}

	private boolean inputDataValdationCheck(String title, String contents) {
		if (title == "" || title == null || contents == "" || contents == null)
			throw new IllegalStateException("내용을 입력하세요.");
		return true;
	}

	@PutMapping("/{id}")
	public String updateQuestion(@PathVariable Long id, String title, String contents, HttpSession session,
			Model model, RedirectAttributes rdAttributes) {
		Question question = quesitonRepository.findById(id).orElse(null);
		try {
			hasPermission(session, question);
			inputDataValdationCheck(title, contents);
			question.update(title, contents);
			quesitonRepository.save(question);
			return String.format("redirect:/questions/%d", id);
		} catch (IllegalStateException e) {
			String errorMessage = e.getMessage();
			rdAttributes.addFlashAttribute("errorMessage", errorMessage);
			if (errorMessage.contains("글쓴이")) {
				return String.format("redirect:/questions/%d", id);
			} else if (errorMessage.contains("내용")) {
				return String.format("redirect:/questions/%d/form", id);
			}
			return "redirect:/users/signIn";
		}
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, HttpSession session, Model model,
			RedirectAttributes rdAttributes) {
		Question question = quesitonRepository.findById(id).orElse(null);
		try {
			hasPermission(session, question);
			quesitonRepository.delete(question);
			return "redirect:/";
		}catch(IllegalStateException e) {
			String errorMessage = e.getMessage();
			rdAttributes.addFlashAttribute("errorMessage", errorMessage);
			if (errorMessage.contains("글쓴이")) {
				return String.format("redirect:/questions/%d", id);
			}
			return "redirect:/users/signIn";
		}
	}

}
