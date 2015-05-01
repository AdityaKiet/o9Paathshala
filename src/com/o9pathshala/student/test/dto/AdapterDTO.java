package com.o9pathshala.student.test.dto;

public class AdapterDTO {
	private Object title;
	private Boolean isHeading;

	public Boolean getIsHeading() {
		return isHeading;
	}

	public void setIsHeading(Boolean isHeading) {
		this.isHeading = isHeading;
	}

	public AdapterDTO(Object title, Boolean isHeading) {
		this.title = title;
		this.isHeading = isHeading;
	}

	public Object getTitle() {
		return title;
	}

	public void setTitle(Object title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "AdapterDTO [title=" + title + ", isHeading=" + isHeading + "]";
	}

}
