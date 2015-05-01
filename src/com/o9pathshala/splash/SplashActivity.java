package com.o9pathshala.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.o9.R;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		// Calendar cal = Calendar.getInstance();   
         //startService(new Intent(getApplicationContext(), MyService.class));
         /*Intent intent = new Intent(getApplicationContext(), NotificationBuilderAlarmReceiver.class);
         PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 234324243, intent, 0);
         AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000, pendingIntent);*/
         Thread thread = new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					Toast.makeText(getApplicationContext(),"Error occured. Try again later", Toast.LENGTH_LONG).show();
				} finally {

					Splash splash = new Splash();
					if (splash.isLogin(SplashActivity.this)) {
						Intent intent = new Intent("com.o9pathshala.student.slidingmenu.fragments.MainActivity");
						startActivity(intent);
						SplashActivity.this.overridePendingTransition(R.drawable.zooom_in,0);
					} else {
						Intent intent = new Intent("com.o9pathshala.student.MainActivity");
						startActivity(intent);
						SplashActivity.this.overridePendingTransition(R.drawable.zooom_in,0);
					}
				}

			}
		};

			thread.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public void onBackPressed() {

	}
	
	
}
