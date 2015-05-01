package com.o9pathshala.student.slidingmenu.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.profile.dto.ProfileDTO;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.test.GetAllTestListAsynTask;
import com.o9pathshala.test.database.SqlConstants;

public class FragmentLeaderboard extends Fragment implements SqlConstants{
	private SharedPreferences sharedPreferences;
	private SessionDTO sessionDTO;
	private ProfileDTO profileDTO;
	private ListView listView;
	private RelativeLayout relativeLayout;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		relativeLayout = (RelativeLayout) inflater.inflate(R.layout.test_list,container, false);
		populate();
        return relativeLayout;
    }
	private void populate() {
		listView = (ListView)relativeLayout.findViewById(R.id.listAllTests);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		Gson gson = new Gson();
		String json = sharedPreferences.getString("profileDTO", null);
	    profileDTO = gson.fromJson(json, ProfileDTO.class);
	    json = sharedPreferences.getString("session", null);
	    sessionDTO = gson.fromJson(json, SessionDTO.class);
		String query = GET_STUDENT_TEST;
		query = query.replaceAll("INSTITUTE_ID", String.valueOf(sessionDTO.getCurrentInstitutesId()));
		query = query.replaceAll("ACTIVATION", "1");
		query = query.replaceAll("BATCH_ID", String.valueOf(profileDTO.getBatch_id()));
		GetAllTestListAsynTask getAllTestListAsynTask = new GetAllTestListAsynTask(getActivity(),query, relativeLayout);
		getAllTestListAsynTask.execute();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent("com.o9pathshala.test.leaderboard.LeaderBoardRankDisplayActivity");
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
		}
		
}
