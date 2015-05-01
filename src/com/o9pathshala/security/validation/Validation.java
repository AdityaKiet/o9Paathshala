package com.o9pathshala.security.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

	private static Pattern pattern;
	private static Matcher matcher;
	private static final String USERNAME_PATTERN = "^[a-z0-9A-Z_.]{5,30}$";
	private static final String INSTITUTE_PATTERN = "^[\\p{L} .'-]+$";
	private static final String PASSWORD_PATTERN = "^[A-Za-z0-9!@#$%^&*()_]{6,16}$";
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String LIBRARYID_PATTERN = "^[0-9]{5}$";
	private static final String OTP_PATTERN = "^[a-z0-9A-Z]{10}$";

	public boolean validateUsername(String username) {
		pattern = Pattern.compile(USERNAME_PATTERN);
		matcher = pattern.matcher(username);
		return matcher.matches();
	}

	public boolean validateInstituteName(String instituteName) {
		pattern = Pattern.compile(INSTITUTE_PATTERN);
		matcher = pattern.matcher(instituteName);
		return matcher.matches();
	}

	public boolean validatePassword(String password) {
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public boolean validateEmail(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public boolean validateLibraryId(String libraryId) {

		pattern = Pattern.compile(LIBRARYID_PATTERN);
		matcher = pattern.matcher(libraryId);
		return matcher.matches();
	}

	public boolean validateOTP(String otp) {
		pattern = Pattern.compile(OTP_PATTERN);
		matcher = pattern.matcher(otp);
		return matcher.matches();
	}
}
