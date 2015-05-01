package com.o9pathshala.settings;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.o9pathshala.profile.dto.InstituteDTO;

public class DecodeInstituteName {
	private String jsonString;
	
	public DecodeInstituteName(String jsonString) {
		this.jsonString = jsonString;
	}
	public List<InstituteDTO> getInstitutes() throws JSONException{
		List<InstituteDTO> list = new ArrayList<InstituteDTO>();
		InstituteDTO instituteDTO = null;
		JSONArray jsonArray = new JSONArray(jsonString);
		JSONObject jsonObject = null;
		for(int i = 0 ;i <jsonArray.length(); i++){
			instituteDTO = new InstituteDTO();
			jsonObject = null;
			jsonObject = jsonArray.getJSONObject(i);
			instituteDTO.setId(jsonObject.getInt("institute_id"));
			instituteDTO.setName(jsonObject.getString("institute_name"));
			list.add(instituteDTO);
		}
		return list;
	}
}
