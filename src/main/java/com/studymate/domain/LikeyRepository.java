package com.studymate.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeyRepository extends JpaRepository<Likey, Long>{
	
	List<Likey> findByUser_Id(Long id);
	
}
