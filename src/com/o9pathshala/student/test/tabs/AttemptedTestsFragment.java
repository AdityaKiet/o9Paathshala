package com.o9pathshala.student.test.tabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.global.GlobalData;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.test.ExploreTestAsynTask;
import com.o9pathshala.test.GetAttemptedTestListAsynTask;
import com.o9pathshala.test.database.SqlConstants;

public class AttemptedTestsFragment extends Fragment implements SqlConstants{
	
	private RelativeLayout relativeLayout;
	private SharedPreferences sharedPreferences;
	private SessionDTO sessionDTO;
	private ListView listView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		relativeLayout = (RelativeLayout) inflater.inflate(R.layout.attempted_test_list, container, false);
		populate();
		return relativeLayout;
	}

	private void populate() {
		listView = (ListView)relativeLayout.findViewById(R.id.listAttemptedTests);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		Gson gson = new Gson();
		String json = sharedPreferences.getString("session", null);
	    sessionDTO = gson.fromJson(json, SessionDTO.class);
		String query = GET_ATTEMPTED_TEST;
		query = query.replaceAll("INSTITUTE_ID", String.valueOf(sessionDTO.getCurrentInstitutesId()));
		query = query.replaceAll("STUDENT_ID", String.valueOf(sessionDTO.getId()));
		GetAttemptedTestListAsynTask getAttemptedTestListAsynTask = new GetAttemptedTestListAsynTask(getActivity(),query, relativeLayout);
		getAttemptedTestListAsynTask.execute();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(GlobalData.myTests.get(position).getActivated()){
				String query = GET_TEST;
				query = query.replaceAll("INSTITUTE_ID", String.valueOf(sessionDTO.getCurrentInstitutesId()));
				query = query.replaceAll("TEST_ID", String.valueOf(GlobalData.myTests.get(position).getId()));
				ExploreTestAsynTask exploreTestAsynTask = new ExploreTestAsynTask(getActivity(), query);
				exploreTestAsynTask.execute();
				}else
					Toast.makeText(getActivity(), "Test is not activated", Toast.LENGTH_LONG).show();
			
			}
		});
		
	}
}
