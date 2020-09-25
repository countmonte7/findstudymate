package com.studymate.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;


@ToString
@NoArgsConstructor
@Getter
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20, unique = true)
	private String userId;
	@NonNull
	private String password;
	@NonNull
	private String name;
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
	
	public Long getId() {
		return id;
	}
	
	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public void update(User updateUser) {
		if (updateUser.getPassword() != null && updateUser.getPassword() != "")
			this.password = updateUser.getPassword();
		if (updateUser.getEmail() != null && updateUser.getEmail() != "")
			this.email = updateUser.getEmail();
	}

}
