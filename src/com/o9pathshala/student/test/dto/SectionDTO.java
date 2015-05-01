package com.o9pathshala.student.test.dto;

import java.util.List;

public class SectionDTO {

	private Integer sectionID;
    private String sectionName;
    private List<QuestionDTO> questions;
	public Integer getSectionID() {
		return sectionID;
	}
	public void setSectionID(Integer sectionID) {
		this.sectionID = sectionID;
	}
	
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public List<QuestionDTO> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}
	@Override
	public String toString() {
		return "SectionDTO [sectionID=" + sectionID + ", sectionName="
				+ sectionName + ", questions=" + questions + "]";
	}
    
}
