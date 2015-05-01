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
import com.o9pathshala.profile.dto.InstituteDTO;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.security.Encryptor;

public class ChangeInstituteCheckPasswordAsynTask extends AsyncTask<String, String, Void> {
	private Context context;
	private InputStream is;
	private SessionDTO sessionDTO;
	private HttpEntity entity;
	private String result = "";
	private String ip;
	private ProgressDialog progressDialog;
	private InstituteDTO instituteDTO;
	private String password;
	private SharedPreferences sharedPreferences;
	
	public ChangeInstituteCheckPasswordAsynTask(final Context context,InstituteDTO instituteDTO, String password) {
		this.instituteDTO = instituteDTO;
		this.context = context;
		this.password = password;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)this.context).getBaseContext());
		Gson gson = new Gson();
		sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
		ResourceBundle rb = ResourceBundle.getBundle("network");
		ip = rb.getString("ip");
		TimerTask task = new TimerTask() {
			public void run() {
				progressDialog.dismiss();
				this.cancel();
				((Activity)context).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if(null != context && ChangeInstituteCheckPasswordAsynTask.this.isCancelled())
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
				ChangeInstituteCheckPasswordAsynTask.this.cancel(true);
			}
		});
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(String... params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>(1);
		list.add(new BasicNameValuePair("password", Encryptor.encryptSHA512(password)));
		list.add(new BasicNameValuePair("email", sessionDTO.getEmail()));
		list.add(new BasicNameValuePair("institute_id", String.valueOf(instituteDTO.getId())));
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/student_info_change_institute.php");
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
			ChangeInstituteCheckPasswordAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void v){
		progressDialog.dismiss();
		if(("false").equals(result))
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Enter correct password", Toast.LENGTH_LONG).show();
		else{
			UpdateProfileAsynTask updateProfileAsynTask = new UpdateProfileAsynTask(context, instituteDTO);
			updateProfileAsynTask.execute();
		}
	}
}
