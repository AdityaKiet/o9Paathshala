package com.o9pathshala.test.slidingmenu.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.o9.R;
import com.o9pathshala.student.test.dto.AdapterDTO;

public class Adapter extends BaseAdapter {
	private Context context;
	private ArrayList<AdapterDTO> adapterDTOs;
	private ArrayList<Integer> headings;
	public Adapter(Context context, ArrayList<AdapterDTO> adapterDTOs) {
		this.context = context;
		this.adapterDTOs = adapterDTOs;
		getHeadings();
	}

	private void getHeadings() {
		headings = new ArrayList<Integer>();
		for(AdapterDTO adapterDTO : adapterDTOs)
			if(adapterDTO.getIsHeading())
				headings.add(adapterDTOs.indexOf(adapterDTO));
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

			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View rowView = null;
			if(adapterDTOs.get(position).getTitle().toString().startsWith("Sec"))
				rowView = mInflater.inflate(R.layout.drawer_list_heading_test, null);
			else
				rowView = mInflater.inflate(R.layout.drawer_list_item_test, null);

		TextView txtTitle = (TextView) rowView.findViewById(R.id.title);

		txtTitle.setText((CharSequence)adapterDTOs.get(position).getTitle());

		return rowView;
	}
}
