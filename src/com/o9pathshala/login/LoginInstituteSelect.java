package com.o9pathshala.login;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.profile.dto.ProfileDTO;
import com.o9pathshala.profile.dto.SessionDTO;

public class LoginInstituteSelect {
	private String result;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor sharedEditor;
	private List<String> institueName = new ArrayList<String>();
	private List<Integer> institueIds = new ArrayList<Integer>();
	private Context context;
	private JSONObject jsonObject;
	private ProfileDTO profileDTO;
	private CheckBox chkRemember;
	private SessionDTO sessionDTO;
	private List<SessionDTO> sessionDTOs;
	private Integer current_institute;
	boolean defaultInstituteFound = false;
	
	public LoginInstituteSelect(Context context,String result) {
		this.result = result;
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)this.context).getBaseContext());
		sharedEditor = sharedPreferences.edit();
	}

	public void createProfile() throws JSONException {
		
			JSONArray jsonArray = new JSONArray(result);
			sessionDTOs = new ArrayList<SessionDTO>();
			sessionDTO = new SessionDTO();
			profileDTO = new ProfileDTO();
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = null;
				jsonObject = jsonArray.getJSONObject(i);
				sessionDTO = new SessionDTO();
				sessionDTO.setId(jsonObject.getInt("id"));
				if(!jsonObject.getString("id").startsWith("1"))
					sessionDTO.setType("Student");
				else
					sessionDTO.setType("Faculty");
				sessionDTO.setCurrentInstitutesId(jsonObject.getInt("institute_id"));
				sessionDTO.setInstituteName(jsonObject.getString("institute_name"));
				sessionDTO.setName(jsonObject.getString("name"));
				sessionDTO.setEmail(jsonObject.getString("email"));
				if(jsonObject.getInt("default_institute_id") == 0)
					sessionDTO.setDefaultInstituteId(false);
				else
					sessionDTO.setDefaultInstituteId(true);
				sessionDTOs.add(sessionDTO);
				institueName.add(jsonObject.getString("institute_name"));
				institueIds.add(jsonObject.getInt("institute_id"));
			}
			if(sessionDTOs.size() == 1){
				sessionDTO = sessionDTOs.get(0);
				setProfileDTO();
				Gson gson = new Gson();
				String json = gson.toJson(sessionDTO);
			    sharedEditor.putString("session", json);
			    sharedEditor.putBoolean("isLogin", true);
			    json = gson.toJson(profileDTO);
			    sharedEditor.putString("profileDTO", json);
			    sharedEditor.commit();
			    new SetProfileAsynTask(context).execute();
			}else{
					for(SessionDTO data : sessionDTOs){
						if(data.getDefaultInstituteId()){
						sessionDTO = new SessionDTO();
						sessionDTO = data;
						sessionDTO.setType("Student");
						defaultInstituteFound = true;
						break;
						}
					}
					if(defaultInstituteFound){
						setProfileDTO();
						Gson gson = new Gson();
						String json = gson.toJson(sessionDTO);
					    sharedEditor.putString("session", json);
					    sharedEditor.putBoolean("isLogin", true);
					    json = gson.toJson(profileDTO);
					    sharedEditor.putString("profileDTO", json);
					    sharedEditor.commit();
					    new SetProfileAsynTask(context).execute();
					}else{
						setProfileDTO();
						selectDefaultInstitue();
					}
				}
				
				
		}

	private void selectDefaultInstitue(){
		
		final String[] institueNames = institueName.toArray(new String[institueName.size()]);
		((Activity)context).setContentView(R.layout.blank);
		chkRemember = new CheckBox(context);
		chkRemember.setText("Set Default");
		chkRemember.setTextSize(22);
		chkRemember.setPadding(5, 15, 0, 0);
		chkRemember.setGravity(Gravity.CENTER_VERTICAL);
		AlertDialog.Builder alert = new AlertDialog.Builder((Activity)context);
		alert.setCancelable(false);
		alert.setTitle("Select your Institute");
		LinearLayout ll=new LinearLayout(context);
	    ll.setOrientation(LinearLayout.VERTICAL);
	    ll.addView(chkRemember);
	    alert.setView(ll);
	    alert.setNegativeButton("Select", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			if(null == current_institute)
				selectDefaultInstitue();
			else{
				if(chkRemember.isChecked()){
					SetDefaultInstituteAsynTask defaultInstituteAsynTask = new SetDefaultInstituteAsynTask(context, sessionDTO,profileDTO);
					defaultInstituteAsynTask.execute();
				}else{
					Gson gson = new Gson();
					String json = gson.toJson(sessionDTO);
				    sharedEditor.putString("session", json);
				    json = gson.toJson(profileDTO);
				    sharedEditor.putString("profileDTO", json);
				    sharedEditor.putBoolean("isLogin", true);
				    sharedEditor.commit();
				    new SetProfileAsynTask(context).execute();
				}
				
			}
			}
		});
		alert.setSingleChoiceItems(institueNames,-1, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialogInterface, int position) {
			current_institute = institueIds.get(position);	
			sessionDTO = sessionDTOs.get(position);
			}
		});
		
		alert.show();
		
	}
	private void setProfileDTO(){
		profileDTO.setAddress(null);
		profileDTO.setBatch_id(null);
		profileDTO.setContact(null);
		profileDTO.setDob(null);
		profileDTO.setEmail(null);
		profileDTO.setGender(null);
		profileDTO.setId(null);
		profileDTO.setName(null);
		profileDTO.setProfilepic(null);
	}

}