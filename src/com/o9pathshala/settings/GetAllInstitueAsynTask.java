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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.o9pathshala.profile.dto.InstituteDTO;
import com.o9pathshala.profile.dto.SessionDTO;

public class GetAllInstitueAsynTask extends AsyncTask<String, String, Void> {
	private Context context;
	private InputStream is;
	private HttpEntity entity;
	private String result = "";
	private ProgressDialog progressDialog;
	private SharedPreferences sharedPreferences;
	private SessionDTO newSessionDTO;
	private String ip;
	private int currentPosition = -1;
	public GetAllInstitueAsynTask(final Context context) {
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)this.context).getBaseContext());
		Gson gson = new Gson();
	    String json = sharedPreferences.getString("session", null);
	    newSessionDTO = gson.fromJson(json, SessionDTO.class);
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
						if(context!= null && GetAllInstitueAsynTask.this.isCancelled())
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
				GetAllInstitueAsynTask.this.cancel(true);
			}
		});
		super.onPreExecute();
	}
	@Override
	protected Void doInBackground(String... params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>(1);
		list.add(new BasicNameValuePair("email", newSessionDTO.getEmail()));
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/get_institutes.php");
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
			GetAllInstitueAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void v) {
		progressDialog.dismiss();
		try{
			DecodeInstituteName decodeInstituteName = new DecodeInstituteName(result);
			final List<InstituteDTO>list = decodeInstituteName.getInstitutes();
			String[] names = new String[list.size()];
			Integer[] ids = new Integer[list.size()];
			for(int i = 0;i < list.size(); i++){
				names[i]= list.get(i).getName();
				ids[i]= list.get(i).getId();
			}
			
			AlertDialog.Builder alert = new AlertDialog.Builder(context);
			
			alert.setSingleChoiceItems(names, -1, new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					currentPosition = which;
				}
			});
			alert.setTitle("Select Institute ");
			alert.setPositiveButton("Cancel", null);
			alert.setNegativeButton("Select", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if(-1 == currentPosition)
						Toast.makeText(context.getApplicationContext().getApplicationContext(),"Select an institute", Toast.LENGTH_LONG).show();
					else{
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Enter your password");
						final EditText etoPassword = new EditText(context);
						etoPassword.setHint("Enter Password");
						etoPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
						etoPassword.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
						etoPassword.setPadding(15, 25, 5, 10);
						LinearLayout linearLayout = new LinearLayout(context);
						linearLayout.setOrientation(LinearLayout.VERTICAL);
						linearLayout.addView(etoPassword);
						builder.setView(linearLayout);
						builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								if(etoPassword.getText().toString().trim().equals(""))
									Toast.makeText(context, "Password is required", Toast.LENGTH_LONG).show();
								else{
									ChangeInstituteCheckPasswordAsynTask changeInstituteCheckPasswordAsynTask = new ChangeInstituteCheckPasswordAsynTask(context,list.get(currentPosition), etoPassword.getText().toString());
									changeInstituteCheckPasswordAsynTask.execute();
								}
							}
						});
						builder.setNegativeButton("Cancel", null);
						builder.show();
					}
				}
			});
			alert.show();
			
		} 
		catch(Exception e){
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Error occured", Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(v);
	}
}
