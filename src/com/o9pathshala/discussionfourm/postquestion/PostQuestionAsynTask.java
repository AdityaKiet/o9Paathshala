package com.o9pathshala.discussionfourm.postquestion;

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

public class PostQuestionAsynTask extends AsyncTask<String, String, Void>{
	private String sql, tagQuery;
	private String ip;
	private InputStream is;
	private HttpEntity entity;
	private String result = "";
	private ProgressDialog progressDialog;
	private Context context;
	
	public PostQuestionAsynTask(String sql, String tagQuery, final Context context) {
		this.sql = sql;
		this.context = context;
		this.tagQuery = tagQuery;
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
						if(null != context && PostQuestionAsynTask.this.isCancelled())
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
				PostQuestionAsynTask.this.cancel(true);
			}
		});
		super.onPreExecute();
	}

	@Override
		protected Void doInBackground(String... params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>(1);
		if(null == tagQuery){
		list.add(new BasicNameValuePair("query", sql));
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/post_question_without_tags.php");
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
			PostQuestionAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}else{
			list.add(new BasicNameValuePair("tag_query", tagQuery));
			list.add(new BasicNameValuePair("query", sql));
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(ip+"/o9pathshala/post_question.php");
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
				PostQuestionAsynTask.this.cancel(true);
				Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		Log.d("log", result);
		progressDialog.dismiss();
		try{
		if(result.equals("true")){
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Question Submitted", Toast.LENGTH_LONG).show();
			((Activity)context).finish();
		}
		else
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Question not Submitted", Toast.LENGTH_LONG).show();
		}
		catch(Exception e){
			PostQuestionAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		
		}
		super.onPostExecute(v);
	}
}
