package com.o9pathshala.test.result.tabs;

public class ResultDTO {
	private Integer student_id;
	private Integer test_id;
	private Integer batch_id;
	private String test_name;
	private Float score;
	private Integer institute_id;

	public Integer getInstitute_id() {
		return institute_id;
	}

	public void setInstitute_id(Integer institute_id) {
		this.institute_id = institute_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public Integer getTest_id() {
		return test_id;
	}

	public void setTest_id(Integer test_id) {
		this.test_id = test_id;
	}

	public Integer getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(Integer batch_id) {
		this.batch_id = batch_id;
	}

	public String getTest_name() {
		return test_name;
	}

	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "ResultDTO [student_id=" + student_id + ", test_id=" + test_id
				+ ", batch_id=" + batch_id + ", test_name=" + test_name
				+ ", score=" + score + "]";
	}

}
