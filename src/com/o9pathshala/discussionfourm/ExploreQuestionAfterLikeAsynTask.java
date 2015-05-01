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
import android.widget.Toast;

import com.o9pathshala.discussionfourm.DecodeExploreQuestion;

public class ExploreQuestionAfterLikeAsynTask extends AsyncTask<String, String, Void>{
	private String sql;
	private String ip;
	private InputStream is;
	private HttpEntity entity;
	private String result = "", result1=  "";
	private ProgressDialog progressDialog;
	private Context context;
	private String sql1;
	public ExploreQuestionAfterLikeAsynTask(String sql, String sql1, final Context context) {
		this.sql = sql;
		this.sql1 = sql1;
		this.context = context;
		ResourceBundle rb = ResourceBundle.getBundle("network");
		ip = rb.getString("ip");
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				progressDialog.dismiss();
				this.cancel();
				((Activity)context).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if(null != context && ExploreQuestionAfterLikeAsynTask.this.isCancelled())
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
				ExploreQuestionAfterLikeAsynTask.this.cancel(true);
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
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/get_explore_questions.php");
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
			
			
			httpPost = new HttpPost(ip+"/o9pathshala/get_explore_answers.php");
			list = new ArrayList<NameValuePair>(1);
			list.add(new BasicNameValuePair("query", sql1));
			httpPost.setEntity(new UrlEncodedFormEntity(list));
			httpResponse = httpClient.execute(httpPost);
			entity = httpResponse.getEntity();
			is = entity.getContent();
			bufferedReader = new BufferedReader(new InputStreamReader(is));
			stringBuilder = new StringBuilder();
			line = "";
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			is.close();
			result1 = stringBuilder.toString();
		} catch (Exception e) {
			Log.d("log", e.getMessage());
			ExploreQuestionAfterLikeAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext(),"Exception " + "Error Occured", Toast.LENGTH_LONG).show();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		progressDialog.dismiss();
		
		try{
			DecodeExploreQuestion decodeExploreQuestion = new DecodeExploreQuestion(result, result1, context);
			decodeExploreQuestion.decode();
		}
		catch(Exception e){
			Log.d("log", e.getMessage());
			ExploreQuestionAfterLikeAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext(),"Exception " + "Error Occured", Toast.LENGTH_LONG).show();
		}
		((Activity)context).finish();
		super.onPostExecute(v);
	}
}
