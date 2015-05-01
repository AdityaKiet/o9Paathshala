package com.o9pathshala.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.o9.R;
import com.o9pathshala.login.LoginAsynTask;
import com.o9pathshala.login.dto.LoginDTO;
import com.o9pathshala.security.Encryptor;

public class MainActivity extends Activity implements OnClickListener {
	private EditText etEmailId, etPassword;
	private Button btnLogin,btnRegister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guest_login);
		btnLogin = (Button) findViewById(R.id.btnGuestLogin);
		etEmailId = (EditText) findViewById(R.id.etGuestID);
		btnRegister = (Button) findViewById(R.id.btnGuestRegister);
		etPassword = (EditText) findViewById(R.id.etGuestPassword);
		btnRegister.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnGuestLogin:
			final LoginDTO loginDTO = new LoginDTO();
			loginDTO.setId(etEmailId.getText().toString());
			loginDTO.setPassword(Encryptor.encryptSHA512(etPassword.getText().toString()));

			LoginAsynTask loginAsynTask = new LoginAsynTask(MainActivity.this, loginDTO);
			loginAsynTask.execute();
			break;
		case R.id.btnGuestRegister:
			Intent intent = new Intent("com.o9pathshala.register.RegisterActivity");
			startActivity(intent);
			MainActivity.this.finish();
			break;
		
		default:
			break;
		}
	}


}