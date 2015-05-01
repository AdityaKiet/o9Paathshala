package com.o9pathshala.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class OpenActivity {
	public static void newActivity(Context context,String activity) {
		Intent intent =  new Intent(activity);
		((Activity)context).startActivity(intent);
	}
}
