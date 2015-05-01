package com.o9pathshala.discussionfourm;

import java.sql.Timestamp;
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
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.o9pathshala.discussionfourm.dto.AnswerDTO;
import com.o9pathshala.discussionfourm.dto.ExploredQuestionDTO;
import com.o9pathshala.discussionfourm.dto.QuestionDTO;
import com.o9pathshala.discussionfourm.dto.TagDTO;
import com.o9pathshala.discussionfourm.explorequestion.MainActivity;

public class DecodeExploreQuestion {
	
	private String result;
	private String result1;
	private Context context;
	private AnswerDTO answerDTO;
	private JSONObject jsonObject;
	private List<AnswerDTO> answers;
	public DecodeExploreQuestion(String result, String result1, Context context) {
		this.result = result;
		this.result1 = result1;
		this.context = context;
	}
	
	public void decode() throws JSONException{
		answers = new ArrayList<AnswerDTO>();
		
		
		JSONArray jsonArray = new JSONArray(result);
		Timestamp timestamp = null;
		TagDTO tagDTO;
		List<TagDTO> tags = new ArrayList<TagDTO>();
		QuestionDTO questionDTO = new QuestionDTO();
		
		
		for(int i = 0;i < jsonArray.length(); i++){
			tagDTO = new TagDTO();
			jsonObject = jsonArray.getJSONObject(i);
			questionDTO.setId(jsonObject.getInt("post_id"));
			questionDTO.setAnswers(jsonObject.getLong("answers"));
			questionDTO.setContent(jsonObject.getString("post_content"));
			questionDTO.setLiked((jsonObject.getInt("liked") == 0)? false: true);
			questionDTO.setReputation(jsonObject.getLong("reputation"));
			if(null != jsonObject.getString("tag_name") && !("null").equals(jsonObject.getString("tag_name")))
				tagDTO.setTagName(jsonObject.getString("tag_name"));
			if(null != jsonObject.getString("tag_desc") && !("null").equals(jsonObject.getString("tag_desc")))
				tagDTO.setTagDesc(jsonObject.getString("tag_desc"));
			if(null != jsonObject.getString("tag_id") && !("null").equals(jsonObject.getString("tag_id")))
				tagDTO.setTagId(jsonObject.getInt("tag_id"));
			if(null != jsonObject.getString("tag_reputation") && !("null").equals(jsonObject.getString("tag_reputation")))
				tagDTO.setTagReputation(jsonObject.getInt("tag_reputation"));
			if(null != tagDTO.getTagId())
				tags.add(tagDTO);
			try{
			    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			    java.util.Date parsedDate = dateFormat.parse(jsonObject.getString("post_time"));
			    timestamp = new java.sql.Timestamp(parsedDate.getTime());
			}catch(Exception e){
				Log.e("error", e.getMessage());
				Toast.makeText(context.getApplicationContext(),"Exception " + "Error Occured", Toast.LENGTH_LONG).show();
			}
			questionDTO.setTime(timestamp);
			questionDTO.setTitle(jsonObject.getString("post_title"));
			questionDTO.setUserId(jsonObject.getInt("user_id"));
			questionDTO.setUserName(jsonObject.getString("user_name"));
			
		}
		
		questionDTO.setTags(tags);
		ExploredQuestionDTO exploredQuestionDTO = new ExploredQuestionDTO();
		exploredQuestionDTO.setQuestion(questionDTO);
		
		if(result1.equals("false")){
			exploredQuestionDTO.setAnswers(null);
		}else{
			jsonArray = new JSONArray(result1);	
			for(int i = 0; i < jsonArray.length(); i++){
				answerDTO = new AnswerDTO();
				jsonObject = null;
				jsonObject = jsonArray.getJSONObject(i);
				answerDTO.setAnswer(jsonObject.getString("answer_content"));
				answerDTO.setAnswerId(jsonObject.getInt("answer_id"));
				timestamp = null;
				try{
				    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				    java.util.Date parsedDate = dateFormat.parse(jsonObject.getString("answer_time"));
				    timestamp = new java.sql.Timestamp(parsedDate.getTime());
				}catch(Exception e){
					Log.e("error", e.getMessage());
					Toast.makeText(context.getApplicationContext(),"Exception " + "Error Occured", Toast.LENGTH_LONG).show();
				}
				answerDTO.setDate(timestamp);
				answerDTO.setLiked((jsonObject.getInt("liked") == 0)? false: true);
				answerDTO.setReputation(jsonObject.getLong("reputation"));
				answerDTO.setUserId(jsonObject.getInt("user_id"));
				answerDTO.setUsername(jsonObject.getString("user_name"));
				answers.add(answerDTO);
			}
			exploredQuestionDTO.setAnswers(answers);
		}
		Intent intent = new Intent(context, MainActivity.class);
		Bundle bundle = new Bundle();
		Gson gson = new Gson();
		String json = gson.toJson(exploredQuestionDTO);
		bundle.putString("exploreQuestion", json);
		intent.putExtras(bundle);
		((Activity)context).startActivity(intent);
	}
}
