package com.studymate.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.studymate.domain.Study;
import com.studymate.domain.StudyRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/studymateSearch")
@Slf4j
public class ApiSutdymateSearchController {

	@Autowired
	private StudyRepository studyRepository;
	
	
	@GetMapping
	public String searchStudyForm() {
		return "/studymate/searchForm";
	}
	
	@GetMapping("/find")
	public String showStudyList(Model model, @RequestParam(value="searchBox") String name) {
		model.addAttribute("studies", studyRepository.findByName(name));
		return "/studymate/list";
	}
}
