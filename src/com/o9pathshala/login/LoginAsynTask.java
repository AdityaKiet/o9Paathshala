package com.o9pathshala.login;

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
import android.widget.Toast;

import com.o9pathshala.login.dto.LoginDTO;
import com.o9pathshala.ui.AlertDialogActivity;
import com.o9pathshala.ui.dto.AlertDialogDTO;

public class LoginAsynTask extends AsyncTask<String, String, Void> {
	private Context context;
	private LoginDTO loginDTO;
	private InputStream is;
	private HttpEntity entity;
	private String result = "";
	private String ip;
	private ProgressDialog progressDialog;
	
	public LoginAsynTask(final Context context, LoginDTO loginDTO) {
		this.context = context;
		this.loginDTO = loginDTO;
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
						if(null != context && LoginAsynTask.this.isCancelled())
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
				LoginAsynTask.this.cancel(true);
			}
		});
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(String... params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>(1);
		list.add(new BasicNameValuePair("email", loginDTO.getId()));
		list.add(new BasicNameValuePair("password", loginDTO.getPassword()));
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/login.php");
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
			LoginAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		progressDialog.dismiss();
		try{
		if(result.equals("false"))
			loginFailed();
		else{
			LoginInstituteSelect profile = new LoginInstituteSelect(context,result);
			profile.createProfile();
			}
		
		}
		
		catch(Exception e){
			LoginAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		
		}
		super.onPostExecute(v);
	}



	private void loginFailed() {
	AlertDialogDTO 	alertDialogDTO = new AlertDialogDTO();
	alertDialogDTO.setContext(context);
	alertDialogDTO.setMessage("Wrong id and password");
	alertDialogDTO.setNegativeButton(null);
	alertDialogDTO.setPositiveButon("Ok");
	alertDialogDTO.setTitle("Login Failed");
	AlertDialogActivity.alertDialogDisplay(alertDialogDTO, null);
	}

}
