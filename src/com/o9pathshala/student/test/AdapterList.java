package com.o9pathshala.student.test;

import java.util.ArrayList;
import java.util.List;

import com.o9pathshala.student.test.dto.QuestionDTO;
import com.o9pathshala.student.test.dto.SectionDTO;
import com.o9pathshala.test.ui.dto.AdapterDTO;

public class AdapterList {
	private List<SectionDTO> sections;
	private AdapterDTO adpterDTO;
	private ArrayList<AdapterDTO> adpterDTOs;

	public AdapterList(List<SectionDTO> sections) {
		this.sections = sections;
	}

	public ArrayList<AdapterDTO> makeList() {
		adpterDTOs = new ArrayList<AdapterDTO>();
		List<QuestionDTO> questions = new ArrayList<QuestionDTO>();
		for (SectionDTO sectionDTO : sections) {
			adpterDTO = new AdapterDTO();
			adpterDTO.setAttempted(null);
			adpterDTO.setIsFlag(null);
			adpterDTO.setIsHeading(true);
			adpterDTO.setTitle(sectionDTO.getSectionName());
			adpterDTOs.add(adpterDTO);
			questions = sectionDTO.getQuestions();
			for (int index = 0; index < questions.size(); index++) {
				adpterDTO = new AdapterDTO();
				adpterDTO.setAttempted(false);
				adpterDTO.setIsFlag(false);
				adpterDTO.setIsHeading(false);
				adpterDTO.setTitle(String.valueOf(index + 1));
				adpterDTOs.add(adpterDTO);
			}
		}
		return adpterDTOs;
	}
}
