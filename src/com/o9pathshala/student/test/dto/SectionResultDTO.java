package com.o9pathshala.student.test.dto;

public class SectionResultDTO {
	private Integer sectionID;
	private String sectionName;
	private Float totalMarks;
	private Float obtainedMarks;
	private Integer unattempted;
	private Integer correct;
	private Integer wrong;

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

	public Float getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(Float totalMarks) {
		this.totalMarks = totalMarks;
	}

	public Float getObtainedMarks() {
		return obtainedMarks;
	}

	public void setObtainedMarks(Float obtainedMarks) {
		this.obtainedMarks = obtainedMarks;
	}

	public Integer getUnattempted() {
		return unattempted;
	}

	public void setUnattempted(Integer unattempted) {
		this.unattempted = unattempted;
	}

	public Integer getCorrect() {
		return correct;
	}

	public void setCorrect(Integer correct) {
		this.correct = correct;
	}

	public Integer getWrong() {
		return wrong;
	}

	public void setWrong(Integer wrong) {
		this.wrong = wrong;
	}

	@Override
	public String toString() {
		return "SectionResultDTO [sectionID=" + sectionID + ", sectionName="
				+ sectionName + ", totalMarks=" + totalMarks
				+ ", obtainedMarks=" + obtainedMarks + ", unattempted="
				+ unattempted + ", correct=" + correct + ", wrong=" + wrong
				+ "]";
	}

}
