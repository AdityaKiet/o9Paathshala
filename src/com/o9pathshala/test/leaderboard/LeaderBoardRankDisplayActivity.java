package com.o9pathshala.test.leaderboard;

import java.io.File;
import java.io.FileInputStream;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.global.GlobalData;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.settings.RoundImage;
import com.o9pathshala.student.test.dto.TestDTO;
import com.o9pathshala.test.database.SqlConstants;

public class LeaderBoardRankDisplayActivity extends Activity implements SqlConstants{
	private SharedPreferences sharedPreferences;
	private Bitmap resized, bitmap;
	private SessionDTO sessionDTO;
	private ImageView profilePic;
	private TextView txtTestName;
	private TestDTO testDTO;
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leaderboard_rank_display);
		int position = getIntent().getIntExtra("position", 0);
		testDTO = GlobalData.allTests.get(position);
		populate();
		String sql = MY_RANK;
		sql = sql.replace("INSTITUTE_ID", String.valueOf(sessionDTO.getCurrentInstitutesId()));
		sql = sql.replace("TEST_ID", String.valueOf(GlobalData.allTests.get(position).getId()));
		GetRankAsynTask getRankAsynTask = new GetRankAsynTask(LeaderBoardRankDisplayActivity.this, sql, layout);
		getRankAsynTask.execute();
		
	}

	private void populate() {
		Gson gson = new Gson();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String json = sharedPreferences.getString("session", null);
	    sessionDTO = gson.fromJson(json, SessionDTO.class);
		layout = (LinearLayout) findViewById(R.id.rankDisplayParent);
		profilePic = (ImageView)findViewById(R.id.imgprofilepicRankDisplay);
		txtTestName = (TextView)findViewById(R.id.txtTestNameRankDisplay);
		txtTestName.setText("Test  : " + testDTO.getTestName());
		String extr = Environment.getExternalStorageDirectory().toString();
		File f= new File(extr + "/.o9Pathshala/o9Pathshala-Images/" + sessionDTO.getId() + ".png");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		try {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
		     	resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
		     	RoundImage roundedImage = new RoundImage(resized);
		     	profilePic.setImageDrawable(roundedImage);
		     } 
		catch(Exception e){
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_self);
				resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
				RoundImage roundedImage = new RoundImage(resized);
				profilePic.setImageDrawable(roundedImage);
		}
	}

}
