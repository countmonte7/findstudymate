package com.studymate.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@NonNull
	@JsonProperty
	private String title;
	@NonNull
	@JsonProperty
	private String contents;

	@JsonProperty
	private Integer countOfAnswer = 0;

	@NonNull
	private LocalDateTime createDate;

	@OneToMany(mappedBy = "question")
	@OrderBy("reorder_no ASC")
	private List<Answer> answers;

	public Question(User writer, String title, String contents) {
		super();
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.createDate = LocalDateTime.now();
	}

	public String getFormattedCreateDate() {
		if (createDate == null)
			return "";

		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
	}

	public void update(String title, String contents) {
		this.title = title;
		this.contents = contents;
	}

	public boolean isSameWriter(User sessionUser) {
		return this.writer.equals(sessionUser);
	}

	public void addAnswerCount() {
		this.countOfAnswer += 1;
	}

	public void deleteAnswerCount() {
		this.countOfAnswer -= 1;
	}

	public void addReOrderNoForAll(Long parentAnswerReorderNo) {

		for (int i = 0; i < answers.size(); i++) {
			Long reorderNo = this.answers.get(i).getReorderNo();
			if (reorderNo > parentAnswerReorderNo) {
				Long newReOderNo = reorderNo + 1;
			}

		}
	}

	public Long addReOrderNo() {
		return Long.valueOf(this.answers.size());
	}

}
