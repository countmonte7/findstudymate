package com.studymate.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long>{
	
	@Query("UPDATE Answer AS a SET reorder_no=:reorderNo WHERE question.id =:questionId")
	public Question updateReOrderNo(@Param("reorderNo") Long reorderNo,
									@Param("questionId") Long id);
	
}
