package com.o9pathshala.student.test.tabs;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.o9.R;
import com.o9pathshala.student.test.dto.TestDTO;

public class AllTestsAdapter extends BaseAdapter{
	private Context context;
	private List<TestDTO> tests;
	
	public AllTestsAdapter(Context context, List<TestDTO> tests) {
		this.context = context;
		this.tests = tests;
	}

	public int getCount() {
		return tests.size();
	}

	public Object getItem(int position) {
		return tests.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View rowView = null;
			rowView = mInflater.inflate(R.layout.all_test_single_row, null);
			TextView txtTestName = (TextView) rowView.findViewById(R.id.txtTestName);
			txtTestName.setText(tests.get(position).getTestName());
			TextView txtTestCreator = (TextView) rowView.findViewById(R.id.txtTestCreator);
			txtTestCreator.setText(tests.get(position).getCreatedBy());
			TextView txtLastDate = (TextView) rowView.findViewById(R.id.txtLastDate);
			if(null != tests.get(position).getEnddate()){
				String date = tests.get(position).getEnddate().toGMTString();
				StringBuilder builder = new StringBuilder(date);
				builder.setLength(11);
				txtLastDate.setText(builder.toString());
			}
			/*TextView txtStartDate = (TextView) rowView.findViewById(R.id.txtTestStart);
			txtStartDate.setText(tests.get(position).getStartdate().getDate() + " - " + tests.get(position).getStartdate().getMonth());
			TextView txtEndDate = (TextView) rowView.findViewById(R.id.txtTestEnd);
			txtEndDate.setText(tests.get(position).getEnddate().getDate() + " - " + tests.get(position).getEnddate().getMonth());*/
		return rowView;
	}
}

