package com.o9pathshala.student.slidingmenu.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.o9.R;
import com.o9pathshala.student.slidingmenu.HomeFragmentAdapter;
import com.o9pathshala.student.slidingmenu.nav_logic.Exit;
import com.o9pathshala.student.slidingmenu.nav_logic.MoreApps;
import com.o9pathshala.student.slidingmenu.nav_logic.RateUS;
import com.o9pathshala.student.slidingmenu.nav_logic.Share;
import com.o9pathshala.util.OpenActivity;

public class FragmentHome extends Fragment {
	private ListView listView;
	private LinearLayout linearLayout;
	private String[] texts = { "Tests", "Discussion Fourm","Leader Board", 
			"Settings", "Rate Us", "Share", "About", "More Apps", "Quit" };
	private Integer[] imageId = { R.drawable.book,
			R.drawable.discussion, R.drawable.leader,
			R.drawable.settings, R.drawable.star,
			R.drawable.share_this_icon, R.drawable.info,
			R.drawable.more_apps, R.drawable.exit };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		linearLayout = (LinearLayout) inflater.inflate(R.layout.guest_home,container, false);
		listView = (ListView) linearLayout.findViewById(R.id.listGuestHome);
		HomeFragmentAdapter homeFragmentAdapter = new HomeFragmentAdapter(getActivity(), texts, imageId);
		listView.setAdapter(homeFragmentAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				switch (position) {
				case 0:
					OpenActivity.newActivity(getActivity(),"com.o9pathshala.student.test.tabs.MainActivity");
					break;
				case 1:
					Intent intent = new Intent("com.o9pathshala.discussionfourm.tabs.MainActivity");
					startActivity(intent);
					break;	
				case 2:
					((MainActivity)getActivity()).replaceFragment(new FragmentLeaderboard(), 3);
					break;
				case 3:
					((MainActivity)getActivity()).replaceFragment(new FragmentSettings(), 4);
					break;
				case 4:
					RateUS.rateUs(getActivity());
					break;
				case 5:
					Share.share(getActivity());
					break;
				case 6:
					intent = new Intent("com.o9pathshala.student.WebViewDisplayActivity");
					intent.putExtra("url", "About");
					getActivity().startActivity(intent);
					break;
				case 7:
					MoreApps.moreApps(getActivity());
					break;
				case 8:
					Exit.exit(getActivity());
					break;
				default:
					break;
				}
			}
		});
		return linearLayout;
	}
}
