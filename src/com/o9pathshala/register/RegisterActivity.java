package com.o9pathshala.register;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.o9.R;
import com.o9pathshala.security.validation.Validation;
import com.o9pathshala.user.dto.RegisterDTO;

public class RegisterActivity extends Activity implements OnClickListener{
	private EditText etName, etEmail, etPassword, etConfirmPassword;
	private Button btnRegister, btnBackToLogin;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		populate();
	}
	private void populate() {
		etName = (EditText)findViewById(R.id.etRegisterName);
		etPassword = (EditText)findViewById(R.id.etRegisterPassword);
		etEmail = (EditText)findViewById(R.id.etRegisterEmail);
		etConfirmPassword  = (EditText)findViewById(R.id.etRegisterConfirmPassword);
		btnRegister = (Button)findViewById(R.id.btnRegister);
		btnBackToLogin = (Button)findViewById(R.id.btnBackToLogin);
		btnRegister.setOnClickListener(this);
		btnBackToLogin.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(v.getId() == R.id.btnRegister){
			Validation validation = new Validation();
			if(etConfirmPassword.getText().toString().trim().equals("")||etEmail.getText().toString().trim().equals("")||etName.getText().toString().trim().equals("")||etPassword.getText().toString().trim().equals(""))
				Toast.makeText(getApplicationContext().getApplicationContext(),"Fields can't be blank", Toast.LENGTH_LONG).show();
			else if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
				Toast.makeText(getApplicationContext().getApplicationContext(),"Password not matched", Toast.LENGTH_LONG).show();
			}else if(!validation.validateEmail((etEmail.getText().toString()))){
				Toast.makeText(getApplicationContext().getApplicationContext(),"E-mail not valid", Toast.LENGTH_LONG).show();
			}else if(!validation.validatePassword(etPassword.getText().toString())){
				Toast.makeText(getApplicationContext().getApplicationContext(),"Password not valid", Toast.LENGTH_LONG).show();
			}else{
				RegisterDTO registerDTO = new RegisterDTO();
				registerDTO.setName(etName.getText().toString());
				registerDTO.setEmail(etEmail.getText().toString());
				registerDTO.setPassword(etPassword.getText().toString());
				RegisterAsynTask registerAsynTask = new RegisterAsynTask(registerDTO, RegisterActivity.this);
				registerAsynTask.execute();
			}
		}
		else if (v.getId() == R.id.btnBackToLogin){
			Intent intent = new Intent("com.o9pathshala.student.MainActivity");
			startActivity(intent);
			RegisterActivity.this.finish();
		}
	}
	public boolean isEmailValid(String email) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}
}
