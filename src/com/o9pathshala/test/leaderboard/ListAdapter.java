package com.o9pathshala.test.leaderboard;

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
import com.o9pathshala.student.test.dto.LeaderBoardDTO;

public class ListAdapter extends BaseAdapter{
	private Context context;
	private List<LeaderBoardDTO> list;

	public ListAdapter(Context context, List<LeaderBoardDTO> list) {
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View rowView = null;
			rowView = mInflater.inflate(R.layout.rank_single_row_look, null);
			TextView txtTitle = (TextView) rowView.findViewById(R.id.SingleRowtitle);
			txtTitle.setText(list.get(position).getRank() + ". " + list.get(position).getName());
			TextView txtSocre = (TextView)rowView.findViewById(R.id.txtSingleRowscore);
			txtSocre.setText("Score : " + list.get(position).getScore());
		return rowView;
	}
}