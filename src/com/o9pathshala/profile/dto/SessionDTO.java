package com.o9pathshala.profile.dto;

public class SessionDTO {

	private String name;
	private String email;
	private Integer id;
	private Boolean defaultInstituteId;
	private Integer currentInstitutesId;
	private String type;
	private String instituteName;

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getDefaultInstituteId() {
		return defaultInstituteId;
	}

	public void setDefaultInstituteId(Boolean defaultInstituteId) {
		this.defaultInstituteId = defaultInstituteId;
	}

	public Integer getCurrentInstitutesId() {
		return currentInstitutesId;
	}

	public void setCurrentInstitutesId(Integer currentInstitutesId) {
		this.currentInstitutesId = currentInstitutesId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SessionDTO [name=" + name + ", email=" + email + ", id=" + id
				+ ", defaultInstituteId=" + defaultInstituteId
				+ ", currentInstitutesId=" + currentInstitutesId + ", type="
				+ type + ", instituteName=" + instituteName + "]";
	}

}
