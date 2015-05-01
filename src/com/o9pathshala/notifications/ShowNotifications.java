package com.o9pathshala.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ShowNotifications extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		Log.d("log", bundle.getString("string"));
	}

}
