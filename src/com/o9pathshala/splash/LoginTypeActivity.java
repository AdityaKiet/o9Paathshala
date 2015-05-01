package com.o9pathshala.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.o9.R;

public class LoginTypeActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_type);
		Button btnMemberLogin = (Button) findViewById(R.id.btnMemberLogin);
		Button btnGuestLogin = (Button) findViewById(R.id.btnGuestLogin);
		btnGuestLogin.setOnClickListener(this);
		btnMemberLogin.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnGuestLogin:
			intent = new Intent("com.o9pathshala.student.MainActivity");
			startActivity(intent);
			LoginTypeActivity.this.finish();
			break;
		case R.id.btnMemberLogin:
			intent = new Intent("com.o9pathshala.login.LoginActivity");
			startActivity(intent);
			LoginTypeActivity.this.finish();
			break;
		default:
			break;
		}
	}

}
