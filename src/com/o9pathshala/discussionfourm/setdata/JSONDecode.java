package com.o9pathshala.discussionfourm.setdata;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.o9pathshala.discussionfourm.dto.QuestionDTO;
import com.o9pathshala.discussionfourm.dto.TagDTO;

public class JSONDecode {
	private String result;
	
	public JSONDecode(Context context, String result) {
		this.result = result;
	}
	public List<QuestionDTO> decode() throws JSONException{
		JSONArray jsonArray = new JSONArray(result);
		List<QuestionDTO> datas = new ArrayList<QuestionDTO>();
		QuestionDTO data;
		TagDTO tagDTO;
		List<TagDTO> tags;
		JSONObject jsonObject = null; 
		
		for(int i = 0;i<jsonArray.length();i++){
			tags = new ArrayList<TagDTO>();
			tagDTO = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			tagDTO = new TagDTO();
			Log.d("log", jsonArray.getJSONObject(i).toString());
			jsonObject = jsonArray.getJSONObject(i);
			data = new QuestionDTO();
			data.setId(jsonObject.getInt("post_id"));
			data.setAnswers(jsonObject.getLong("answers"));
			data.setContent(jsonObject.getString("post_content"));
			data.setLiked(jsonObject.getInt("liked") > 0 ? true : false);
			data.setReputation(jsonObject.getLong("reputation"));
			Timestamp timestamp = null;
			try{
			    Date parsedDate = dateFormat.parse(jsonObject.getString("post_time"));
			    timestamp = new java.sql.Timestamp(parsedDate.getTime());
			}catch(Exception e){
			 Log.e("error", e.getMessage());
			}
			data.setTime(timestamp);
			data.setUserId(jsonObject.getInt("user_id"));
			data.setTitle(jsonObject.getString("post_title"));
			data.setUserName(jsonObject.getString("user_name"));
			if(null != jsonObject.getString("tag_name") && !("null").equals(jsonObject.getString("tag_name")))
				tagDTO.setTagName(jsonObject.getString("tag_name"));
			if(null != jsonObject.getString("tag_desc") && !("null").equals(jsonObject.getString("tag_desc")))
				tagDTO.setTagDesc(jsonObject.getString("tag_desc"));
			if(null != jsonObject.getString("tag_id") && !("null").equals(jsonObject.getString("tag_id")))
				tagDTO.setTagId(jsonObject.getInt("tag_id"));
			if(null != jsonObject.getString("tag_reputation") && !("null").equals(jsonObject.getString("tag_reputation")))
				tagDTO.setTagReputation(jsonObject.getInt("tag_reputation"));
			else
				tagDTO.setTagReputation(0);
			
			if(datas.contains(data)){
				datas.get(datas.indexOf(data)).getTags().add(tagDTO);
			}
				else{
					tags.add(tagDTO);
					data.setTags(tags);
					datas.add(data);
				}
			
		}
		/*Gson gson = new Gson();
		String json = gson.toJson(datas);
	    sharedEditor.putString("allquestions", json);
	    sharedEditor.commit();
		((GlobalItems)context.getApplicationContext()).setAllQuestions(datas);
		Log.d("log", ((GlobalItems)context.getApplicationContext()).getAllQuestions().toString());*/
		return datas;
	}
}
