package com.o9pathshala.logout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;

public class Logout {
	private Context context;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor sharedEditor;
	public Logout(Context context) {
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)this.context).getBaseContext());
		sharedEditor = sharedPreferences.edit();
	}
	public void logout(){
		
		sharedEditor.putBoolean("isLogin", false);
		Gson gson = new Gson();
		String json = gson.toJson(null);
		sharedEditor.putString("session", json);
		sharedEditor.putString("profileDTO", json);
	    if(sharedEditor.commit()){
	    	Intent intent = new Intent("com.o9pathshala.student.MainActivity");
	    	((Activity)context).startActivity(intent);
	    	((Activity)context).finish();
	    	Toast.makeText(((Activity)context).getApplicationContext(), "Logout Successful..", Toast.LENGTH_LONG).show();
	    }else{
	    	Toast.makeText(((Activity)context).getApplicationContext(), "Logout Failed..", Toast.LENGTH_LONG).show();
	    }
	}
	
}
