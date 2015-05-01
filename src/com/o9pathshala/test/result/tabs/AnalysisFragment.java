package com.o9pathshala.test.result.tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.o9.R;
import com.o9pathshala.student.test.dto.QuestionDTO;
import com.o9pathshala.student.test.dto.SectionDTO;
import com.o9pathshala.student.test.dto.SectionResultDTO;
import com.o9pathshala.student.test.dto.TestDTO;

public class AnalysisFragment extends Fragment {
	private ScrollView scrollView;
	private LinearLayout linearLayout;
	private TestDTO testDTO;
	private SectionResultDTO sectionResultDTO;
	private List<SectionResultDTO> sectionsResult;
	public AnalysisFragment(TestDTO testDTO) {
		this.testDTO = testDTO;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		sectionsResult= new ArrayList<SectionResultDTO>();
		calculate();
		
		linearLayout = new LinearLayout(getActivity());
		linearLayout.setPadding(25, 25, 25, 25);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setBackgroundResource(R.drawable.settings_background);
		scrollView = (ScrollView) inflater.inflate(R.layout.result_analysis, container, false);
		for(SectionResultDTO sectionDTO : sectionsResult){
			View view = inflater.inflate(R.layout.result_single_sections, null);
			TextView txtSectionName= (TextView)view.findViewById(R.id.txtSectionName);
			TextView txtSectionScore = (TextView)view.findViewById(R.id.txtSectionScore);
			TextView txtSectionCorrect = (TextView)view.findViewById(R.id.txtSectionNumberofQuestionCorrect);
			TextView txtSectionIncorrect = (TextView)view.findViewById(R.id.txtSectionNumberofQuestionInCorrect);
			TextView txtSectionUnattempted = (TextView)view.findViewById(R.id.txtSectionNumberofQuestionUnAttempted);
			txtSectionCorrect.setText(String.valueOf(sectionDTO.getCorrect()));
			txtSectionIncorrect.setText(String.valueOf(sectionDTO.getWrong()));
			txtSectionUnattempted.setText(String.valueOf(sectionDTO.getUnattempted()));
			txtSectionName.setText(sectionDTO.getSectionName());
			txtSectionScore.setText(String.valueOf(sectionDTO.getObtainedMarks()) + " / " + sectionDTO.getTotalMarks());
			View blankView = inflater.inflate(R.layout.blank_settings_background, null);
			blankView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
			linearLayout.addView(view);
			linearLayout.addView(blankView);
		}
		scrollView.addView(linearLayout);
		return scrollView;
	}

	private void calculate() {
		for(SectionDTO sectionDTO : testDTO.getSections()){
			int unattempted = 0, correct = 0, wrong = 0;
			float marksobtained = 0;
			sectionResultDTO = new SectionResultDTO();
			sectionResultDTO.setSectionID(sectionDTO.getSectionID());
			sectionResultDTO.setSectionName(sectionDTO.getSectionName());
			sectionResultDTO.setTotalMarks(sectionDTO.getQuestions().size() * testDTO.getPositiveMark());
			for(QuestionDTO questionDTO : sectionDTO.getQuestions()){
				if(questionDTO.getAttempted()){
					List<Integer> userAnswers = questionDTO.getUserAnswers();
					Collections.sort(userAnswers);
					if(questionDTO.getCorrectOptions().toString().equals(userAnswers.toString())){
						correct++;
						marksobtained += testDTO.getPositiveMark();
					}
					else{
						wrong++;
						marksobtained -= testDTO.getNegativeMark();
					}
				}else
					unattempted++;
			}
			sectionResultDTO.setObtainedMarks(marksobtained);
			sectionResultDTO.setCorrect(correct);
			sectionResultDTO.setWrong(wrong);
			sectionResultDTO.setUnattempted(unattempted);
			sectionsResult.add(sectionResultDTO);
		}
		
	}

}
