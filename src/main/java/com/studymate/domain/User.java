package com.studymate.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Long id;

	@Column(nullable = false, length = 20, unique = true)
	@JsonProperty
	private String userId;
	
	@OneToMany(mappedBy = "user")
	private List<Likey> likes;
	
	@NonNull
	private String password;
	
	@JsonProperty
	@NonNull
	private String name;
	
	@JsonProperty
	@NonNull
	private String email;

	public boolean matchId(Long inputId) {
		if (inputId == null)
			return false;

		return inputId.equals(id);
	}

	public boolean matchPassword(String inputPassword) {
		if (inputPassword == null || inputPassword == "")
			return false;

		return inputPassword.equals(password);
	}
	
	public void update(User updateUser) {
		if (updateUser.getPassword() != null && updateUser.getPassword() != "")
			this.password = updateUser.getPassword();
		if (updateUser.getEmail() != null && updateUser.getEmail() != "")
			this.email = updateUser.getEmail();
	}

}
