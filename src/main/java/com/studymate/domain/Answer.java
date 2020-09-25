package com.studymate.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Answer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User writer;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;
	
	@Lob
	private String contents;
	
	private LocalDateTime createDate;
	
	
	public Answer(Question question, User writer, String contents) {
		this.question = question;
		this.writer = writer;
		this.contents = contents;
		this.createDate = LocalDateTime.now();
			
	}
	
	public String getFormattedCreateDate() {
		if(createDate == null) return "";
		
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
	}
}