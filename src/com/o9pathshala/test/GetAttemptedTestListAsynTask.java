package com.o9pathshala.test;

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
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.o9.R;
import com.o9pathshala.global.GlobalData;
import com.o9pathshala.student.test.dto.TestDTO;
import com.o9pathshala.student.test.tabs.AllTestsAdapter;

public class GetAttemptedTestListAsynTask extends AsyncTask<String, String, Void> {
	private Context context;
	private InputStream is;
	private HttpEntity entity;
	private String result = "";
	private String ip;
	private ProgressDialog progressDialog;
	private String query;
	private RelativeLayout relativeLayout;
	
	
	public GetAttemptedTestListAsynTask(final Context context, String query, RelativeLayout relativeLayout) {
		this.context = context;
		this.query = query;
		this.relativeLayout = relativeLayout;
		ResourceBundle rb = ResourceBundle.getBundle("network");
		ip = rb.getString("ip");
		TimerTask task = new TimerTask() {
			public void run() {
				progressDialog.dismiss();
				this.cancel();
				((Activity)context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(null != context && GetAttemptedTestListAsynTask.this.isCancelled())
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
				GetAttemptedTestListAsynTask.this.cancel(true);
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
			HttpPost httpPost = new HttpPost(ip+"/o9pathshala/get_data.php");
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
			GetAttemptedTestListAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		progressDialog.dismiss();
		try{
		if(result.equals("false")){
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"No Attempted tests found.. try again later", Toast.LENGTH_LONG).show();
		}
		else{
				DecodeAttemptedTest decodeAttemptedTest = new DecodeAttemptedTest(result);
				ListView listView = (ListView) relativeLayout.findViewById(R.id.listAttemptedTests);
				List<TestDTO> list = decodeAttemptedTest.getTests();
				if(null != list){
					GlobalData.myTests = list;
				AllTestsAdapter adapter = new AllTestsAdapter(context, list);
				listView.setAdapter(adapter);
				}else{
					listView.setVisibility(View.GONE);
				}
			}
		
		}
		
		catch(Exception e){
			GetAttemptedTestListAsynTask.this.cancel(true);
			Toast.makeText(context.getApplicationContext().getApplicationContext(),"Exception " + e.getMessage(), Toast.LENGTH_LONG).show();
		
		}
		super.onPostExecute(v);
	}


}