package com.o9pathshala.discussionfourm.explorequestion;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.database.SQLConstants;
import com.o9pathshala.discussionfourm.LikeAnswerAsynTask;
import com.o9pathshala.discussionfourm.LikeQuestionAsynTask;
import com.o9pathshala.discussionfourm.PostAnswerAsynTask;
import com.o9pathshala.discussionfourm.dto.AnswerDTO;
import com.o9pathshala.discussionfourm.dto.ExploredQuestionDTO;
import com.o9pathshala.discussionfourm.dto.TagDTO;
import com.o9pathshala.profile.dto.SessionDTO;

public class MainActivity extends Activity implements OnClickListener, SQLConstants {
	private Bundle bundle;
	private EditText etPostAnswer;
	private Button btnLike, btnPostAnswer;
	private SharedPreferences sharedPreferences;
	private SessionDTO newSessionDTO;
	private LinearLayout linearLayout;
	private ExploredQuestionDTO exploredQuestionDTO;
	private TextView txtTitle, txtContent, txtUser, txtTime, txtTags;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.explore_question_main);
		bundle = getIntent().getExtras();
		String json = bundle.getString("exploreQuestion");
		Gson gson = new Gson();
		exploredQuestionDTO = gson.fromJson(json, ExploredQuestionDTO.class);
		populate();
		setData();
	}
	
	
	private void setData() {
		txtContent.setText(exploredQuestionDTO.getQuestion().getContent());
		txtTitle.setText(exploredQuestionDTO.getQuestion().getTitle());
		txtUser.setText(exploredQuestionDTO.getQuestion().getUserName());
		String date = exploredQuestionDTO.getQuestion().getTime().toGMTString();
		StringBuilder builder = new StringBuilder(date);
		builder.setLength(11);
		txtTime.setText(builder.toString());
		if(exploredQuestionDTO.getQuestion().getLiked())
			btnLike.setText("Unlike  | " + exploredQuestionDTO.getQuestion().getReputation());
		else
			btnLike.setText("Like | " + exploredQuestionDTO.getQuestion().getReputation());
		StringBuilder stringBuilder = new StringBuilder("");
		if(null != exploredQuestionDTO.getQuestion().getTags() && exploredQuestionDTO.getQuestion().getTags().size() > 0){
		for(TagDTO tagDTO : exploredQuestionDTO.getQuestion().getTags())
			if(null != tagDTO.getTagName() && !("null").equals(tagDTO.getTagName()))
				stringBuilder.append("# " + tagDTO.getTagName() + "  ");
		if(null != stringBuilder.toString() && !("null").equals(stringBuilder.toString()))
			txtTags.setText("Tags : " + stringBuilder.toString());
		}
		setAnswers();
	}


	private void setAnswers() {
		LayoutInflater inflater;
		List<AnswerDTO> answers = exploredQuestionDTO.getAnswers();
		if(null != answers)
		for(final AnswerDTO answerDTO : answers){
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.discussion_fourm_answer_single_row, null);
			TextView textView = (TextView) view.findViewById(R.id.txtAnswerContentExploreQuestionTitle);
			textView.setText(answerDTO.getAnswer());
			textView = (TextView)view.findViewById(R.id.txtExploreQuestionAnswerUserName);
			textView.setText(answerDTO.getUsername());
			textView = (TextView)view.findViewById(R.id.txtExploreQuestionAnswerTime);
			String date = answerDTO.getDate().toGMTString();
			StringBuilder builder = new StringBuilder(date);
			builder.setLength(11);
			textView.setText(builder.toString());
			Button btnAnswerLike = (Button)view.findViewById(R.id.btnLikeAnswer);
			if(answerDTO.getLiked())
				btnAnswerLike.setText("Unlike | "  + answerDTO.getReputation());
			else
				btnAnswerLike.setText("Like | " + answerDTO.getReputation());
			btnAnswerLike.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					likeAnswer(answerDTO);
				}
			});
			View blankView = inflater.inflate(R.layout.blank_settings_background, null);
			blankView.setBackgroundColor(Color.parseColor("#ffffff"));
			blankView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
			
			linearLayout.addView(view);
			linearLayout.addView(blankView);
		}else{
			
		}
	}


	protected void likeAnswer(AnswerDTO answerDTO) {
		String query;
		if(answerDTO.getLiked()){
			query = UPDATE_ANSWER_UNLIKE;
			query = query.replaceAll("INSTITUTE_ID", String.valueOf(newSessionDTO.getCurrentInstitutesId()));
			query = query.replaceAll("ANSWER_ID", String.valueOf(answerDTO.getAnswerId()));
			query = query.replaceAll("USER_ID", String.valueOf(newSessionDTO.getId()));
		}else{
			query = UPDATE_ANSWER_LIKE;
			query = query.replaceAll("INSTITUTE_ID", String.valueOf(newSessionDTO.getCurrentInstitutesId()));
			query = query.replaceAll("ANSWER_ID", String.valueOf(answerDTO.getAnswerId()));
			query = query.replaceAll("USER_ID", String.valueOf(newSessionDTO.getId()));
		}
		LikeAnswerAsynTask likeAnswerAsynTask = new LikeAnswerAsynTask(MainActivity.this, exploredQuestionDTO, query);
		likeAnswerAsynTask.execute();
	}


	private void populate() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Gson gson = new Gson();
		String json = sharedPreferences.getString("session", null);
	    newSessionDTO = gson.fromJson(json, SessionDTO.class);
		etPostAnswer = (EditText)findViewById(R.id.etPostAnswer);
		btnPostAnswer = (Button)findViewById(R.id.btnPostAnswer);
		txtTags = (TextView)findViewById(R.id.txtExploreQuestionTags);
		linearLayout = (LinearLayout)findViewById(R.id.linearAnswers);
		btnLike = (Button)findViewById(R.id.btnLike);
		txtContent = (TextView)findViewById(R.id.txtExploreQuestionContent);
		txtTime = (TextView)findViewById(R.id.txtExploreQuestionTime);
		txtTitle = (TextView)findViewById(R.id.txtExploreQuestionTitle);
		txtUser = (TextView)findViewById(R.id.txtExploreQuestionUserName);
		btnLike.setOnClickListener(this);
		btnPostAnswer.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnLike:
			String query;
			if(exploredQuestionDTO.getQuestion().getLiked()){
				query = UPDATE_QUESTION_UNLIKE;
				query = query.replaceAll("INSTITUTE_ID", String.valueOf(newSessionDTO.getCurrentInstitutesId()));
				query = query.replaceAll("POST_ID", String.valueOf(exploredQuestionDTO.getQuestion().getId()));
				query = query.replaceAll("USER_ID", String.valueOf(newSessionDTO.getId()));
			}else{
				query = UPDATE_QUESTION_LIKE;
				query = query.replaceAll("INSTITUTE_ID", String.valueOf(newSessionDTO.getCurrentInstitutesId()));
				query = query.replaceAll("POST_ID", String.valueOf(exploredQuestionDTO.getQuestion().getId()));
				query = query.replaceAll("USER_ID", String.valueOf(newSessionDTO.getId()));
			}
			LikeQuestionAsynTask likeQuestionAsynTask = new LikeQuestionAsynTask(MainActivity.this, exploredQuestionDTO, query);
			likeQuestionAsynTask.execute();
			break;
		case R.id.btnPostAnswer:
			if(etPostAnswer.getText().toString().trim().equals(""))
				Toast.makeText(getApplicationContext(), "Write your answer.", Toast.LENGTH_LONG).show();
			else{
				String query1 = POST_ANSWER;
				query1 = query1.replaceAll("INSTITUTE_ID", String.valueOf(newSessionDTO.getCurrentInstitutesId()));
				query1 = query1.replaceAll("POST_ID", String.valueOf(exploredQuestionDTO.getQuestion().getId()));
				query1 = query1.replaceAll("USER_ID", String.valueOf(newSessionDTO.getId()));
				query1 = query1.replaceAll("ANSWER",etPostAnswer.getText().toString().trim());
				PostAnswerAsynTask postAnswerAsynTask = new PostAnswerAsynTask(MainActivity.this,exploredQuestionDTO, query1);
				postAnswerAsynTask.execute();
			}
			break;
		default:
			
			break;
		}
		
	}

}
