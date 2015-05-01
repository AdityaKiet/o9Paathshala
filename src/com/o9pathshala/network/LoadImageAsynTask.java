package com.o9pathshala.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.o9pathshala.profile.dto.SessionDTO;

public class LoadImageAsynTask extends AsyncTask<String, String, Bitmap> {
	private Bitmap bitmap;
	private SharedPreferences sharedPreferences;
	private SessionDTO sessionDTO;
	private String ip;
	private String url;
	
	
	public LoadImageAsynTask(Context context, String url) {
		this.url = url;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)context).getBaseContext());
		Gson gson = new Gson();
	    String json = sharedPreferences.getString("session", null);
	    sessionDTO = gson.fromJson(json, SessionDTO.class);
	    ResourceBundle rb = ResourceBundle.getBundle("network");
		ip = rb.getString("ip");
	}
	
	@Override
        protected void onPreExecute() {
            super.onPreExecute();
    }
       protected Bitmap doInBackground(String... args) {
         try {
        	   url = url.replaceAll("IP", ip);
        	   url = url.replaceAll("INSTITUTE_ID", String.valueOf(sessionDTO.getCurrentInstitutesId()));
        	   url = url.replaceAll("USER_ID", String.valueOf(sessionDTO.getId()));
        	   Log.d("log", url);
               bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
        } catch (Exception e) {
        	url = url.replaceAll(".png", ".jpg");
     	   	Log.d("log", url);
     	   	try{
     	   		bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
     	   	}
     	   	catch(Exception e1){
     	   	url = url.replaceAll(".jpg", ".jpeg");
     	   	Log.d("log", url);
     	   
     	   	try{
     	   		bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
     	   	}
     	   	catch(Exception e2){
     	   		Log.d("log", e.getMessage());
     	   	}
        }
     	   	}
      return bitmap;
       }
       
       
       protected void onPostExecute(Bitmap image) {
    	   String extr = Environment.getExternalStorageDirectory().toString();
           File mFolder = new File(extr + "/.o9Pathshala");

           if (!mFolder.exists()) {
               mFolder.mkdir();
           }

           String strF = mFolder.getAbsolutePath();
           File mSubFolder = new File(strF + "/o9Pathshala-Images");

           if (!mSubFolder.exists()) {
               mSubFolder.mkdir();
           }

           String s = sessionDTO.getId() + ".png";

           File f = new File(mSubFolder.getAbsolutePath(),s);
           FileOutputStream fos = null;
           try {
               fos = new FileOutputStream(f);
               bitmap.compress(Bitmap.CompressFormat.PNG,70, fos);
               fos.flush();
               fos.close();
           }catch (Exception e) {
               e.printStackTrace();
           }
       }
   }