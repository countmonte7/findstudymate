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

import com.fasterxml.jackson.annotation.JsonProperty;

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
	@JsonProperty
	private Long id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
	@JsonProperty
	private User writer;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	@JsonProperty
	private Question question;
	
	@Lob
	@JsonProperty
	private String contents;
	
	@JsonProperty
	private LocalDateTime createDate;
	
	@JsonProperty
	private Long reparentNo;
	
	@JsonProperty
	private Long redepthNo = 0L;
	
	@JsonProperty
	private Long reorderNo = 0L;
	
	
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

	public boolean isSameWriter(User loginUser) {
		return loginUser.equals(this.writer);
	}

	public void update(String contents) {
		this.createDate = LocalDateTime.now();
		this.contents = contents;
	}
	
	public void addAnswerDepth(Long parentAnswerDepth) {
		this.redepthNo = parentAnswerDepth + 1;
	}
	
	public void addAnswerOrder(Long parentAnswerOrder) {
		this.reorderNo = parentAnswerOrder + 1;
	}
	
	public void addAnswerParentNo(Long parentAnswerNo) {
		this.reparentNo = parentAnswerNo;
	}
	
}
