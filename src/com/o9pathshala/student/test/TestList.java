package com.o9pathshala.student.test;

import java.util.ArrayList;
import java.util.List;

import com.o9pathshala.student.test.dto.QuestionDTO;
import com.o9pathshala.student.test.dto.SectionDTO;
import com.o9pathshala.ui.dto.TestListDTO;

public class TestList {
	private ArrayList<String> textList;
	private List<SectionDTO> sections;
	private List<Integer> sectionPosition;
	private List<TestListDTO> testListDTOs;
	private TestListDTO testListDTO;

	public TestList(List<SectionDTO> sections) {
		this.sections = sections;
	}

	public List<TestListDTO> makeList() {
		int questionIndex = 0;
		testListDTOs = new ArrayList<TestListDTO>();
		sectionPosition = new ArrayList<Integer>();
		List<QuestionDTO> questions = new ArrayList<QuestionDTO>();
		textList = new ArrayList<String>();

		for (SectionDTO sectionDTO : sections) {
			sectionPosition.add(questionIndex);
			testListDTO = new TestListDTO();
			testListDTO.setIndex(questionIndex);
			testListDTO.setQuestionId(null);
			testListDTO.setSectionId(sectionDTO.getSectionID());
			
			testListDTOs.add(testListDTO);
			textList.add(sectionDTO.getSectionName());
			questionIndex++;
			questions = sectionDTO.getQuestions();
			for (int index = 0; index < questions.size(); index++) {
				testListDTO = new TestListDTO();
				testListDTO.setFlag(false);
				textList.add(String.valueOf(index + 1));
				testListDTO.setIndex(questionIndex);
				testListDTO.setQuestionId(questions.get(index).getQuestionId());
				testListDTO.setSectionId(sectionDTO.getSectionID());
				testListDTOs.add(testListDTO);
				questionIndex++;
			}
		}
		return testListDTOs;
	}
}
