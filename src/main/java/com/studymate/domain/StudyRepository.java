package com.studymate.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long>{
	
	public List<Study> findByName(String name);
}
