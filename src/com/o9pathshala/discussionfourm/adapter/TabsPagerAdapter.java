package com.o9pathshala.discussionfourm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.o9pathshala.discussionfourm.tabs.AllQuestionsFragment;
import com.o9pathshala.discussionfourm.tabs.MyQuestionsFragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new AllQuestionsFragment();
		case 1:
			return new MyQuestionsFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
