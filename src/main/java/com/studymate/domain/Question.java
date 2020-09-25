package com.studymate.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name="fk_question_writer"))
	private User writer;
	
	public Question() {
		super();
	}

	private String title;

	private String contents;
	
	private LocalDateTime createDate;
	
	public Question(User writer, String title, String contents) {
		super();
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.createDate = LocalDateTime.now();
	}
	
	public String getFormattedCreateDate() {
		if(createDate == null) return "";
		
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
	}

	public void update(String title, String contents) {
		if(title!="" && title != null) this.title = title;
		if(contents!="" && contents != null) this.contents = contents;
	}
}
