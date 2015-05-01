package com.o9pathshala.student.slidingmenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.o9.R;

public class HomeFragmentAdapter extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] texts;
	private final Integer[] imageId;

	public HomeFragmentAdapter(Activity context, String[] web, Integer[] imageId) {
		super(context, R.layout.guest_single_row_look, web);
		this.context = context;
		this.texts = web;
		this.imageId = imageId;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.guest_single_row_look, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txtguest_single_row);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imgguest_single_row);
		txtTitle.setText(texts[position]);
		imageView.setImageResource(imageId[position]);
		return rowView;
	}
}
