package com.studymate.web;

import java.util.ArrayList;
import java.util.List;

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
		question.addAnswerCount();
		return answerRepository.save(answer);	
	}
	
	@DeleteMapping("/{id}")
	public List<Object> delete(@PathVariable Long questionId, @PathVariable Long id, 
		HttpSession session, Model model) {
		
		List<Object> resultObj = new ArrayList<>();
		
		if(!HttpSessionUtils.isLoginUser(session)) {
			resultObj.add(Result.fail("로그인 해야합니다."));
			return resultObj;
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Answer answer = answerRepository.findById(id).orElse(null);
		if(!answer.isSameWriter(loginUser)) {
			resultObj.add(Result.fail("본인의 글만 삭제할 수 있어요."));
			return resultObj;
		}
		answerRepository.delete(answer);
		Question question = questionRepository.findById(questionId).orElse(null);
		question.deleteAnswerCount();
		questionRepository.save(question);
		
		resultObj.add(Result.ok());
		resultObj.add(question);
		return resultObj;
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
	
	@PostMapping("/{id}/update")
	public Answer updateAction(@PathVariable Long id, HttpSession session, String contents) {
		if(!HttpSessionUtils.isLoginUser(session)) return null;
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Answer answer = answerRepository.findById(id).orElse(null);
		if(!answer.isSameWriter(loginUser)) return null;
		answer.update(contents);
		return answerRepository.save(answer);
	}
	
}
