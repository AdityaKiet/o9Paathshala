package com.o9pathshala.settings;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.o9.R;

public class ListAdapter extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] texts;
	private final Integer[] imageId;

	public ListAdapter(Activity context, String[] web, Integer[] imageId) {
		super(context, R.layout.settings_list_single_row, web);
		this.context = context;
		this.texts = web;
		this.imageId = imageId;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.settings_list_single_row, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txtsettings_single_row);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imgSettingListRow);
		txtTitle.setText(texts[position]);
		imageView.setImageResource(imageId[position]);
		return rowView;
	}
}
