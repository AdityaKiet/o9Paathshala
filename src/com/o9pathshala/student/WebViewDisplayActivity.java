package com.o9pathshala.student;

import com.o9.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewDisplayActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_display);
		String url = getIntent().getExtras().getString("url");
		WebView webView = (WebView)findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/" + url + ".html");
	}

}
