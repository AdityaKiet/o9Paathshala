package com.o9pathshala.discussionfourm.dto;

import java.sql.Timestamp;
import java.util.List;

public class QuestionDTO {

	private String title;
	private Integer id;
	private Long reputation;
	private String content;
	private Timestamp time;
	private List<TagDTO> tags;
	private Boolean liked;
	private String userName;
	private Integer userId;
	private Long answers;
	
	public Long getAnswers() {
		return answers;
	}
	public void setAnswers(Long answers) {
		this.answers = answers;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getReputation() {
		return reputation;
	}
	public void setReputation(Long reputation) {
		this.reputation = reputation;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public List<TagDTO> getTags() {
		return tags;
	}
	public void setTags(List<TagDTO> tags) {
		this.tags = tags;
	}
	public Boolean getLiked() {
		return liked;
	}
	public void setLiked(Boolean liked) {
		this.liked = liked;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return "QuestionDTO [title=" + title + ", id=" + id + ", reputation="
				+ reputation + ", content=" + content + ", time=" + time
				+ ", tags=" + tags + ", liked=" + liked + ", userName="
				+ userName + ", userId=" + userId + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (((QuestionDTO) obj).getId().equals(this.id)) {
			result = true;
		}
		return result;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
}
