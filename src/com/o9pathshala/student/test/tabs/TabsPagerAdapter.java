package com.o9pathshala.student.test.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new AllTestsFragment();
		case 1:
			return new AttemptedTestsFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
