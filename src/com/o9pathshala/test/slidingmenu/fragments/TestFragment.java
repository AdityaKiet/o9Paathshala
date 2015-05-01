package com.o9pathshala.test.slidingmenu.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.global.GlobalData;
import com.o9pathshala.student.test.AdapterList;
import com.o9pathshala.student.test.ClockUpdate;
import com.o9pathshala.student.test.ListAdpater;
import com.o9pathshala.student.test.TestList;
import com.o9pathshala.student.test.dto.QuestionDTO;
import com.o9pathshala.student.test.dto.SectionDTO;
import com.o9pathshala.student.test.dto.TestDTO;
import com.o9pathshala.test.result.tabs.MainActivity;
import com.o9pathshala.test.ui.dto.AdapterDTO;
import com.o9pathshala.test.util.Score;
import com.o9pathshala.ui.dto.TestListDTO;

public class TestFragment extends Fragment implements OnClickListener {
	private RelativeLayout relativeLayout;
	private TestDTO testDTO;
	private Float negativeMark;
	private Float positiveMark;
	private List<SectionDTO> sections;
	private TextView txtTestName, txtClock, txtQuestionNumber, txtQuestion;
	private ListView questionList;
	private CountDownTimer countDownTimer;
	private List<TestListDTO> testListDtos;
	private int currentQuestion = 1;
	private Button btnNext, btnPrevious, btnSubmit;
	private CheckBox[] checkBoxes;
	private View[] lines;
	
	private ToggleButton toggleButton;
	private static ArrayList<AdapterDTO> adapters;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		relativeLayout = (RelativeLayout) inflater.inflate(R.layout.test,container, false);
		populate();
		testDTO = GlobalData.testDTO;
		getData();
		setData();
		gettimer();
		TestList testList = new TestList(sections);
		testListDtos = testList.makeList();
		AdapterList adapterList = new AdapterList(sections);
		adapters = adapterList.makeList();
		setQuestion(currentQuestion);
		
