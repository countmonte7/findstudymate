package com.studymate.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studymate.domain.Answer;
import com.studymate.domain.AnswerRepository;
import com.studymate.domain.Question;
import com.studymate.domain.QuestionRepository;
import com.studymate.domain.User;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@PostMapping("")
	public String createAnswer(@PathVariable Long questionId, String contents, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session))
			return "redirect:/users/signIn";

		User writer = HttpSessionUtils.getUserFromSession(session);

		if (contents == null || contents == "")
			return String.format("/questions/%d", questionId);

		Question question = questionRepository.findById(questionId).orElse(null);

		Answer answer = new Answer(question, writer, contents);

		answerRepository.save(answer);

		return String.format("redirect:/questions/%d", questionId);
	}
}
