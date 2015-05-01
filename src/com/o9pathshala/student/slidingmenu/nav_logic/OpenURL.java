package com.o9pathshala.student.slidingmenu.nav_logic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class OpenURL {
	public static void openURL(Context context, String url){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		((Activity)context).startActivity(browserIntent);
	}
}
