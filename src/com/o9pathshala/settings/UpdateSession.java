package com.o9pathshala.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ListView;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.profile.dto.ProfileDTO;
import com.o9pathshala.settings.profile.UpdateAccountListAdapter;

public class UpdateSession {
	public static void update(Context context,ProfileDTO profileDTO, SessionDTO sessionDTO) {
		SharedPreferences sharedPreferences;
		SharedPreferences.Editor sharedEditor;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(((Activity)context).getBaseContext());
		sharedEditor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(profileDTO);
	    sharedEditor.putString("profileDTO", json);
	    json = gson.toJson(sessionDTO);
	    sharedEditor.putString("session", json);
	    sharedEditor.commit();
	    ListView listView = (ListView) ((Activity)context).findViewById(R.id.listAccountUpdate);
	    String[] headings = {"Name","Password","Gender","Contact","Batch ID","Date of Birth","E-mail","Address"};
	    String[] values ={String.valueOf(profileDTO.getName()),"",String.valueOf(profileDTO.getGender()),profileDTO.getContact(),
				String.valueOf(profileDTO.getBatch_id()),profileDTO.getDob(),profileDTO.getEmail(), profileDTO.getAddress()};
		UpdateAccountListAdapter adapter = new UpdateAccountListAdapter(((Activity)context),headings,values);
		listView.setAdapter(adapter);
	}
}
