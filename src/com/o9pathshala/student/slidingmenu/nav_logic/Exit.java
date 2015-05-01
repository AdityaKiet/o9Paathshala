package com.o9pathshala.student.slidingmenu.nav_logic;

import android.app.Activity;
import android.content.Context;

public class Exit {
	public static void exit(Context context) {
		((Activity)context).finish();
		System.exit(0);
	}
}
