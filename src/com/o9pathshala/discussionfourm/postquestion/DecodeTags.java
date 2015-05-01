package com.o9pathshala.discussionfourm.postquestion;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.o9pathshala.discussionfourm.dto.TagDTO;
import com.o9pathshala.global.GlobalData;

public class DecodeTags {
	public void decode(String jsonString) throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonString);
		JSONObject jsonObject = null;
		TagDTO tagDTO = null;
		List<TagDTO> list = new ArrayList<TagDTO>();
		for(int i = 0; i < jsonArray.length() ;i++){
			jsonObject = jsonArray.getJSONObject(i);
			tagDTO = null;
			tagDTO = new TagDTO();
			if(jsonObject.getString("tag_id") != null && !("null").equals(jsonObject.getString("tag_id")))
				tagDTO.setTagId(jsonObject.getInt("tag_id"));
			if(jsonObject.getString("tag_name") != null && !("null").equals(jsonObject.getString("tag_name")))
				tagDTO.setTagName(jsonObject.getString("tag_name"));
			if(jsonObject.getString("tag_desc") != null && !("null").equals(jsonObject.getString("tag_desc")))
				tagDTO.setTagDesc(jsonObject.getString("tag_desc"));
			if(jsonObject.getString("tag_reputation") != null && !("null").equals(jsonObject.getString("tag_reputation")))
				tagDTO.setTagReputation(jsonObject.getInt("tag_reputation"));
			else
				tagDTO.setTagReputation(0);
			list.add(tagDTO);
		}
		GlobalData.tags = list;
	}
}
