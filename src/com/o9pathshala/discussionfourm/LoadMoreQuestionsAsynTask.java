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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.o9.R;
import com.o9pathshala.discussionfourm.dto.QuestionDTO;
import com.o9pathshala.discussionfourm.setdata.JSONDecode;
import com.o9pathshala.discussionfourm.tabs.AllQuestionsListAdapter;
import com.o9pathshala.global.GlobalData;

public class LoadMoreQuestionsAsynTask extends AsyncTask<String, String, Void> {
	private String sql;
	private String ip;
	private InputStream is;
	private HttpEntity entity;
	private String result = "";
	private ProgressDialog progressDialog;
	private RelativeLayout relativeLayout;
	private Context context;
	
	public LoadMoreQuestionsAsynTask(String sql, final Context context, RelativeLayout relativeLayout) {
		this.sql = sql;
		this.context = context;
		this.relativeLayout = relativeLayout;
		ResourceBundle rb = ResourceBundle.getBundle("network");
		ip = rb.getString("ip");
		this.sql = this.sql.replaceAll("LOWER_LIMIT", GlobalData.questions.size()+ "");
		this.sql = this.sql.replaceAll("UPPER_LIMIT", "5");
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				this.cancel();
				((Activity)context).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if(null != context && LoadMoreQuestionsAsynTask.this.isCancelled())
							Toast.makeText(context.getApplicationContext().getApplicationContext(),"Connection failed..", Toast.LENGTH_LONG).show();
					}
				});
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, 15000);
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Please Wait.....");
		progressDialog.show();
		progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				LoadMoreQuestionsAsynTask.this.cancel(true);
			}
		});
		super.onPreExecute();
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
			LoadMoreQuestionsAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void v) {
		 progressDialog.dismiss();
		 Log.d("log", sql);
		try{
			if(result.equals("false"))
				Toast.makeText(context, "No more questions found", Toast.LENGTH_LONG).show();
			else{
				ProgressBar progressBar;
				List<QuestionDTO> questions;
				JSONDecode jsonDecode = new JSONDecode(context,result);
				questions = jsonDecode.decode();
				progressBar = (ProgressBar)relativeLayout.findViewById(R.id.progressBarDiscussionFourm);
				progressBar.setVisibility(View.GONE);
				GlobalData.questions.addAll(questions);
				ListView listView = (ListView) relativeLayout.findViewById(R.id.listAllQuestions);
				AllQuestionsListAdapter adapter = new AllQuestionsListAdapter(context, GlobalData.questions);
				listView.setAdapter(adapter);
			}
		}
		catch(Exception e){
			LoadMoreQuestionsAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		
		}
		super.onPostExecute(v);
	}
}
