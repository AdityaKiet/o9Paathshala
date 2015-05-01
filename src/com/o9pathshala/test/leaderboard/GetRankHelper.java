package com.o9pathshala.test.leaderboard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.student.test.dto.LeaderBoardDTO;

public class GetRankHelper {
	private SessionDTO sessionDTO;
	private SharedPreferences sharedPreferences;
	private String jsonString;
	private LinearLayout layout;
	private TextView txtRank, txtScore;
	private ListView listView;
	private Context context;
	
	
	public GetRankHelper(Context context, String jsonString, LinearLayout layout) {
		this.jsonString = jsonString;
		this.context = context;
		this.layout = layout;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		Gson gson = new Gson();
		String json = sharedPreferences.getString("session", null);
	    sessionDTO = gson.fromJson(json, SessionDTO.class);
	}
	 
	public void getRank() throws JSONException{
		listView = (ListView)layout.findViewById(R.id.listRanksDisplay);
		txtRank = (TextView)layout.findViewById(R.id.txtRankRankDisplay);
		txtScore = (TextView)layout.findViewById(R.id.txtScoreRankDsiplay);
		LeaderBoardDTO leaderBoardDTO = null;
		List<LeaderBoardDTO> list= new ArrayList<LeaderBoardDTO>(); 
		List<LeaderBoardDTO> returnList;
		JSONArray jsonArray = new JSONArray(jsonString);
		JSONObject jsonObject = null;
		for(int i = 0; i< jsonArray.length(); i++){
			jsonObject = null;
			jsonObject = jsonArray.getJSONObject(i);
			leaderBoardDTO = null;
			leaderBoardDTO = new LeaderBoardDTO();
			leaderBoardDTO.setId(jsonObject.getInt("student_id"));
			leaderBoardDTO.setName(jsonObject.getString("student_name"));
			leaderBoardDTO.setScore(Float.parseFloat(jsonObject.getString("score")));
			if(list.contains(leaderBoardDTO))
				list.remove(leaderBoardDTO);
			list.add(leaderBoardDTO);
		}
		for(int i = 0; i<list.size(); i++)
			list.get(i).setRank(i + 1);
		if(list.size() > 200)
			returnList = list.subList(0, 200);
		else
			returnList = list;
		LeaderBoardDTO dto=new LeaderBoardDTO();
		dto.setId(sessionDTO.getId());
		if(!returnList.contains(dto)&& list.contains(dto)){
			int index = list.indexOf(dto);
			dto.setName(list.get(index).getName());
			dto.setRank(list.get(index).getRank());
			dto.setScore(list.get(index).getScore());
			returnList.add(list.get(list.indexOf(dto)));
		}
		
		if(list.contains(dto)){
			txtRank.setText("Rank  : " + list.get(list.indexOf(dto)).getRank());
			txtScore.setText("Score : " + list.get(list.indexOf(dto)).getScore());
		}else{
			txtRank.setText("Rank  : -- ");
		    txtScore.setText("Score : -- ");
		}
		ListAdapter adapter = new ListAdapter(context, list);
		listView.setAdapter(adapter);
	}
}
