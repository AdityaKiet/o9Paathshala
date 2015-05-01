package com.o9pathshala.settings.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.o9.R;

public class UpdateAccountListAdapter extends BaseAdapter{
		private Context context;
		private String[] headings;
		private String[] values;
		public UpdateAccountListAdapter(Context context, String[] headings, String[] values) {
			this.context = context;
			this.headings = headings;
			this.values = values;
		}

		public int getCount() {
			return values.length;
		}

		public Object getItem(int position) {
			return values[position];
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		public View getView(int position, View convertView, ViewGroup parent) {

				LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				View rowView = null;
				rowView = mInflater.inflate(R.layout.update_account_single_list, null);
				TextView txtHeading = (TextView) rowView.findViewById(R.id.txtupdateAccountHeading);
				TextView txtValue = (TextView)rowView.findViewById(R.id.txtupdateAccountValue);
				txtHeading.setText(headings[position]);
				if(values[position] != null && !values[position].equals("null"))
					txtValue.setText(values[position]);
				else
					txtValue.setText("");

			return rowView;
		}
}

