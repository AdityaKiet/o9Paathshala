package com.o9pathshala.settings.profile;

import java.io.File;
import java.io.FileInputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.profile.dto.ProfileDTO;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.settings.RoundImage;

public class UpdateAccountActivity extends Activity implements OnClickListener{
	private SharedPreferences sharedPreferences;
	private Bitmap resized;
	private ListView listView;
	private ProfileDTO profileDTO;
	private SessionDTO sessionDTO;
	private TextView txtName, txtInstituteName;
	private ImageView profilePic;
	private Bitmap bitmap;
	private File f;
	private UpdateAccountListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_account);
		populate();
		String extr = Environment.getExternalStorageDirectory().toString();
		f= new File(extr + "/.o9Pathshala/o9Pathshala-Images/" + sessionDTO.getId() + ".png");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		try {
		     bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
		     resized = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
		     RoundImage roundedImage = new RoundImage(resized);
		     profilePic.setImageDrawable(roundedImage);
		     } 
		catch(Exception e){
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_self);
			resized = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
			RoundImage roundedImage = new RoundImage(resized);
			profilePic.setImageDrawable(roundedImage);
		}
	}
	private void populate() {
		listView = (ListView)findViewById(R.id.listAccountUpdate);
		txtInstituteName = (TextView)findViewById(R.id.txtInstituteName);
		txtName = (TextView) findViewById(R.id.txtProfileName);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		profilePic = (ImageView) findViewById(R.id.imgprofilepic);
		profilePic.setOnClickListener(this);
		Gson gson = new Gson();
		String json = sharedPreferences.getString("profileDTO", null);
	    profileDTO = gson.fromJson(json, ProfileDTO.class);
	    json = sharedPreferences.getString("session", null);
	    sessionDTO = gson.fromJson(json, SessionDTO.class);
	    txtName.setText(sessionDTO.getName());
	    txtInstituteName.setText(sessionDTO.getInstituteName());
	   
	    String[] headings = {"Name","Password","Gender","Contact","Batch Name","Date of Birth","E-mail","Address"};
		String[] values ={String.valueOf(profileDTO.getName()),"",String.valueOf(profileDTO.getGender()),profileDTO.getContact(),
				profileDTO.getBatchName(),profileDTO.getDob(),profileDTO.getEmail(), profileDTO.getAddress()};
		adapter = new UpdateAccountListAdapter(UpdateAccountActivity.this,headings,values);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				UpdateAccount updateAccount = new UpdateAccount(UpdateAccountActivity.this,position);
				updateAccount.update();
			}
		});
		getActionBar().setDisplayHomeAsUpEnabled(true);
			
	}
	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.imgprofilepic)
		{	
			if(f.length() == 0)
				Toast.makeText(UpdateAccountActivity.this, "No profile pic", Toast.LENGTH_LONG).show();
			else{
				Intent intent = new Intent(UpdateAccountActivity.this, ProfilePicActivity.class);
				startActivity(intent);
				}
			}
		}

	}
