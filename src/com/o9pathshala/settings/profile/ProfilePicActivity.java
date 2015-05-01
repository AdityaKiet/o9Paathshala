package com.o9pathshala.settings.profile;

import java.io.File;
import java.io.FileInputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.profile.dto.SessionDTO;

public class ProfilePicActivity extends Activity {
	private Bitmap bitmap;
	private  SessionDTO sessionDTO;
	private ImageView image;
	private SharedPreferences sharedPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);
		populate();
			Gson gson = new Gson();
		    String json = sharedPref.getString("session", null);
		    sessionDTO = gson.fromJson(json, SessionDTO.class);
		    getActionBar().setTitle(sessionDTO.getName());
		try {
			String extr = Environment.getExternalStorageDirectory().toString();
			File f= new File(extr + "/.o9Pathshala/o9Pathshala-Images/" + sessionDTO.getId() + ".png");
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
			image.setImageBitmap(bitmap);
		}
		catch(Exception e){
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_self);
			image.setImageBitmap(bitmap);
		}
	   
	}

	private void populate() {
		image = (ImageView) findViewById(R.id.imgProfilePicFullScreen);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.profile_pic, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.share_pic:
			String extr = Environment.getExternalStorageDirectory().toString();
			File file= new File(extr + "/.o9Pathshala/o9Pathshala-Images/" + sessionDTO.getId() + ".png");
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			Uri uri = Uri.fromFile(file);
			intent.setType("image/png");
			intent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(intent, "Share image using"));
			
			break;

		}
		return super.onOptionsItemSelected(item);

	}

}
