package com.o9pathshala.settings.profile;

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
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.profile.dto.ProfileDTO;
import com.o9pathshala.settings.UpdateSession;

public class UpdateAddressAsynTask extends AsyncTask<String, String, Void>{
	private Context context;
	private InputStream is;
	private HttpEntity entity;
	private String result = "";
	private ProgressDialog progressDialog;
	private SharedPreferences sharedPreferences;
	private String ip, address;
	/*private SessionDTO sessionDTO;*/
	private SessionDTO newSessionDTO;
	private ProfileDTO profileDTO;
	public UpdateAddressAsynTask(final Context context, String address) {
		this.context = context;
		this.address = address;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)this.context).getBaseContext());
		Gson gson = new Gson();
	    String json = sharedPreferences.getString("session", null);
	    newSessionDTO = gson.fromJson(json, SessionDTO.class);
	    json = sharedPreferences.getString("profileDTO", null);
	    profileDTO = gson.fromJson(json, ProfileDTO.class);
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
						if(null != null && UpdateAddressAsynTask.this.isCancelled())
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
		progressDialog.setTitle("Loading.....");
		progressDialog.setMessage("Please Wait.....");
		progressDialog.show();
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				UpdateAddressAsynTask.this.cancel(true);
			}
		});
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(String... params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>(1);
		list.add(new BasicNameValuePair("id", String.valueOf(newSessionDTO.getId())));
		list.add(new BasicNameValuePair("institue_id", newSessionDTO.getCurrentInstitutesId()+""));
		list.add(new BasicNameValuePair("address", address));
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/update_address.php");
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
			UpdateAddressAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void v) {
		progressDialog.dismiss();
		try{
			if(Integer.parseInt(result) == 1){
				Toast.makeText(context.getApplicationContext().getApplicationContext(),"Address has been updated", Toast.LENGTH_LONG).show();
				profileDTO.setAddress(address);
				UpdateSession.update(context, profileDTO, newSessionDTO);
			}else{
				Toast.makeText(context.getApplicationContext().getApplicationContext(),"Address not updated", Toast.LENGTH_LONG).show();
			}
		}catch(Exception e){
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(v);
	}
}
