package com.o9pathshala.login;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.profile.dto.ProfileDTO;

public class SetDefaultInstituteAsynTask extends AsyncTask<String, String, Void>{
	private Context context;
	private SessionDTO newSessionDTO;
	private InputStream is;
	private HttpEntity entity;
	private String result = "";
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor sharedEditor;
	private ProgressDialog progressDialog;
	private ProfileDTO profileDTO;
	private String ip;
	public SetDefaultInstituteAsynTask(Context context,SessionDTO newSessionDTO, ProfileDTO profileDTO) {
		this.context = context;
		this.newSessionDTO = newSessionDTO;
		this.profileDTO = profileDTO;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)this.context).getBaseContext());
		sharedEditor = sharedPreferences.edit();
		ResourceBundle rb = ResourceBundle.getBundle("network");
		ip = rb.getString("ip");
	}
	
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Loading.....");
		progressDialog.setMessage("Please Wait.....");
		progressDialog.show();
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				
			}
		});
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(String... params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>(1);
		list.add(new BasicNameValuePair("email", newSessionDTO.getEmail()));
		list.add(new BasicNameValuePair("name", newSessionDTO.getName()));
		list.add(new BasicNameValuePair("institute_id", String.valueOf(newSessionDTO.getCurrentInstitutesId())));
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/update_default_institute.php");
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
			SetDefaultInstituteAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		progressDialog.dismiss();
		
		try{
			int updates = Integer.parseInt(result);
		if(updates > 0){
			newSessionDTO.setDefaultInstituteId(true);
			Gson gson = new Gson();
			String json = gson.toJson(newSessionDTO);
		    sharedEditor.putString("session", json);
		    sharedEditor.putBoolean("isLogin", true);
		    json = gson.toJson(profileDTO);
		    sharedEditor.putString("profileDTO", json);
		    sharedEditor.commit();
		    new SetProfileAsynTask(context).execute();
		}
		else{
			SetDefaultInstituteAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Login Failed Try again" , Toast.LENGTH_LONG).show();
			Intent intent = new Intent("com.o9pathshala.student.MainActivity");
			context.startActivity(intent);
			((Activity)context).finish();
			}
		}
		catch(Exception e){
			SetDefaultInstituteAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
			Intent intent = new Intent("com.o9pathshala.student.MainActivity");
			context.startActivity(intent);
			((Activity)context).finish();
		}
		super.onPostExecute(v);
	}
}
