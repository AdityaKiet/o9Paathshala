package com.o9pathshala.student.test;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.o9.R;
import com.o9pathshala.test.ui.dto.AdapterDTO;

public class ListAdpater extends BaseAdapter {
	private final Activity context;
	private List<AdapterDTO> adapterDTOs;
	public ListAdpater(Activity context, List<AdapterDTO> adapterDTOs) {
		this.context = context;
		this.adapterDTOs = adapterDTOs;
	}

	

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = null;

		if (adapterDTOs.get(position).getIsHeading()) {
			rowView = inflater.inflate(R.layout.test_question_list_section_name, null, true);
		} else {
			rowView = inflater.inflate(R.layout.test_question_list_item, null,true);
			if(adapterDTOs.get(position).getAttempted())
				rowView.setBackgroundResource(R.drawable.rectangle_attempted_background);
			else
				rowView.setBackgroundResource(R.drawable.rectengle_background);
			ImageView imgview = (ImageView) rowView.findViewById(R.id.imgFlag);
			if(adapterDTOs.get(position).getIsFlag())
				imgview.setVisibility(View.VISIBLE);
			else
				imgview.setVisibility(View.INVISIBLE);

		}

		TextView txtTitle = (TextView) rowView.findViewById(R.id.txtListQuestion);
		if (adapterDTOs.get(position).getIsHeading()) {
			txtTitle.setText(String.valueOf((adapterDTOs.get(position).getTitle().toUpperCase().charAt(0))));
		} else {
			txtTitle.setText(adapterDTOs.get(position).getTitle());
		}
		return rowView;
	}

	@Override
	public int getCount() {
		return adapterDTOs.size();
	}

	@Override
	public Object getItem(int position) {
		return adapterDTOs.get(position).getTitle();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
