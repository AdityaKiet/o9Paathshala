package com.o9pathshala.test.result.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.o9pathshala.student.test.dto.TestDTO;
import com.o9pathshala.test.result.tabs.SectionResultFragment;
import com.o9pathshala.test.result.tabs.AnalysisFragment;
import com.o9pathshala.test.result.tabs.ScoreFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	private TestDTO testDTO;
	private float score;
	public TabsPagerAdapter(FragmentManager fm, TestDTO testDTO, float score) {
		super(fm);
		this.testDTO = testDTO;
		this.score = score;
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new ScoreFragment(testDTO,score);
		case 1:
			return new SectionResultFragment(testDTO);
		case 2:
			return new AnalysisFragment(testDTO);
		}

		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
