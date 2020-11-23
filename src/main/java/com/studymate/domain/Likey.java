package com.studymate.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Likey {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private int id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_like_answer"))
	@JsonProperty
	private Answer answer;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_like_user"))
	@JsonProperty
	private User user;

	public boolean isSameAnswer(Answer likedAnswer) {
		if (likedAnswer == this.answer) {
			return true;
		}

		return false;
	}

	public Likey(Answer answer, User user) {
		super();
		this.answer = answer;
		this.user = user;
	}

}
