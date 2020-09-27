package com.studymate.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studymate.domain.Answer;
import com.studymate.domain.AnswerRepository;
import com.studymate.domain.Question;
import com.studymate.domain.QuestionRepository;
import com.studymate.domain.Result;
import com.studymate.domain.User;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionRepository questionRepository;
	
	
	@GetMapping("/{id}")
	public Answer showAnswer(@PathVariable Long id) {
		Answer answer = answerRepository.findById(id).orElse(null);
		
		return answer;
	}
	

	@PostMapping("")
	public Answer createAnswer(@PathVariable Long questionId, String contents, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session))
			return null;
		if (contents == null || contents == "")
			return null;
		User writer = HttpSessionUtils.getUserFromSession(session);

		Question question = questionRepository.findById(questionId).orElse(null);

		Answer answer = new Answer(question, writer, contents);
		question.addAnswer();
		return answerRepository.save(answer);	
	}
	
	@DeleteMapping("/{id}")
	public Result delete(@PathVariable Long questionId, @PathVariable Long id, 
		HttpSession session, Model model) {
		if(!HttpSessionUtils.isLoginUser(session)) return Result.fail("로그인 해야합니다.");
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Answer answer = answerRepository.findById(id).orElse(null);
		if(!answer.isSameWriter(loginUser)) return Result.fail("본인의 글만 삭제할 수 있어요.");
		answerRepository.delete(answer);
		Question question = questionRepository.findById(questionId).orElse(null);
		question.deleteAnswer();
		questionRepository.save(question);
		return Result.ok();
	}
	
	
	@GetMapping("/{id}/updateForm")
	public Answer update(@PathVariable Long id, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) return null;
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Answer answer = answerRepository.findById(id).orElse(null);
		if(!answer.isSameWriter(loginUser)) {
			return null;
		}
		return answer;
		
	}
	
}
