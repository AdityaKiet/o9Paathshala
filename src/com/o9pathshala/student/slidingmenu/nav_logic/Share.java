package com.o9pathshala.student.slidingmenu.nav_logic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class Share {
	public static void share(Context context) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Join me on o9Paathshala App !");
		intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi ! Loved the o9Paathshala App. Download from http://goo.gl/KurEeX .");
		((Activity) context)
				.startActivity(Intent.createChooser(intent, "Select option"));
	}
}