		ListAdpater adpater = new ListAdpater(getActivity(),adapters);
		questionList.setAdapter(adpater);
		questionList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				setQuestion(position);
			}
		});
		return relativeLayout;
	}
	
	public static ArrayList<AdapterDTO> getAdapters(){
		return adapters;
	}
	private void setQuestion(int index) {
		if (testListDtos.get(index).getQuestionId() != null) {
			currentQuestion = index;
			questionList.setSelection(currentQuestion);
			for (SectionDTO sectionDTO : sections) {
				if (sectionDTO.getSectionID() == testListDtos.get(index).getSectionId()) {
					List<QuestionDTO> questions = new ArrayList<QuestionDTO>();
					questions = sectionDTO.getQuestions();
					for (QuestionDTO questionDTO : questions) {
						if (questionDTO.getQuestionId() == testListDtos.get(index).getQuestionId()) {
							txtQuestionNumber.setText(" Question - "+ (index - sections.indexOf(sectionDTO)));
							txtQuestion.setText(questionDTO.getContent());
							setOptions(questionDTO);
							for (int i = 0; i < questionDTO.getOptions().size(); i++)
								checkBoxes[i].setChecked(false);
							for (int i = 0; i < questionDTO.getOptions().size(); i++)
								if (questionDTO.getUserAnswers().contains(i + 1))
									checkBoxes[i].setChecked(true);
								if (adapters.get(currentQuestion).getIsFlag())
									toggleButton.setChecked(true);
								else
									toggleButton.setChecked(false);

							break;
						}
					}
					break;
				}
			}

		}
	}

	private void getData() {
		negativeMark = testDTO.getNegativeMark();
		positiveMark = testDTO.getPositiveMark();
		sections = testDTO.getSections();
	}

	private void setOptions(QuestionDTO questionDTO) {
		for (int i = 1; i < 6; i++) {
			checkBoxes[i].setVisibility(View.GONE);
			lines[i].setVisibility(View.GONE);
		}
		for (int i = 0; i < questionDTO.getOptions().size(); i++) {
			checkBoxes[i].setVisibility(View.VISIBLE);
			checkBoxes[i].setText(questionDTO.getOptions().get(i));
			lines[i].setVisibility(View.VISIBLE);
		}
	}

	private void setData() {
		txtTestName.setText(testDTO.getTestName());

	}

	private void gettimer() {
		countDownTimer = new CountDownTimer(testDTO.getDuration() * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				String time = ClockUpdate.clock(millisUntilFinished);
				txtClock.setText(time);
			}

			@Override
			public void onFinish() {
				txtClock.setText("Finished");
				Score score = new Score(sections, positiveMark, negativeMark);
				testDTO.setSections(sections);
				Gson gson = new Gson();
				String json = gson.toJson(testDTO);
				if(null != getActivity()){
				Intent intent = new Intent(getActivity(),MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putFloat("score", score.score());
				bundle.putString("testDTO", json);
				intent.putExtras(bundle);
				startActivity(intent);
				getActivity().finish();
				}
			}
		};
		countDownTimer.start();

	}

	private void populate() {
		checkBoxes = new CheckBox[6];
		lines = new View[6];
		txtTestName = (TextView) relativeLayout.findViewById(R.id.txtTestName);
		txtClock = (TextView) relativeLayout.findViewById(R.id.txtClock);
		txtQuestionNumber = (TextView) relativeLayout.findViewById(R.id.txtQuestionNumber);
		txtQuestion = (TextView) relativeLayout.findViewById(R.id.txtQuestion);
		questionList = (ListView) relativeLayout.findViewById(R.id.listQuestionsTest);
		btnNext = (Button) relativeLayout.findViewById(R.id.btnNext);
		btnPrevious = (Button) relativeLayout.findViewById(R.id.btnPrevious);
		btnSubmit = (Button) relativeLayout.findViewById(R.id.btnSubmit);
		toggleButton = (ToggleButton) relativeLayout.findViewById(R.id.toggleFlag);
		checkBoxes[0] = (CheckBox) relativeLayout.findViewById(R.id.chkOption1);
		checkBoxes[1] = (CheckBox) relativeLayout.findViewById(R.id.chkOption2);
		checkBoxes[2] = (CheckBox) relativeLayout.findViewById(R.id.chkOption3);
		checkBoxes[3] = (CheckBox) relativeLayout.findViewById(R.id.chkOption4);
		checkBoxes[4] = (CheckBox) relativeLayout.findViewById(R.id.chkOption5);
		checkBoxes[5] = (CheckBox) relativeLayout.findViewById(R.id.chkOption6);
		lines[0] = (View) relativeLayout.findViewById(R.id.lineOption1);
		toggleButton.setOnClickListener(this);
		lines[1] = (View) relativeLayout.findViewById(R.id.lineOption2);
		lines[2] = (View) relativeLayout.findViewById(R.id.lineOption3);
		lines[3] = (View) relativeLayout.findViewById(R.id.lineOption4);
		lines[4] = (View) relativeLayout.findViewById(R.id.lineOption5);
		lines[5] = (View) relativeLayout.findViewById(R.id.lineOption6);
		btnNext.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		for (int i = 0; i < 6; i++)
			checkBoxes[i].setOnClickListener(this);

	}

	private void changeFlag() {
		Boolean isFlag = adapters.get(currentQuestion).getIsFlag();
		adapters.get(currentQuestion).setIsFlag(!isFlag);
		ListAdpater adpater = new ListAdpater(getActivity(),adapters);
		questionList.setAdapter(adpater);
		questionList.setSelection(currentQuestion);
		
	}
	
	private void updateAnswer(int option) {
		for (SectionDTO sectionDTO : sections) {
			if (sectionDTO.getSectionID() == testListDtos.get(currentQuestion).getSectionId()) {
				List<QuestionDTO> questionLists = new ArrayList<QuestionDTO>();
				questionLists = sectionDTO.getQuestions();
				for (QuestionDTO questionDTO : questionLists) {
					if (questionDTO.getQuestionId() == testListDtos.get(currentQuestion).getQuestionId()) {
						if (questionDTO.getUserAnswers().contains(option)) {
							int location = questionDTO.getUserAnswers().indexOf(option);
							questionDTO.getUserAnswers().remove(location);
						} else
							questionDTO.getUserAnswers().add(option);
						if (questionDTO.getUserAnswers().size() > 0){
							adapters.get(currentQuestion).setAttempted(true);
							questionDTO.setAttempted(true);
						}
						else{
							adapters.get(currentQuestion).setAttempted(false);
							questionDTO.setAttempted(false);
						}
						ListAdpater adpater = new ListAdpater(getActivity(),adapters);
						questionList.setAdapter(adpater);
						questionList.setSelection(currentQuestion);
						View view = questionList.getChildAt(currentQuestion);
						if (view != null) {
							view.setBackgroundResource(R.drawable.rectengle_background);
							if (questionDTO.getAttempted())
								view.setBackgroundResource(R.drawable.rectangle_attempted_background);
							else
								view.setBackgroundResource(R.drawable.rectengle_background);
						}
						
						break;
					}
				}
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnNext:
			
			if (currentQuestion < testListDtos.size() - 1) {
				currentQuestion++;
				if (null == testListDtos.get(currentQuestion).getQuestionId())
					currentQuestion++;
				setQuestion(currentQuestion);

			}
			break;
		case R.id.btnPrevious:
			if (currentQuestion > 1) {
				currentQuestion--;
				if (null == testListDtos.get(currentQuestion).getQuestionId())
					currentQuestion--;
				setQuestion(currentQuestion);
			}
			break;
		case R.id.chkOption1:
			updateAnswer(1);
			break;
		case R.id.chkOption2:
			updateAnswer(2);
			break;
		case R.id.chkOption3:
			updateAnswer(3);
			break;
		case R.id.chkOption4:
			updateAnswer(4);
			break;
		case R.id.chkOption5:
			updateAnswer(5);
			break;
		case R.id.chkOption6:
			updateAnswer(6);
			break;
		case R.id.btnSubmit:
			Score score = new Score(sections, positiveMark, negativeMark);
			testDTO.setSections(sections);
			Gson gson = new Gson();
			String json = gson.toJson(testDTO);
			Intent intent = new Intent(getActivity(),MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putFloat("score", score.score());
			bundle.putString("testDTO", json);
			intent.putExtras(bundle);
			startActivity(intent);
			getActivity().finish();
			break;
		case R.id.toggleFlag:
			changeFlag();
			break;
		default:
			break;
		}

	}

	
	

	
}
