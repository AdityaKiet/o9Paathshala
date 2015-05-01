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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.o9pathshala.database.SQLConstants;
import com.o9pathshala.discussionfourm.dto.ExploredQuestionDTO;
import com.o9pathshala.profile.dto.SessionDTO;

public class LikeQuestionAsynTask  extends AsyncTask<String, String, Void> implements SQLConstants{
	private ExploredQuestionDTO exploredQuestionDTO;
	private String query;
	private SharedPreferences sharedPreferences;
	private InputStream is;
	private HttpEntity entity;
	private String result = "";
	private String ip;
	private ProgressDialog progressDialog;
	private Context context;
	private SessionDTO sessionDTO;
	
	public LikeQuestionAsynTask(final Context context, ExploredQuestionDTO exploredQuestionDTO, String query) {
		this.context = context;
		this.exploredQuestionDTO = exploredQuestionDTO;
		this.query = query;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		Gson gson = new Gson();
		String json = sharedPreferences.getString("session", null);
	    sessionDTO = gson.fromJson(json, SessionDTO.class);
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
						if(null != context && LikeQuestionAsynTask.this.isCancelled())
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
				LikeQuestionAsynTask.this.cancel(true);
			}
		});
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(String... params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>(1);
		list.add(new BasicNameValuePair("query", query));
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/post_answer.php");
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
			LikeQuestionAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext(),"Exception " + "Error Occured", Toast.LENGTH_LONG).show();
		}
		return null;
	}
	
	
	@Override
	protected void onPostExecute(Void v) {
		progressDialog.dismiss();
		try{
		if(result.equals("true")){
			String query = GET_EXPLORED_QUESTION;
			query = query.replace("INSTITUTE_ID", "" + sessionDTO.getCurrentInstitutesId());
			query = query.replace("POST_ID", String.valueOf(exploredQuestionDTO.getQuestion().getId()));
			query = query.replace("USER_ID", String.valueOf(sessionDTO.getId()));
			String query1 = GET_POST_ANSWER;
			query1 = query1.replace("INSTITUTE_ID", "" + sessionDTO.getCurrentInstitutesId());
			query1 = query1.replace("POST_ID", String.valueOf(exploredQuestionDTO.getQuestion().getId()));
			query1 = query1.replace("USER_ID", String.valueOf(sessionDTO.getId()));
			ExploreQuestionAfterLikeAsynTask asyn = new ExploreQuestionAfterLikeAsynTask(query, query1, context);
			asyn.execute();
		}else
			Toast.makeText(context.getApplicationContext(),"Exception " + "Error Occured", Toast.LENGTH_LONG).show();
		}catch(Exception e){
			Toast.makeText(context.getApplicationContext(),"Exception " + "Error Occured", Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(v);
	}
}
