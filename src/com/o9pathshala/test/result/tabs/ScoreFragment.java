package com.o9pathshala.test.result.tabs;

import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.profile.dto.ProfileDTO;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.student.test.dto.QuestionDTO;
import com.o9pathshala.student.test.dto.SectionDTO;
import com.o9pathshala.student.test.dto.TestDTO;
import com.o9pathshala.test.database.SetResultAsynTask;
import com.o9pathshala.test.database.SqlConstants;
import com.o9pathshala.util.NetworkCheck;

public class ScoreFragment extends Fragment implements OnClickListener, SqlConstants{
	private ScrollView scrollView;
	private SessionDTO sessionDTO;
	private ProfileDTO profileDTO;
	private TextView txtScore, txtCorrect, txtWrong, txtUnAttempted;
	private float totalMarks = 0 , score;
	private int correct = 0, wrong = 0, unattempted = 0;
	private List<SectionDTO> sections;
	private Button btnShare;
	private TestDTO testDTO;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor sharedEditor;
	
	public ScoreFragment(TestDTO testDTO, float score) {
		this.testDTO = testDTO;
		this.score = score;
		sections = testDTO.getSections();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		scrollView = (ScrollView) inflater.inflate(R.layout.result_score,container, false);
		populate();
		calculate();
		setData();
		ResultDTO resultDTO = new ResultDTO();
		resultDTO.setBatch_id(profileDTO.getBatch_id());
		resultDTO.setScore(score);
		resultDTO.setStudent_id(sessionDTO.getId());
		resultDTO.setTest_id(testDTO.getId());
		resultDTO.setTest_name(testDTO.getTestName());
		resultDTO.setInstitute_id(sessionDTO.getCurrentInstitutesId());
		Gson gson = new Gson();
		sharedEditor.putString("resultDTO", gson.toJson(resultDTO));
		sharedEditor.commit();
		
		if(NetworkCheck.isNetworkAvailable(getActivity())){
		String query = SET_RESULT;
		query = query.replaceAll("INSTITUTE_ID", String.valueOf(sessionDTO.getCurrentInstitutesId()));
		query = query.replaceAll("TEST_ID", String.valueOf(testDTO.getId()));
		query = query.replaceAll("TEST_NAME", testDTO.getTestName());
		query = query.replaceAll("SCORE", String.valueOf(score));
		query = query.replaceAll("BATCH_ID", String.valueOf(profileDTO.getBatch_id()));
		query = query.replaceAll("STUDENT_ID", String.valueOf(sessionDTO.getId()));
		SetResultAsynTask setResultAsynTask = new SetResultAsynTask(query, getActivity());
		setResultAsynTask.execute();
		}
		return scrollView;
	}

	private void setData() {
		txtScore.setText(score + " / "+ totalMarks);
		txtCorrect.setText(String.valueOf(correct));
		txtWrong.setText(String.valueOf(wrong));
		txtUnAttempted.setText(String.valueOf(unattempted));
	}

	private void calculate() {
		totalMarks = 0;
		correct = 0;
		wrong = 0;
		unattempted = 0;
		for(SectionDTO sectionDTO : sections){
			for(QuestionDTO questionDTO : sectionDTO.getQuestions()){
				totalMarks += testDTO.getPositiveMark();
				if(questionDTO.getAttempted()){
					List<Integer> userAnswers = questionDTO.getUserAnswers();
					Collections.sort(userAnswers);
					if(questionDTO.getCorrectOptions().toString().equals(userAnswers.toString()))
						correct++;
					else
						wrong++;
				}else{
					unattempted++;
				}
			}
		}
	}

	private void populate() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		sharedEditor = sharedPreferences.edit();
		Gson gson = new Gson();
		sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
		profileDTO = gson.fromJson(sharedPreferences.getString("profileDTO", null), ProfileDTO.class);
		txtScore = (TextView)scrollView.findViewById(R.id.txtScore);
		txtCorrect = (TextView)scrollView.findViewById(R.id.txtNumberofQuestionCorrect);
		txtWrong = (TextView)scrollView.findViewById(R.id.txtNumberofQuestionInCorrect);
		txtUnAttempted = (TextView)scrollView.findViewById(R.id.txtNumberofQuestionUnAttempted);
		btnShare = (Button) scrollView.findViewById(R.id.btnShareScore);
		btnShare.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnShareScore:
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Join me on o9Paathshala App !");
			intent.putExtra(android.content.Intent.EXTRA_TEXT, "My Score in test "+ testDTO.getTestName() + " is "+ score +" / " + totalMarks);
			getActivity().startActivity(Intent.createChooser(intent, "Select option"));
			break;
		default:
			break;
		}
	}	
	
}
