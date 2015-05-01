package com.o9pathshala.discussionfourm.postquestion;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.database.SQLConstants;
import com.o9pathshala.global.GlobalData;
import com.o9pathshala.profile.dto.SessionDTO;

public class PostQuestionActivity extends Activity implements SQLConstants, OnClickListener{
	private EditText etTitle, etQuestion;
	private Button btnSubmit, btnAddTags;
	private TextView txtTags;
	private SharedPreferences sharedPreferences;
	private SessionDTO sessionDTO;
	private List<Integer> selectedTags = new ArrayList<Integer>();
	private List<Integer> localSelectedTags;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_question);
		populate();
		getTags();
		
	}
	private void getTags() {
		String sql = GET_ALL_TAGS;
		sql = sql.replaceAll("INSTITUTE_ID", String.valueOf(sessionDTO.getCurrentInstitutesId()));
		GetAllTagsAsynTask getAllTagsAsynTask = new GetAllTagsAsynTask(PostQuestionActivity.this, sql);
		getAllTagsAsynTask.execute();
	}
	
	
	private void populate() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Gson gson = new Gson();
		String json = sharedPreferences.getString("profileDTO", null);
	    json = sharedPreferences.getString("session", null);
	    sessionDTO = gson.fromJson(json, SessionDTO.class);
		etTitle = (EditText)findViewById(R.id.etQuestionTitle);
		txtTags = (TextView)findViewById(R.id.txtSelectedTags);
		btnAddTags = (Button)findViewById(R.id.btnAddTags);
		etQuestion = (EditText)findViewById(R.id.etQuestionContent);
		btnSubmit = (Button)findViewById(R.id.btnSubmitQuestion);
		btnSubmit.setOnClickListener(this);
		btnAddTags.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		
		
		if(v.getId() == R.id.btnSubmitQuestion){
			
			if(etTitle.getText().toString().trim().equals("") || etQuestion.getText().toString().trim().equals(""))
				Toast.makeText(getApplicationContext(), "Fields can't be blank", Toast.LENGTH_LONG).show();
			else{
				String tags = null;
				if(selectedTags.size() != 0){
					tags = QUESTION_TAG_MAP;
					tags = tags.replaceAll("INSTITUTE_ID", String.valueOf(sessionDTO.getCurrentInstitutesId()));
					tags = tagQuery(tags, selectedTags);
					Log.d("log", tags);
				}
				String question = POST_QUESTION;
				question = question.replace("INSTITUTE_ID", String.valueOf(sessionDTO.getCurrentInstitutesId()));
				question = question.replace("TITLE", etTitle.getText().toString());
				question = question.replace("CONTENT", etQuestion.getText().toString());
				question = question.replace("USERID", sessionDTO.getId()+"");
				
				PostQuestionAsynTask postQuestionAsynTask = new PostQuestionAsynTask(question, tags, PostQuestionActivity.this);
				postQuestionAsynTask.execute();
				question = null;
				System.gc();
			}
			
		}
		
		else if (v.getId() == R.id.btnAddTags){
			localSelectedTags = new ArrayList<Integer>();
			for(Integer i : selectedTags)
				localSelectedTags.add(i);
			if(null != GlobalData.tags){
				String[] items = new String[GlobalData.tags.size()];
				boolean[] isChecked = new boolean[GlobalData.tags.size()];
				for(int i = 0 ; i < GlobalData.tags.size() ;i++){
					items[i] =  GlobalData.tags.get(i).getTagName();
					if(selectedTags.contains(i))
						isChecked[i] = true;
					else
						isChecked[i] = false;
				}
				
				AlertDialog.Builder alert = new AlertDialog.Builder(PostQuestionActivity.this);

				alert.setMultiChoiceItems(items, isChecked, new OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {

						if(localSelectedTags.contains(which)){
							int index = localSelectedTags.indexOf(which);
							localSelectedTags.remove(index);
						}
						else
							localSelectedTags.add(which);
					}
				});
				alert.setTitle("Select Tags");
				alert.setPositiveButton("Select", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						selectedTags = localSelectedTags;
						StringBuilder builder = new StringBuilder("");
						builder.append("TAGS :");
						for(int i : selectedTags)
							builder.append(" # " + GlobalData.tags.get(i).getTagName());
						txtTags.setText(builder.toString());
					}
				});
				alert.setNegativeButton("Cancel", null);
				alert.show();
			}
			
		}
		
	}
	public String tagQuery(String s, List<Integer> ai) {
		StringBuilder builder = new StringBuilder(s);
		for(int i = 0; i < selectedTags.size() ; i++){
			builder.append("('POST_ID',"+ GlobalData.tags.get(selectedTags.get(i)).getTagId() + "),");
		}
		builder.setLength(builder.length() - 1);
		builder.append(";");
		return builder.toString();
	}

}
