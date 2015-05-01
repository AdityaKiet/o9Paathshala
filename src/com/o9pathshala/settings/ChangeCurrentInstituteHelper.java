package com.o9pathshala.settings;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.o9pathshala.profile.dto.SessionDTO;

public class ChangeCurrentInstituteHelper {
	private String jsonString;
	private Context context;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor sharedEditor;
	private SessionDTO sessionDTO;
	private int selectedPosition = -1;
	
	public ChangeCurrentInstituteHelper(Context context,String jsonString) {
		this.jsonString = jsonString;
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		sharedEditor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = sharedPreferences.getString("session", null);
		sessionDTO = gson.fromJson(json, SessionDTO.class);
	}

	public void changeInstitute() throws JSONException {
		final List<Integer> instituteIds = new ArrayList<Integer>();
		final List<String> instituteNames = new ArrayList<String>();
		
		JSONArray jsonArray = new JSONArray(jsonString);
		for(int i = 0;i < jsonArray.length(); i++){
			instituteIds.add(jsonArray.getJSONObject(i).getInt("institute_id"));
			instituteNames.add(jsonArray.getJSONObject(i).getString("institute_name"));
		}
		
		final String[] institueName = instituteNames.toArray(new String[instituteNames.size()]);
		AlertDialog.Builder alert = new AlertDialog.Builder((Activity)context);
		alert.setCancelable(true);
		alert.setPositiveButton("Cancel", null);
		alert.setTitle("Select your Institute");
		LinearLayout ll=new LinearLayout(context);
	    ll.setOrientation(LinearLayout.VERTICAL);
	    alert.setView(ll);
	    alert.setNegativeButton("Select", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(selectedPosition!=-1){
					sessionDTO.setInstituteName(instituteNames.get(selectedPosition));
					sessionDTO.setCurrentInstitutesId(instituteIds.get(selectedPosition));
					sessionDTO.setDefaultInstituteId(true);
					Gson gson = new Gson();
					String json = gson.toJson(sessionDTO);
				    sharedEditor.putString("session", json);
				    sharedEditor.commit();
				    UpdateProfileAsynTask updateProfileAsynTask = new UpdateProfileAsynTask(context);
				    updateProfileAsynTask.execute();
				}else
					Toast.makeText(context, "Select an institute", Toast.LENGTH_SHORT).show();
			}
			
		});
	    
		alert.setSingleChoiceItems(institueName,-1, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialogInterface, int position) {
				selectedPosition = position;
				
		}
		});
		
		alert.show();
	}
}
