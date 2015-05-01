package com.o9pathshala.test.util;

import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.o9pathshala.student.test.dto.QuestionDTO;
import com.o9pathshala.student.test.dto.SectionDTO;

public class Score {
	private List<SectionDTO> sections;
	private Float positive, negative;
	public Score(List<SectionDTO> sections, Float positive, Float negative) {
		this.sections = sections;
		this.positive = positive;
		this.negative = negative;
	}
	public float score() {
		float score = 0;
		for(SectionDTO sectionDTO : sections){
			for(QuestionDTO questionDTO : sectionDTO.getQuestions()){
				Log.d("log", questionDTO.toString());
				if(questionDTO.getAttempted()){
					List<Integer> userAnswers = questionDTO.getUserAnswers();
					Collections.sort(userAnswers);
					if(userAnswers.toString().equals(questionDTO.getCorrectOptions().toString()))
					score += positive;
					else 
					score -= negative;
				}
			}
		}
		return score;
	}
}
