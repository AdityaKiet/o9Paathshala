package com.o9pathshala.discussionfourm.tabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.database.SQLConstants;
import com.o9pathshala.discussionfourm.GetMyQuestionAsynTask;
import com.o9pathshala.discussionfourm.LoadMoreMyQuestionsAsynTask;
import com.o9pathshala.discussionfourm.explorequestion.ExploreQuestionAsynTask;
import com.o9pathshala.global.GlobalData;
import com.o9pathshala.profile.dto.SessionDTO;

public class MyQuestionsFragment extends Fragment implements OnClickListener, SQLConstants{
	
	private SharedPreferences sharedPreferences;
	private SessionDTO sessionDTO;
	private RelativeLayout relativeLayout;
	private ListView listView;
	private Button btnLoadMoreQuestions;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		relativeLayout = (RelativeLayout) inflater.inflate(R.layout.discussion_fourm, container, false);
		populate();
		return relativeLayout;
	
	}

	private void populate() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		Gson gson = new Gson();
		String json = sharedPreferences.getString("profileDTO", null);
	    json = sharedPreferences.getString("session", null);
	    sessionDTO = gson.fromJson(json, SessionDTO.class);
	    listView = (ListView)relativeLayout.findViewById(R.id.listAllQuestions);
		btnLoadMoreQuestions = (Button)relativeLayout.findViewById(R.id.btnLoadMore);
		btnLoadMoreQuestions.setOnClickListener(this);
		String query = GET_MY_QUESTIONS;
		query = query.replaceAll("INSTITUTE_ID", ""+sessionDTO.getCurrentInstitutesId());
		query = query.replaceAll("USER_ID", sessionDTO.getId() + "");
		query = query.replaceAll("LOWER_LIMIT", "0");
		query = query.replaceAll("UPPER_LIMIT", "10");
		GetMyQuestionAsynTask getQuestionAsynTask = new GetMyQuestionAsynTask(query, getActivity(), relativeLayout);
		getQuestionAsynTask.execute();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String query = GET_EXPLORED_QUESTION;
				query = query.replace("INSTITUTE_ID", "" + sessionDTO.getCurrentInstitutesId());
				query = query.replace("POST_ID", String.valueOf(GlobalData.myQuestions.get(position).getId()));
				query = query.replace("USER_ID", String.valueOf(sessionDTO.getId()));
				
				String query1 = GET_POST_ANSWER;
				query1 = query1.replace("INSTITUTE_ID", "" + sessionDTO.getCurrentInstitutesId());
				query1 = query1.replace("POST_ID", String.valueOf(GlobalData.myQuestions.get(position).getId()));
				query1 = query1.replace("USER_ID", String.valueOf(sessionDTO.getId()));
				ExploreQuestionAsynTask asyn = new ExploreQuestionAsynTask(query, query1, getActivity());
				asyn.execute();				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnLoadMore:
			String query = GET_MY_QUESTIONS;
			query = query.replaceAll("INSTITUTE_ID", ""+sessionDTO.getCurrentInstitutesId());
			query = query.replaceAll("USER_ID", sessionDTO.getId() + "");
			query = query.replaceAll("LOWER_LIMIT", GlobalData.myQuestions.size() + "");
			query = query.replaceAll("UPPER_LIMIT", "10");
			LoadMoreMyQuestionsAsynTask loadMoreMyQuestionsAsynTask = new LoadMoreMyQuestionsAsynTask(query, getActivity(), relativeLayout);
			loadMoreMyQuestionsAsynTask.execute();
			break;
		}
		
	}
}
