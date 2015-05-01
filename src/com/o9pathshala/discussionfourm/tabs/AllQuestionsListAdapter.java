package com.o9pathshala.discussionfourm.tabs;

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
import com.o9pathshala.discussionfourm.dto.QuestionDTO;
import com.o9pathshala.discussionfourm.dto.TagDTO;

public class AllQuestionsListAdapter extends BaseAdapter{
		private Context context;
		private List<QuestionDTO> questions;
		public AllQuestionsListAdapter(Context context, List<QuestionDTO> questions) {
			this.context = context;
			this.questions = questions;
		}

		public int getCount() {
			return questions.size();
		}

		public Object getItem(int position) {
			return questions.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		public View getView(int position, View convertView, ViewGroup parent) {

				LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				View rowView = null;
				rowView = mInflater.inflate(R.layout.discussion_fourm_single_line, null);
				TextView txtUserName = (TextView) rowView.findViewById(R.id.txtDiscussionForumUserName);
				txtUserName.setText(questions.get(position).getUserName());
				TextView txtTitle = (TextView)rowView.findViewById(R.id.txtDiscussionFourmTitle);
				txtTitle.setText(questions.get(position).getTitle());
				TextView txtTime = (TextView)rowView.findViewById(R.id.txtDiscussionForumTime);
				String date = questions.get(position).getTime().toGMTString();
				StringBuilder builder = new StringBuilder(date);
				builder.setLength(11);
				txtTime.setText(builder.toString());
				TextView txtTags = (TextView)rowView.findViewById(R.id.txtTagsDiscussionFourmSingleRow);
				StringBuilder stringBuilder = new StringBuilder("");
				if(null != questions.get(position).getTags() && questions.get(position).getTags().size() > 0){
				for(TagDTO tagDTO : questions.get(position).getTags())
					if(null != tagDTO.getTagName() && !("null").equals(tagDTO.getTagName()))
						stringBuilder.append("# " + tagDTO.getTagName() + "  ");
				if(null != stringBuilder.toString() && !("null").equals(stringBuilder.toString()))
					txtTags.setText("Tags : " + stringBuilder.toString());
				}
			return rowView;
		}
}

