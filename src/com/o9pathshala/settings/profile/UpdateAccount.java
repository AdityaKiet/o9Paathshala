package com.o9pathshala.settings.profile;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.o9pathshala.profile.dto.ProfileDTO;
import com.o9pathshala.util.NetworkCheck;

public class UpdateAccount {
	private int position;
	private int index = -1;
	private Context context;
	private SharedPreferences sharedPreferences;
	private ProfileDTO profileDTO;
	public UpdateAccount(Context context,int position) {
		this.position = position;
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)context).getApplicationContext());
		Gson gson = new Gson();
		String json = sharedPreferences.getString("profileDTO", null);
	    profileDTO = gson.fromJson(json, ProfileDTO.class);
	}

	public void update() {
		switch (position) {
		case 0:
			if(NetworkCheck.isNetworkAvailable(context))
				updateName();
			else
				Toast.makeText(context.getApplicationContext().getApplicationContext(),"Network not available", Toast.LENGTH_LONG).show();
			break;
		case 1:
			if(NetworkCheck.isNetworkAvailable(context))
				updatePassword();
			else
				Toast.makeText(context.getApplicationContext().getApplicationContext(),"Network not available", Toast.LENGTH_LONG).show();
			break;
		case 2:
			if(NetworkCheck.isNetworkAvailable(context))
				updateGender();
			else
				Toast.makeText(context.getApplicationContext().getApplicationContext(),"Network not available", Toast.LENGTH_LONG).show();
			break;
		case 3:
			if(NetworkCheck.isNetworkAvailable(context))
				updateContact();
			else
				Toast.makeText(context.getApplicationContext().getApplicationContext(),"Network not available", Toast.LENGTH_LONG).show();
			break;
		case 5:
			if(NetworkCheck.isNetworkAvailable(context))
				dobUpdate();
			else
				Toast.makeText(context.getApplicationContext().getApplicationContext(),"Network not available", Toast.LENGTH_LONG).show();
			break;
		case 7:
			if(NetworkCheck.isNetworkAvailable(context))
				updateAddress();
			else
				Toast.makeText(context.getApplicationContext().getApplicationContext(),"Network not available", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}

	private void updateName() {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Name");
		final EditText editText = new EditText(context);
		if(null!=profileDTO.getName() && !profileDTO.getName().equals("null"))
			editText.setHint("Enter Name");
		editText.setText(profileDTO.getName());
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
		editText.setPadding(15, 25, 5, 10);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.addView(editText);
		alert.setView(linearLayout);
		alert.setPositiveButton("Update", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				UpdateNameAsynTask updateNameAsynTask = new UpdateNameAsynTask(context, editText.getText().toString());
				updateNameAsynTask.execute();
			}
		});
		alert.setNegativeButton("Cancel", null);
		alert.show();
		
	}

	private void updatePassword() {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Password");
		final EditText editTextOldPassword = new EditText(context);
		editTextOldPassword.setHint("Old Password");
		editTextOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		editTextOldPassword.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
		editTextOldPassword.setPadding(15, 25, 5, 10);
		final EditText editTextNewPassword = new EditText(context);
		editTextNewPassword.setHint("New Password");
		editTextNewPassword.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
		editTextNewPassword.setPadding(15, 25, 5, 10);
		editTextNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		final EditText editTextConfirmNewPassword = new EditText(context);
		editTextConfirmNewPassword.setHint("Confirm Old Password");
		editTextConfirmNewPassword.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
		editTextConfirmNewPassword.setPadding(15, 25, 5, 10);
		editTextConfirmNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.addView(editTextOldPassword);
		linearLayout.addView(editTextNewPassword);
		linearLayout.addView(editTextConfirmNewPassword);
		alert.setView(linearLayout);
		alert.setPositiveButton("Update", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(editTextConfirmNewPassword.getText().toString().trim().equals("") ||
						editTextNewPassword.getText().toString().trim().equals("") ||
						editTextOldPassword.getText().toString().trim().equals("")){
					Toast.makeText(context.getApplicationContext().getApplicationContext(),"Password can not be blank", Toast.LENGTH_LONG).show();
				}
				else if(!editTextNewPassword.getText().toString().equals(editTextConfirmNewPassword.getText().toString())){
					Toast.makeText(context.getApplicationContext().getApplicationContext(),"Password not matched", Toast.LENGTH_LONG).show();
				}else{
					UpdatePasswordAsynTask updatePasswordAsynTask = new UpdatePasswordAsynTask(context, editTextOldPassword.getText().toString(),editTextNewPassword.getText().toString());
					updatePasswordAsynTask.execute();
				}
			}
		});
		alert.setNegativeButton("Cancel", null);
		alert.show();	
	}
 
	private void updateContact() {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Contact Number");
		final EditText editText = new EditText(context);
		editText.setHint("Enter Contact Number");
		if(null != profileDTO.getContact() &&!profileDTO.getContact().equals("null"))
			editText.setText(profileDTO.getContact());
		editText.setInputType(InputType.TYPE_CLASS_PHONE);
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
		editText.setPadding(15, 25, 5, 10);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.addView(editText);
		alert.setView(linearLayout);
		alert.setPositiveButton("Update", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				UpdateContactAsynTask updateContactAsynTask = new UpdateContactAsynTask(context, editText.getText().toString());
				updateContactAsynTask.execute();
			}
		});
		alert.setNegativeButton("Cancel", null);
		alert.show();
	}
	

	private void updateGender() {
		String[] genders ={"Male","Female"};
		
		if(null == profileDTO.getGender())
			index = -1;
		else{
		if(profileDTO.getGender() == 'M')
			index = 0;
		if(profileDTO.getGender() == 'F')
			index = 1;
		}
		AlertDialog.Builder alert = new AlertDialog.Builder((Activity)context);
		alert.setCancelable(true);
		alert.setNegativeButton("Cancel", null);
		alert.setTitle("Gender");
		alert.setSingleChoiceItems(genders,index, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int position) {
				index = position;
				}
			});
		alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(index == -1)
					Toast.makeText(context.getApplicationContext().getApplicationContext(),"Select a Gender", Toast.LENGTH_LONG).show();
				else{
					UpdateGenderAsynTask updateGenderAsynTask = new UpdateGenderAsynTask(context, (index == 0)?'M':'F');
					updateGenderAsynTask.execute();
					}
			}
		});
		alert.setCancelable(false);
		alert.show();
	}

	private void updateAddress() {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Address");
		final EditText editText = new EditText(context);
		editText.setHint("Enter Address");
		if(null != profileDTO.getAddress() && !profileDTO.getAddress().equals("null"))
			editText.setText(profileDTO.getAddress());
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
		editText.setPadding(15, 25, 5, 10);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.addView(editText);
		alert.setView(linearLayout);
		alert.setPositiveButton("Update", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				UpdateAddressAsynTask updateAddressAsynTask = new UpdateAddressAsynTask(context, editText.getText().toString());
				updateAddressAsynTask.execute();
			}
		});
		alert.setNegativeButton("Cancel", null);
		alert.show();
	}
 
	private void dobUpdate(){
	DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			StringBuffer dobBuffer = new StringBuffer();
			dobBuffer.append(year + "-"+ (monthOfYear+1) +"-"+dayOfMonth);
			UpdateDOBAsynTask updateDOBAsynTask = new UpdateDOBAsynTask(context, dobBuffer.toString());
			updateDOBAsynTask.execute();
		}
	};

	final Calendar calendar = Calendar.getInstance();
		DatePickerDialog datePickerDialog = new DatePickerDialog(context, datePickerListener,calendar.get(Calendar.YEAR) ,calendar.get(Calendar.MONTH) , calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.setCancelable(true);
		datePickerDialog.show();
	
	}
	}
	
