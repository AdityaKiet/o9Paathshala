package com.o9pathshala.discussionfourm.dto;

import java.sql.Timestamp;

public class AnswerDTO {

	private String answer;
	private Integer answerId;
	private Integer userId;
	private String username;
	private Long reputation;
	private Boolean liked;
	private Timestamp time;
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Integer getAnswerId() {
		return answerId;
	}
	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getReputation() {
		return reputation;
	}
	public void setReputation(Long reputation) {
		this.reputation = reputation;
	}
	public Boolean getLiked() {
		return liked;
	}
	public void setLiked(Boolean liked) {
		this.liked = liked;
	}
	public Timestamp getDate() {
		return time;
	}
	public void setDate(Timestamp time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "AnswerDTO [answer=" + answer + ", answerId=" + answerId
				+ ", userId=" + userId + ", username=" + username
				+ ", reputation=" + reputation + ", liked=" + liked + ", time="
				+ time + "]";
	}
	
	
}
