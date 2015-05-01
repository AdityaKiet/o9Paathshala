package com.o9pathshala.discussionfourm.dto;

public class TagDTO {

	private String tagName;
	private Integer tagId;
	private String tagDesc;
	private Integer tagReputation;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getTagDesc() {
		return tagDesc;
	}

	public void setTagDesc(String tagDesc) {
		this.tagDesc = tagDesc;
	}

	public Integer getTagReputation() {
		return tagReputation;
	}

	public void setTagReputation(Integer tagReputation) {
		this.tagReputation = tagReputation;
	}

	@Override
	public String toString() {
		return "TagDTO [tagName=" + tagName + ", tagId=" + tagId + ", tagDesc="
				+ tagDesc + ", tagReputation=" + tagReputation + "]";
	}

}
