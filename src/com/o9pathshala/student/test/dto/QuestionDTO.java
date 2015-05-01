package com.o9pathshala.student.test.dto;

import java.util.List;

public class QuestionDTO {

	private Integer questionId;
	private String content;
	private Float marks;
	private Boolean attempted;
	private List<String> options;
	private List<Integer> correctOptions;
	private List<Integer> userAnswers;

	public Boolean getAttempted() {
		return attempted;
	}

	public void setAttempted(Boolean attempted) {
		this.attempted = attempted;
	}

	public Float getMarks() {
		return marks;
	}

	public void setMarks(Float marks) {
		this.marks = marks;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public List<Integer> getCorrectOptions() {
		return correctOptions;
	}

	public void setCorrectOptions(List<Integer> correctOptions) {
		this.correctOptions = correctOptions;
	}

	public List<Integer> getUserAnswers() {
		return userAnswers;
	}

	public void setUserAnswers(List<Integer> userAnswers) {
		this.userAnswers = userAnswers;
	}

	@Override
	public String toString() {
		return "QuestionDTO [questionId=" + questionId + ", content=" + content
				+ ", marks=" + marks + ", attempted=" + attempted
				+ ", options=" + options + ", correctOptions=" + correctOptions
				+ ", userAnswers=" + userAnswers + "]";
	}

}
