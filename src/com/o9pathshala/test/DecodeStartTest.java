package com.o9pathshala.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.o9pathshala.global.GlobalData;
import com.o9pathshala.student.test.dto.QuestionDTO;
import com.o9pathshala.student.test.dto.SectionDTO;
import com.o9pathshala.student.test.dto.TestDTO;

public class DecodeStartTest {
	private String query;
	private Context context;
	private TestDTO testDTO = new TestDTO();
	
	public DecodeStartTest(Context context, String query, TestDTO testDTO) {
		this.query = query;
		this.context = context;
		this.testDTO = testDTO;
	}

	public void startTest() throws JSONException {
		List<String> options;
		List<Integer> correctOptions;
		List<QuestionDTO> questions;
		SectionDTO sectionDTO;
		List<Integer> userAnswers;
		QuestionDTO questionDTO;
		JSONArray jsonArray = new JSONArray(query);
		JSONObject jsonObject;
		boolean flag = false;
		
		for(int i = 0;i<jsonArray.length(); i++){
			flag = false;
			correctOptions = new ArrayList<Integer>();
			userAnswers = new ArrayList<Integer>();
			questionDTO = new QuestionDTO();
			options = new ArrayList<String>();
			jsonObject = null;
			jsonObject = jsonArray.getJSONObject(i);
			if(null!= jsonObject.getString("option1") && !("null").equals(jsonObject.getString("option1")) && !jsonObject.getString("option1").trim().equals(""))
				options.add(jsonObject.getString("option1"));
			if(null!= jsonObject.getString("option2") && !("null").equals(jsonObject.getString("option2"))&& !jsonObject.getString("option2").trim().equals(""))
				options.add(jsonObject.getString("option2"));
			if(null!= jsonObject.getString("option3") && !("null").equals(jsonObject.getString("option3"))&& !jsonObject.getString("option3").trim().equals(""))
				options.add(jsonObject.getString("option3"));
			if(null!= jsonObject.getString("option4") && !("null").equals(jsonObject.getString("option4"))&& !jsonObject.getString("option4").trim().equals(""))
				options.add(jsonObject.getString("option4"));
			if(null!= jsonObject.getString("option5") && !("null").equals(jsonObject.getString("option5"))&& !jsonObject.getString("option5").trim().equals(""))
				options.add(jsonObject.getString("option5"));
			if(null!= jsonObject.getString("option6") && !("null").equals(jsonObject.getString("option6"))&& !jsonObject.getString("option6").trim().equals(""))
				options.add(jsonObject.getString("option6"));
			questionDTO.setOptions(options);
			
			if(null!= jsonObject.getString("question_content") && !("null").equals(jsonObject.getString("question_content")))
				questionDTO.setContent(jsonObject.getString("question_content"));
			
			if(null!= jsonObject.get("question_id") && !("null").equals(jsonObject.getString("question_id")))
				questionDTO.setQuestionId(jsonObject.getInt("question_id"));
			questionDTO.setAttempted(false);
			questionDTO.setUserAnswers(userAnswers);
			if(null != jsonObject.getString("correct_answer") && !("null").equals(jsonObject.getString("correct_answer"))){
				String []answers = jsonObject.getString("correct_answer").split(",");
				for(String temp : answers)
					correctOptions.add(Integer.parseInt(temp));
			}
			Collections.sort(correctOptions);
			questionDTO.setCorrectOptions(correctOptions);
			if(testDTO.getSections().size() == 0 && null != questionDTO.getQuestionId()){
				sectionDTO = new SectionDTO();
				sectionDTO.setSectionID(jsonObject.getInt("section_id"));
				sectionDTO.setSectionName(jsonObject.getString("section_name"));
				questions = new ArrayList<QuestionDTO>();
				questions.add(questionDTO);
				sectionDTO.setQuestions(questions);
				testDTO.getSections().add(sectionDTO);
			}
			
			else{
			for(int index = 0; index < testDTO.getSections().size(); index++){
				if(testDTO.getSections().get(index).getSectionName().equals(jsonObject.getString("section_name"))){
					if(null != questionDTO.getQuestionId()){
						flag = true;
						testDTO.getSections().get(index).getQuestions().add(questionDTO);
						break;
					}
				}
				}
			if(!flag && null != questionDTO.getQuestionId()){
				sectionDTO = new SectionDTO();
				sectionDTO.setSectionID(jsonObject.getInt("section_id"));
				sectionDTO.setSectionName(jsonObject.getString("section_name"));
				questions = new ArrayList<QuestionDTO>();
				questions.add(questionDTO);
				sectionDTO.setQuestions(questions);
				testDTO.getSections().add(sectionDTO);
			}
			}
			
		
		}
		GlobalData.testDTO = testDTO;
		int size = 0;
		for(SectionDTO sectionDTO2 : testDTO.getSections())
			size += sectionDTO2.getQuestions().size();
		if(size == 0){
			Toast.makeText(context, "No questions found in test.", Toast.LENGTH_LONG).show();
		}else{
		Intent intent = new Intent("com.o9pathshala.test.slidingmenu.fragments.MainActivity");
		((Activity)context).startActivity(intent);
		((Activity)context).finish();
		}
	}
}