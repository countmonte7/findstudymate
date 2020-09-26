package com.studymate.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Result {
	
	private boolean valid;
	
	private String errorMessage;
	
	
	public static Result ok() {
		return new Result(true, null);
	}
	
	public static Result fail(String errorMessage) {
		return new Result(false, errorMessage);
	}
	
	public boolean isValid() {
		return valid;
	}
}
