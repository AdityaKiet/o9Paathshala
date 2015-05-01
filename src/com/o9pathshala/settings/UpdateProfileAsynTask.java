package com.o9pathshala.settings;

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
import com.o9pathshala.network.LoadImageAsynTask;
import com.o9pathshala.profile.dto.InstituteDTO;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.test.database.SqlConstants;

public class UpdateProfileAsynTask extends AsyncTask<String, String, Void> implements SqlConstants{
	private Context context;
	private InputStream is;
	private HttpEntity entity;
	private String result = "";
	private ProgressDialog progressDialog;
	private SharedPreferences sharedPreferences;
	private SessionDTO sessionDTO;
	private String ip;
	private SharedPreferences.Editor sharedEditor;
	private InstituteDTO instituteDTO;
	
	public UpdateProfileAsynTask(final Context context) {
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)this.context).getBaseContext());
		sharedEditor = sharedPreferences.edit();
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
						if(context!= null && UpdateProfileAsynTask.this.isCancelled())
							Toast.makeText(context.getApplicationContext().getApplicationContext(),"Connection failed..", Toast.LENGTH_LONG).show();
					}
				});
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, 15000);
	}
	
	public UpdateProfileAsynTask(final Context context, InstituteDTO instituteDTO) {
		this.context = context;
		this.instituteDTO = instituteDTO;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)this.context).getBaseContext());
		sharedEditor = sharedPreferences.edit();
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
						if(context!= null && UpdateProfileAsynTask.this.isCancelled())
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
				UpdateProfileAsynTask.this.cancel(true);
			}
		});
		super.onPreExecute();
	}
	@Override
	protected Void doInBackground(String... params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>(1);
		list.add(new BasicNameValuePair("email", String.valueOf(sessionDTO.getEmail())));
		list.add(new BasicNameValuePair("institue_id", String.valueOf(sessionDTO.getCurrentInstitutesId())));
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/student_info.php");
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
			UpdateProfileAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void v) {
		progressDialog.dismiss();
		try{
			if(result.equals("false"))
				Toast.makeText(context.getApplicationContext().getApplicationContext(),"Choose different institute " , Toast.LENGTH_LONG).show();
			else{
			AccountUpdate accountUpdate = new AccountUpdate(result,context);
			accountUpdate.update();
			if(null != instituteDTO){
			
			sessionDTO.setInstituteName(instituteDTO.getName());
			sessionDTO.setCurrentInstitutesId(instituteDTO.getId());
			sessionDTO.setDefaultInstituteId(true);
			Gson gson = new Gson();
			String json = gson.toJson(sessionDTO);
		    sharedEditor.putString("session", json);
		    sharedEditor.commit();
			}
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Institute has been changed" , Toast.LENGTH_LONG).show();
			/*LoadImageAsynTask loadImageAsynTask = new LoadImageAsynTask(context, GET_PROFILE_PIC);
			loadImageAsynTask.execute();*/
				}
			}
		catch(Exception e){
			UpdateProfileAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(v);
	}
}
