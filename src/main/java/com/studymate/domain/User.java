package com.studymate.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false, length=20, unique=true)
	private String userId;
	private String password;
	private String name;
	private String email;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public boolean matchId(Long inputId) {
		if(inputId==null) return false;
		
		return inputId.equals(id);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean matchPassword(String inputPassword) {
		if(inputPassword==null || inputPassword=="") return false;
		
		return inputPassword.equals(password);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
	}

	public void update(User updateUser) {
		if(updateUser.getPassword()!=null  && updateUser.getPassword()!="")
			this.password = updateUser.getPassword();
		if(updateUser.getEmail()!=null && updateUser.getEmail()!="")
			this.email = updateUser.getEmail();
	}

	
}
