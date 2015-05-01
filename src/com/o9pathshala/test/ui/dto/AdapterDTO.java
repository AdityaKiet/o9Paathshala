package com.o9pathshala.test.ui.dto;

public class AdapterDTO {
	private String title;
	private Boolean isHeading;
	private Boolean isFlag;
	private Boolean attempted;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getIsHeading() {
		return isHeading;
	}

	public void setIsHeading(Boolean isHeading) {
		this.isHeading = isHeading;
	}

	public Boolean getIsFlag() {
		return isFlag;
	}

	public void setIsFlag(Boolean isFlag) {
		this.isFlag = isFlag;
	}

	public Boolean getAttempted() {
		return attempted;
	}

	public void setAttempted(Boolean attempted) {
		this.attempted = attempted;
	}

	@Override
	public String toString() {
		return "AdapterDTO [title=" + title + ", isHeading=" + isHeading
				+ ", isFlag=" + isFlag + ", attempted=" + attempted + "]";
	}

}
