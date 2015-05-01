package com.o9pathshala.test.result.tabs;

import java.util.Collections;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.o9.R;
import com.o9pathshala.student.test.dto.QuestionDTO;
import com.o9pathshala.student.test.dto.SectionDTO;
import com.o9pathshala.student.test.dto.TestDTO;

public class SectionResultFragment extends Fragment {
	LinearLayout linearLayout;
	TestDTO testDTO;
	ScrollView scrollView;
	TextView txtStatus, txtQuestion, txtCorrectAnswer;
	CheckBox[] chk = new CheckBox[6];
	public SectionResultFragment(TestDTO testDTO) {
		this.testDTO = testDTO;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		scrollView = (ScrollView) inflater.inflate(R.layout.result_review, container, false);
		linearLayout = new LinearLayout(getActivity());
		linearLayout.setBackgroundResource(R.drawable.settings_background);
		linearLayout.setPadding(15, 15, 15, 15);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		for(SectionDTO sectionDTO : testDTO.getSections()){
			for(QuestionDTO questionDTO : sectionDTO.getQuestions()){
				String correctAnswer = "";
				View view = inflater.inflate(R.layout.result_review_single_list, null);
				view.setPadding(15, 15, 15, 15);
				populate(view);
				txtQuestion.setText(questionDTO.getContent());
				for(int i = 0; i<questionDTO.getOptions().size(); i++){
					chk[i].setVisibility(View.VISIBLE);
					chk[i].setText(questionDTO.getOptions().get(i));
					if(questionDTO.getUserAnswers().contains(i+1))
						chk[i].setChecked(true);
					if(questionDTO.getCorrectOptions().contains(i+1))
						correctAnswer += "\n" + chk[i].getText().toString();
				}
				txtCorrectAnswer.setText("Correct Answer is " + correctAnswer);
				if(!questionDTO.getAttempted()){
					txtStatus.setText("Unattempted");
					txtStatus.setTextColor(Color.GRAY);
				}else{
					List<Integer> userAnswers = questionDTO.getUserAnswers();
					Collections.sort(userAnswers);
					if(questionDTO.getCorrectOptions().toString().equals(userAnswers.toString()))
					{
						txtStatus.setText("Correct");
						txtStatus.setTextColor(Color.GREEN);
					}
					else
					{
							txtStatus.setText("In-correct");
							txtStatus.setTextColor(Color.RED);
					}
				}
				
				View blankView = inflater.inflate(R.layout.blank_settings_background, null);
				blankView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
				linearLayout.addView(view);
				linearLayout.addView(blankView);
			}
		}
		scrollView.addView(linearLayout);
		return scrollView;
	}
	private void populate(View view) {
		txtQuestion = (TextView)view.findViewById(R.id.txtResultQuestion);
		txtStatus = (TextView)view.findViewById(R.id.txtAnswerStatus);
		txtCorrectAnswer = (TextView)view.findViewById(R.id.txtResultRightAnswer);
		
		chk[0] = (CheckBox)view.findViewById(R.id.chkResult1);
		chk[1] = (CheckBox)view.findViewById(R.id.chkResult2);
		chk[2] = (CheckBox)view.findViewById(R.id.chkResult3);
		chk[3] = (CheckBox)view.findViewById(R.id.chkResult4);
		chk[4] = (CheckBox)view.findViewById(R.id.chkResult5);
		chk[5] = (CheckBox)view.findViewById(R.id.chkResult6);
		for(int i = 0;i < 6; i++)
			chk[i].setVisibility(View.GONE);
		
	}
}
