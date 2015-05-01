package com.o9pathshala.test;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.student.test.dto.TestDTO;
import com.o9pathshala.test.database.SqlConstants;

public class StartTestActivity extends Activity implements OnClickListener, SqlConstants{
	private Button btnStartTest;
	private SharedPreferences sharedPreferences;
	private TestDTO testDTO;
	private Bundle bundle;
	private SessionDTO sessionDTO;
	private TextView txtTestName, txtTime, txtPositiveMarks, txtNegativeMarks;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_test_activity);
		populate();
		bundle = getIntent().getExtras();
		String json = bundle.getString("json");
		Gson gson = new Gson();
		testDTO = gson.fromJson(json, TestDTO.class);
		json = sharedPreferences.getString("session", null);
		sessionDTO = gson.fromJson(json, SessionDTO.class);
		setdata();
	}
	private void setdata() {
		txtNegativeMarks.setText("Negative Marks : " + testDTO.getNegativeMark());
		txtPositiveMarks.setText("Positive Marks : " + testDTO.getPositiveMark());
		txtTestName.setText(testDTO.getTestName());
		txtTime.setText("Time : " + (testDTO.getDuration() / 60) + " Mins.");
	}
	private void populate() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		btnStartTest = (Button)findViewById(R.id.btnStartTest);
		txtNegativeMarks = (TextView)findViewById(R.id.txtStartNegativeMarks);
		txtPositiveMarks = (TextView)findViewById(R.id.txtStartPositiveMarks);
		txtTestName = (TextView)findViewById(R.id.txtTestNameStartTest);
		txtTime = (TextView)findViewById(R.id.txtStartTestTime);
		btnStartTest.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnStartTest:
			testDTO = new TestDTO();
			String json = bundle.getString("json");
			Gson gson = new Gson();
			testDTO = gson.fromJson(json, TestDTO.class);
			String query = START_TEST;
			query = query.replaceAll("INSTITUTE_ID", String.valueOf(sessionDTO.getCurrentInstitutesId()));
			query = query.replaceAll("TEST_ID", String.valueOf(testDTO.getId()));
			GetTestDataAsynTask getTestDataAsynTask = new GetTestDataAsynTask(StartTestActivity.this, query, testDTO);
			getTestDataAsynTask.execute();
			break;
		}
	}
	
	

}
