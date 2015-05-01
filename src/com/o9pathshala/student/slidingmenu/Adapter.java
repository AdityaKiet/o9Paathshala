package com.o9pathshala.student.slidingmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.o9.R;
import com.o9pathshala.ui.dto.AdapterDTO;

public class Adapter extends BaseAdapter {
	private Context context;
	private ArrayList<AdapterDTO> adapterDTOs;

	public Adapter(Context context, ArrayList<AdapterDTO> adapterDTOs) {
		this.context = context;
		this.adapterDTOs = adapterDTOs;
	}

	public int getCount() {
		return adapterDTOs.size();
	}

	public Object getItem(int position) {
		return adapterDTOs.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {

			View rowView;
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (adapterDTOs.get(position).getTitle().equalsIgnoreCase("application") || adapterDTOs.get(position).getTitle().equalsIgnoreCase("general"))
				rowView = mInflater.inflate(R.layout.drawer_list_heading,null);
			else
				rowView = mInflater.inflate(R.layout.drawer_list_item, null);
		

		ImageView imgIcon = (ImageView) rowView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.title);

		imgIcon.setImageResource(adapterDTOs.get(position).getIcon());
		txtTitle.setText(adapterDTOs.get(position).getTitle());

		return rowView;
	}
}
