package com.o9pathshala.discussionfourm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.o9.R;
import com.o9pathshala.discussionfourm.dto.QuestionDTO;
import com.o9pathshala.discussionfourm.setdata.JSONDecode;
import com.o9pathshala.discussionfourm.tabs.AllQuestionsListAdapter;
import com.o9pathshala.global.GlobalData;

public class GetQuestionAsynTask extends AsyncTask<String, String, Void>{
		private String sql;
		private String ip;
		private InputStream is;
		private HttpEntity entity;
		
		private String result = "";
		private RelativeLayout relativeLayout;
		private Context context;
		
		
		public GetQuestionAsynTask(String sql, final Context context, RelativeLayout relativeLayout) {
			this.sql = sql;
			this.context = context;
			this.relativeLayout = relativeLayout;
			ResourceBundle rb = ResourceBundle.getBundle("network");
			ip = rb.getString("ip");
			TimerTask task = new TimerTask() {
				
				@Override
				public void run() {
					this.cancel();
					((Activity)context).runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(null != context && GetQuestionAsynTask.this.isCancelled())
								Toast.makeText(context.getApplicationContext().getApplicationContext(),"Connection failed..", Toast.LENGTH_LONG).show();
						}
					});
				}
			};
			
			Timer timer = new Timer();
			timer.schedule(task, 15000);
		}
		

		@Override
			protected Void doInBackground(String... params) {
			List<NameValuePair> list = new ArrayList<NameValuePair>(1);
			list.add(new BasicNameValuePair("query", sql));
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(ip+"/o9pathshala/get_question.php");
				httpPost.setEntity(new UrlEncodedFormEntity(list));
				HttpResponse httpResponse = httpClient.execute(httpPost);
				entity = httpResponse.getEntity();
				is = entity.getContent();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
				StringBuilder stringBuilder = new StringBuilder();
				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
				}
				is.close();
				result = stringBuilder.toString();
			} catch (Exception e) {
				GetQuestionAsynTask.this.cancel(true);
				Toast.makeText(context.getApplicationContext(),"Exception " + "Error Occured", Toast.LENGTH_LONG).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			ProgressBar progressBar;
			try{
				if(result.equals("false"))
					Toast.makeText(context, "No questions found", Toast.LENGTH_LONG).show();
				else{
					List<QuestionDTO> questions;
					JSONDecode jsonDecode = new JSONDecode(context,result);
					questions = jsonDecode.decode();
					GlobalData.questions = questions;
					Button button = (Button) relativeLayout.findViewById(R.id.btnLoadMore);
					button.setVisibility(View.VISIBLE);
					ListView listView = (ListView) relativeLayout.findViewById(R.id.listAllQuestions);
					AllQuestionsListAdapter adapter = new AllQuestionsListAdapter(context, questions);
					listView.setAdapter(adapter);
				}
				progressBar = (ProgressBar)relativeLayout.findViewById(R.id.progressBarDiscussionFourm);
				progressBar.setVisibility(View.GONE);
			}
			catch(Exception e){
				progressBar = (ProgressBar)relativeLayout.findViewById(R.id.progressBarDiscussionFourm);
				progressBar.setVisibility(View.GONE);
				GetQuestionAsynTask.this.cancel(true);
				Toast.makeText(context.getApplicationContext(),"Exception " + "Error Occured", Toast.LENGTH_LONG).show();
			
			}
			super.onPostExecute(v);
		}}
