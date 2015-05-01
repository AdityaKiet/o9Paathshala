package com.o9pathshala.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.o9pathshala.student.test.dto.TestDTO;

public class DecodeTestList {
	private String result;

	public DecodeTestList(String result) {
		this.result = result;
	}
	public List<TestDTO> getTestList() throws JSONException, ParseException{
		List<TestDTO> testList = new ArrayList<TestDTO>();
		JSONObject jsonObject;
		TestDTO testDTO;
		JSONArray jsonArray = new JSONArray(result);
		for(int i = 0;i < jsonArray.length(); i++){
			Timestamp timestamp = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			jsonObject = jsonArray.getJSONObject(i);
			testDTO = new TestDTO();
			testDTO.setActivated(true);
			testDTO.setCreatedBy(jsonObject.getString("test_created_by_name"));
			testDTO.setDuration(jsonObject.getInt("test_duration") * 60);
			if(null != jsonObject.getString("test_end_date") && !("null").equals(jsonObject.getString("test_end_date"))){
				java.util.Date parsedDate = dateFormat.parse(jsonObject.getString("test_end_date"));
				timestamp = new java.sql.Timestamp(parsedDate.getTime());
			}
			testDTO.setEnddate(timestamp);
			testDTO.setId(jsonObject.getInt("test_id"));
			testDTO.setNegativeMark(Float.parseFloat(jsonObject.getString("test_negative_mark")));
			testDTO.setPositiveMark(Float.parseFloat(jsonObject.getString("test_positive_mark")));
			if(null != jsonObject.getString("test_start_date") && !("null").equals(jsonObject.getString("test_start_date"))){
				java.util.Date parsedDate = dateFormat.parse(jsonObject.getString("test_start_date"));
				timestamp = new java.sql.Timestamp(parsedDate.getTime());
			}
			testDTO.setStartdate(timestamp);
			Date date = new Date();
			Timestamp current = new Timestamp(date.getTime());
			if(null != testDTO.getStartdate() && current.getTime() > testDTO.getStartdate().getTime()){
				if(null == testDTO.getEnddate())
					testDTO.setActivated(true);
				else if(current.getTime() < testDTO.getEnddate().getTime())
					testDTO.setActivated(true);
				else 
					testDTO.setActivated(false);
			}
			else
				testDTO.setActivated(true);
			testDTO.setTestName(jsonObject.getString("test_name"));
			testList.add(testDTO);
		}
		return testList;
	}
}
