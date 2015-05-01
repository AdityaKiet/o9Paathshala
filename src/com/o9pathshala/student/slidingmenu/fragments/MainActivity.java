package com.o9pathshala.student.slidingmenu.fragments;

import java.sql.Timestamp;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.o9.R;
import com.o9pathshala.global.GlobalData;
import com.o9pathshala.logout.Logout;
import com.o9pathshala.network.LoadImageAsynTask;
import com.o9pathshala.student.slidingmenu.Adapter;
import com.o9pathshala.student.slidingmenu.nav_logic.BackPressed;
import com.o9pathshala.student.slidingmenu.nav_logic.Exit;
import com.o9pathshala.student.slidingmenu.nav_logic.MoreApps;
import com.o9pathshala.student.slidingmenu.nav_logic.OpenURL;
import com.o9pathshala.student.slidingmenu.nav_logic.RateUS;
import com.o9pathshala.student.slidingmenu.nav_logic.Share;
import com.o9pathshala.test.database.SetResultAsynTask;
import com.o9pathshala.test.database.SqlConstants;
import com.o9pathshala.test.result.tabs.ResultDTO;
import com.o9pathshala.ui.dto.AdapterDTO;
import com.o9pathshala.util.NetworkCheck;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements SqlConstants{
	private DrawerLayout drawerLayout;
	private ListView listView;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<AdapterDTO> adapterDTOs;
	private Adapter adapter;
	private Fragment lastFragment;
	private SharedPreferences sharedPreferences;
	Fragment fragment = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guest_activity_main);
		/*if(NetworkCheck.isNetworkAvailable(MainActivity.this)){
			LoadImageAsynTask loadImageAsynTask = new LoadImageAsynTask(MainActivity.this,GET_PROFILE_PIC);
			loadImageAsynTask.execute();
		} */
		if(null != GlobalData.last_sync){
			java.util.Date date= new java.util.Date();
			GlobalData.last_sync = new Timestamp(date.getTime());
		}
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if(NetworkCheck.isNetworkAvailable(MainActivity.this)){
			if(null != sharedPreferences.getString("resultDTO", null)){
				Gson gson = new Gson();
				ResultDTO resultDTO = gson.fromJson(sharedPreferences.getString("resultDTO", null), ResultDTO.class);
				Log.d("log", resultDTO.toString());
				String query = SET_RESULT;
				query = query.replaceAll("INSTITUTE_ID", String.valueOf(resultDTO.getInstitute_id()));
				query = query.replaceAll("TEST_ID", String.valueOf(resultDTO.getTest_id()));
				query = query.replaceAll("TEST_NAME", resultDTO.getTest_name());
				query = query.replaceAll("SCORE", String.valueOf(resultDTO.getScore()));
				query = query.replaceAll("BATCH_ID", String.valueOf(resultDTO.getBatch_id()));
				query = query.replaceAll("STUDENT_ID", String.valueOf(resultDTO.getStudent_id()));
				SetResultAsynTask setResultAsynTask = new SetResultAsynTask(query, MainActivity.this);
				setResultAsynTask.execute();
			}
		}
		mTitle = mDrawerTitle = getTitle();
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		listView = (ListView) findViewById(R.id.list_slidermenu);

		adapterDTOs = new ArrayList<AdapterDTO>();
		adapterDTOs.add(new AdapterDTO(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));
		adapterDTOs.add(new AdapterDTO(navMenuTitles[11], navMenuIcons.getResourceId(11, -1)));
		navMenuIcons.recycle();
		listView.setOnItemClickListener(new SlideMenuClickListener());
		adapter = new Adapter(getApplicationContext(), adapterDTOs);
		listView.setAdapter(adapter);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(actionBarDrawerToggle);

		if (savedInstanceState == null) {
			displayView(1);
		}
	}

	private class SlideMenuClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			displayView(position);
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
		case R.id.action_share :
			Share.share(MainActivity.this);
			break;
		case R.id.action_rate:
			RateUS.rateUs(MainActivity.this);
			break;
		default:
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	private void displayView(int position) {
		
		switch (position) {
		case 1:
			fragment = new FragmentHome();
			break;
		case 2:
			Intent intent = new Intent("com.o9pathshala.discussionfourm.tabs.MainActivity");
			startActivity(intent);
			break;
		case 3:
			fragment = new FragmentLeaderboard();
			break;
		case 4:
			fragment = new FragmentSettings();
			break;
		case 5:
			Logout logout = new Logout(MainActivity.this);
			logout.logout();
			break;
		case 7:
			RateUS.rateUs(MainActivity.this);
			break;
		case 8:
			Share.share(MainActivity.this);
			break;
		case 9:
			OpenURL.openURL(MainActivity.this, "https://www.google.com");
			break;
		case 10:
			MoreApps.moreApps(MainActivity.this);
			break;
		case 11:
			Exit.exit(MainActivity.this);
			break;
		default:
			break;
		}
		lastFragment = fragment;
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			listView.setItemChecked(position, true);
			listView.setSelection(position);
			setTitle(navMenuTitles[position]);
			drawerLayout.closeDrawer(listView);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
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
		if (!(lastFragment instanceof FragmentHome)) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, new FragmentHome()).commit();

			listView.setItemChecked(1, true);
			listView.setSelection(1);
			setTitle(navMenuTitles[1]);
			lastFragment = new FragmentHome();
		} else {
			BackPressed backPressed = new BackPressed(MainActivity.this);
			backPressed.backPressed();
		}

	}
	public void replaceFragment(Fragment fragment, int position){
		lastFragment = fragment;
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			setTitle(navMenuTitles[position]);
			drawerLayout.closeDrawer(listView);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
}
