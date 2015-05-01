package com.o9pathshala.notifications;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.global.GlobalData;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.student.slidingmenu.fragments.MainActivity;

public class GetNotificationAsynTask extends AsyncTask<String, String, Void>{
	private String ip, query;
	private InputStream is;
	private HttpEntity entity;
	private String result = "";
	private Context context;
	private SharedPreferences sharedPreferences;
	private SessionDTO sessionDTO;
	
	public GetNotificationAsynTask(final Context context) {
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		Gson gson = new Gson();
		sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
		ResourceBundle rb = ResourceBundle.getBundle("network");
		ip = rb.getString("ip");
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				this.cancel();
				}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, 15000);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
		protected Void doInBackground(String... params) {
		if(null != GlobalData.last_sync)
			query = "select * from o9_" + sessionDTO.getCurrentInstitutesId() + "_test_notification where `create_time` > " +GlobalData.last_sync.toGMTString();
		else
			query = "select * from o9_" + sessionDTO.getCurrentInstitutesId() + "_test_notification" ;
		List<NameValuePair> list = new ArrayList<NameValuePair>(1);
		try {
			HttpClient httpClient = new DefaultHttpClient();
			list.add(new BasicNameValuePair("query", query));
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/get_notifications.php");
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
			Log.d("log", e.getMessage());
			GetNotificationAsynTask.this.cancel(true);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		super.onPostExecute(v);
			if(!result.equals("false")){
				NotificationManager notifManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
				Notification note = new Notification(R.drawable.logo, "Message from O9Paathshala", System.currentTimeMillis());
				Intent intent = new Intent(context, MainActivity.class);
		        PendingIntent bintent = PendingIntent.getActivity(context, 0,intent, 0);
		        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.notification);
		        mediaPlayer.start();
		        note.setLatestEventInfo(context, "New Tests has been uploaded " + result, "Review new tests uploaded by o9paathshala for you !!", bintent);
		        notifManager.notify(123, note);
			}
	        java.util.Date date= new java.util.Date();
			GlobalData.last_sync = new Timestamp(date.getTime());
			
	}
}
