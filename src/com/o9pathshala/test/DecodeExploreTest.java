package com.o9pathshala.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.o9pathshala.student.test.dto.SectionDTO;
import com.o9pathshala.student.test.dto.TestDTO;

public class DecodeExploreTest {
	private String result;
	private Context context;
	public DecodeExploreTest(Context context, String result) {
		this.result = result;
		this.context = context;
	}
	public void startTest() throws JSONException, ParseException{
		TestDTO testDTO = new TestDTO();
		List<SectionDTO> sections = new ArrayList<SectionDTO>();
		JSONArray jsonArray = new JSONArray(result);
		JSONObject jsonObject =  jsonArray.getJSONObject(0);
		Timestamp timestamp = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if(null != jsonObject.getString("test_end_date") && !("null").equals(jsonObject.getString("test_end_date"))){
			java.util.Date parsedDate = dateFormat.parse(jsonObject.getString("test_end_date"));
			timestamp = new java.sql.Timestamp(parsedDate.getTime());
		}
		testDTO.setEnddate(timestamp);	
		timestamp = null;
		if(null != jsonObject.getString("test_start_date") && !("null").equals(jsonObject.getString("test_start_date"))){
			java.util.Date parsedDate = dateFormat.parse(jsonObject.getString("test_start_date"));
			timestamp = new java.sql.Timestamp(parsedDate.getTime());
		}
		testDTO.setStartdate(timestamp);
		testDTO.setUploaddate(null);
		testDTO.setTestName(jsonObject.getString("test_name"));
		testDTO.setActivated(true);
		testDTO.setCreatedBy(jsonObject.getString("test_created_by_name"));
		testDTO.setDuration(jsonObject.getInt("test_duration") * 60);
		testDTO.setId(jsonObject.getInt("test_id"));
		testDTO.setSections(sections);
		testDTO.setNegativeMark(Float.parseFloat(jsonObject.getString("test_negative_mark")));
		testDTO.setPositiveMark(Float.parseFloat(jsonObject.getString("test_positive_mark")));
		Gson gson = new Gson();
		String json = gson.toJson(testDTO);
		Intent intent = new Intent((Activity)context, StartTestActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("json", json);
		intent.putExtras(bundle);
		((Activity)context).startActivity(intent);
	}
}
