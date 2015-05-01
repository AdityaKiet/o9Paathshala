package com.o9pathshala.test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.o9pathshala.global.GlobalData;
import com.o9pathshala.student.test.dto.TestDTO;

public class DecodeAttemptedTest {
	private String jsonString;

	public DecodeAttemptedTest(String jsonString) {
		this.jsonString = jsonString;
	}

	public List<TestDTO> getTests(){
		try{
		List<TestDTO> listTests = new ArrayList<TestDTO>();
		JSONArray jsonArray = new JSONArray(jsonString);
		JSONObject jsonObject;
		List<Integer> attemptedListIds = new ArrayList<Integer>();
		
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = null;
			jsonObject = jsonArray.getJSONObject(i);
			attemptedListIds.add(jsonObject.getInt("test_id"));
		}
		for(int i = 0; i< GlobalData.allTests.size(); i++){
			if(attemptedListIds.contains(GlobalData.allTests.get(i).getId())){
				listTests.add(GlobalData.allTests.get(i));
			}
		}
		return listTests;
		}
		catch(Exception e){
			return null;
		}
	}
}
