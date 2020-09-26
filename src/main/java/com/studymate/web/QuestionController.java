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
import com.studymate.domain.Result;
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
		Result result = valid(session, question);
		if(!result.isValid()) {
			String errorMessage = result.getErrorMessage();
			rdAttributes.addFlashAttribute("errorMessage", errorMessage);
			if (errorMessage.contains("본인")) {
				return String.format("redirect:/questions/%d", id);
			}
			return "redirect:/users/signIn";
		}
		model.addAttribute("question", question);
		return "/qna/updateForm";
	}
	
	//로그인, 계정 일치 여부 validation
	private Result valid(HttpSession session, Question question) {
		if(!HttpSessionUtils.isLoginUser(session)) return Result.fail("로그인이 필요합니다");
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!question.isSameWriter(loginUser)) return Result.fail("본인의 글만 수정/삭제 가능합니다.");
		return Result.ok();
	}

	//내용 입력 여부 validation
	private Result inputDataValdationCheck(String title, String contents) {
		if (title == "" || title == null || contents == "" || contents == null) return Result.fail("내용을 입력하세요.");
		return Result.ok();
	}

	@PutMapping("/{id}")
	public String updateQuestion(@PathVariable Long id, String title, String contents, HttpSession session,
			RedirectAttributes rdAttributes) {
		Question question = quesitonRepository.findById(id).orElse(null);
		Result result = valid(session, question);
		if(!result.isValid()) {
			String errorMessage = result.getErrorMessage();
			rdAttributes.addFlashAttribute("errorMessage", errorMessage);
			if(errorMessage.contains("본인")) {
				return String.format("redirect:/questions/%d", id);
			}
			
			return "redirect:/users/signIn";
		}
		Result inputDataValidResult = inputDataValdationCheck(title, contents);
		if(!inputDataValidResult.isValid()) {
			String errorMessage = inputDataValidResult.getErrorMessage();
			rdAttributes.addFlashAttribute("errorMessage",errorMessage);
			return String.format("redirect:/questions/%d", id);
		}
		question.update(title, contents);
		quesitonRepository.save(question);
		return String.format("redirect:/questions/%d", id);
		
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, HttpSession session, Model model,
			RedirectAttributes rdAttributes) {
		Question question = quesitonRepository.findById(id).orElse(null);
		Result result = valid(session, question);
		if(!result.isValid()) {
			String errorMessage = result.getErrorMessage();
			rdAttributes.addFlashAttribute("errorMessage", errorMessage);
			if(errorMessage.contains("본인")) return String.format("redirect:/questions/%d", id);
			return "redirect:/users/signIn";
		}
		quesitonRepository.delete(question);
		return "redirect:/";
	}

}
