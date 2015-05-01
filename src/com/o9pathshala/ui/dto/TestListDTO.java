package com.o9pathshala.ui.dto;

public class TestListDTO {
	private Integer index;
	private Integer sectionId;
	private Integer questionId;
	private Boolean flag;

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	@Override
	public String toString() {
		return "TestListDTO [index=" + index + ", sectionId=" + sectionId
				+ ", questionId=" + questionId + ", flag=" + flag + "]";
	}

}
