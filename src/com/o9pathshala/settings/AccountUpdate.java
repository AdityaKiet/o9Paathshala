package com.o9pathshala.settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.o9pathshala.profile.dto.ProfileDTO;
import com.o9pathshala.profile.dto.SessionDTO;

public class AccountUpdate {
	private String jsonString;
	private Context context;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor sharedEditor;
	private ProfileDTO profileDTO; 
	private SessionDTO sessionDTO;
	
	public AccountUpdate(String jsonString,Context context) {
		this.jsonString = jsonString;
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)this.context).getBaseContext());
		sharedEditor = sharedPreferences.edit();
		
	}

	public void update() throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonString);
		JSONObject jsonObject = null;
		Gson gson = new Gson();
		sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
		profileDTO = new ProfileDTO();
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = null;
			jsonObject = jsonArray.getJSONObject(i);
		}
		profileDTO.setAddress(jsonObject.getString("student_address"));
		if(jsonObject.getInt("student_batch_id") != 0)
			profileDTO.setBatch_id(jsonObject.getInt("student_batch_id"));
		profileDTO.setContact(jsonObject.getString("student_contact"));
		profileDTO.setDob(jsonObject.getString("student_dob"));
		profileDTO.setEmail(jsonObject.getString("student_email"));
		if(null != jsonObject.getString("student_gender") && !"null".equals(jsonObject.getString("student_gender")))
			profileDTO.setGender(jsonObject.getString("student_gender").charAt(0));
		profileDTO.setId(jsonObject.getInt("student_id"));
		sessionDTO.setId(jsonObject.getInt("student_id"));
		profileDTO.setName(jsonObject.getString("student_name"));
		sessionDTO.setName(jsonObject.getString("student_name"));
		if(null != jsonObject.getString("batch_name") && !"null".equals(jsonObject.getString("batch_name")))
			profileDTO.setBatchName(jsonObject.getString("batch_name"));
		String json = gson.toJson(profileDTO);
	    sharedEditor.putString("profileDTO", json);
	    json = gson.toJson(sessionDTO);
	    sharedEditor.putString("session", json);
	    sharedEditor.commit();
	}
}
