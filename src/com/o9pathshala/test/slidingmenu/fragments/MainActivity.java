package com.o9pathshala.test.slidingmenu.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.global.GlobalData;
import com.o9pathshala.student.test.dto.AdapterDTO;
import com.o9pathshala.student.test.dto.SectionDTO;
import com.o9pathshala.student.test.dto.TestDTO;
import com.o9pathshala.test.slidingmenu.adapter.Adapter;
import com.o9pathshala.test.util.Score;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	private TestDTO testDTO;
	private DrawerLayout drawerLayout;
	private ListView listView;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private ArrayList<AdapterDTO> adapterDTOs;
	private Adapter adapter;
	private List<SectionDTO> sections;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guest_activity_main);
		testDTO = GlobalData.testDTO;
		sections = testDTO.getSections();
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		listView = (ListView) findViewById(R.id.list_slidermenu);
		adapterDTOs = new ArrayList<AdapterDTO>();
		for (SectionDTO sectionDTO : sections) {
			adapterDTOs = new ArrayList<AdapterDTO>();
			adapterDTOs.add(new AdapterDTO("Sec : " + sectionDTO.getSectionName(),true));
			adapterDTOs.add(new AdapterDTO("Total Questions :"+ sectionDTO.getQuestions().size(),false));
			adapterDTOs.add(new AdapterDTO("Attempted Questions : 0 ",false));
			adapterDTOs.add(new AdapterDTO("Flag Questions : 0 ",false));
		}
		adapter = new Adapter(getApplicationContext(), adapterDTOs);
		listView.setAdapter(adapter);

		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				adapterDTOs = new ArrayList<AdapterDTO>();
				ArrayList<com.o9pathshala.test.ui.dto.AdapterDTO> adapters = TestFragment.getAdapters();
				int index = 0;
				int flags = 0;
				int attempted = 0;
				for(com.o9pathshala.test.ui.dto.AdapterDTO adapter : adapters){
						if(adapter.getAttempted()!=null && adapter.getAttempted())
							attempted++;
						if(adapter.getIsFlag()!= null  && adapter.getIsFlag())
							flags++;
						if(adapter.getIsHeading() && adapters.indexOf(adapter)!=0){
							
							adapterDTOs.add(new AdapterDTO("Attempted Questions : " + attempted,false));
							adapterDTOs.add(new AdapterDTO("Flag Questions : " + flags,false));
						}
						if(adapter.getIsHeading()){
							adapterDTOs.add(new AdapterDTO("Sec : " + sections.get(index).getSectionName(),true));
							adapterDTOs.add(new AdapterDTO("Total Questions :"+ sections.get(index).getQuestions().size(),false));
							index++;
							attempted = 0;
							flags = 0;
							}
						
				}
				index--;
				adapterDTOs.add(new AdapterDTO("Attempted Questions : " + attempted,false));
				adapterDTOs.add(new AdapterDTO("Flag Questions : " + flags,false));
				

				adapter = new Adapter(getApplicationContext(), adapterDTOs);
				listView.setAdapter(adapter);

				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(actionBarDrawerToggle);

		if (savedInstanceState == null) {
			Fragment fragment = new TestFragment();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.guest_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setMessage("Are you sure you want to exit this test ?");
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Score score = new Score(sections, testDTO.getPositiveMark(), testDTO.getNegativeMark());
				testDTO.setSections(sections);
				Gson gson = new Gson();
				String json = gson.toJson(testDTO);
				Intent intent = new Intent(MainActivity.this,com.o9pathshala.test.result.tabs.MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putFloat("score", score.score());
				bundle.putString("testDTO", json);
				intent.putExtras(bundle);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});
		alert.setNegativeButton("Cancel", null);
		alert.show();
		
	}

	
}
