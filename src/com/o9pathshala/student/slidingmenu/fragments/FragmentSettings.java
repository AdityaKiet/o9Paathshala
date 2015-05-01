package com.o9pathshala.student.slidingmenu.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.profile.dto.SessionDTO;
import com.o9pathshala.settings.AccountInfoAsynTask;
import com.o9pathshala.settings.GetAllInstitueAsynTask;
import com.o9pathshala.settings.ListAdapter;
import com.o9pathshala.util.NetworkCheck;

public class FragmentSettings extends Fragment {
	private LinearLayout linearLayout;
	private ListView listView;
	private SessionDTO sessionDTO;
	private SharedPreferences sharedPreferences;
	private ListAdapter adapter;
	private String[] texts = { "Your Account", "Privacy Policy",
			"Help & About Us", "Change Current Institute ", "Share Your Profile" };
	private Integer[] imageId = { R.drawable.settings_profile,R.drawable.settings_privacy,
			 R.drawable.settings_help,R.drawable.settings_contacts,
			R.drawable.settings_account };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		linearLayout = (LinearLayout) inflater.inflate(R.layout.my_account,container, false);
		populate();
		return linearLayout;
	}

	private void populate() {
		
		listView = (ListView) linearLayout.findViewById(R.id.listSettings);
		adapter = new ListAdapter(getActivity(), texts, imageId);
		listView.setAdapter(adapter);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		Gson gson = new Gson();
		sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				openNewActivity(position);
			}
		});

		
	}

	private void openNewActivity(int position) {
		switch(position){
		case 0:
			if(NetworkCheck.isNetworkAvailable(getActivity())){
				AccountInfoAsynTask accountInfoAsynTask = new AccountInfoAsynTask(getActivity());
				accountInfoAsynTask.execute();
			}else{
				Intent intent = new Intent("com.o9pathshala.settings.profile.UpdateAccountActivity");
				startActivity(intent);
			}
		break;
		case 1:
			Intent intent = new Intent("com.o9pathshala.student.WebViewDisplayActivity");
			intent.putExtra("url", "Privacy");
			getActivity().startActivity(intent);
			break;

		case 2:
			Intent intent2 = new Intent("com.o9pathshala.student.WebViewDisplayActivity");
			intent2.putExtra("url", "About");
			getActivity().startActivity(intent2);
			break;
		case 3:
			GetAllInstitueAsynTask getAllInstitueAsynTask = new GetAllInstitueAsynTask(getActivity());
			getAllInstitueAsynTask.execute();
			break;
		case 4:
			String string = "Hi ! I am " + sessionDTO.getName() +" from " + sessionDTO.getInstituteName() + ". Download o9Paathshala App from http://goo.gl/KurEeX.";
			Intent intent1 = new Intent(android.content.Intent.ACTION_SEND);
			intent1.setType("text/plain");
			intent1.putExtra(android.content.Intent.EXTRA_TEXT, string);
			startActivity(Intent.createChooser(intent1, "Share with"));
			break;
		default:
			break;
		}
	}

	
}
