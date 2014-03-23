package com.hack.sherhackathon;

import java.util.ArrayList;
import java.util.HashMap;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.os.Build;
import android.widget.AdapterView.OnItemClickListener;

public class FrontActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.front, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends ListFragment {
		ArrayList<HashMap<String, Object>> categories=new ArrayList<HashMap<String, Object>>();
		ExtendedSimpleAdapter ext;
		int count=0;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			
			//new showItems()
			return rootView;
		}
		public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);
			if(count==0){ 
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("event_name","Patins libres");
			categories.add(map);
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("event_name","Bains libres");
			categories.add(map1);
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("event_name","Parcs");
			categories.add(map2);
			HashMap<String, Object> map3 = new HashMap<String, Object>();
			map3.put("event_name","Pistes Cyclables");
			categories.add(map3);
			ext= new ExtendedSimpleAdapter(getActivity(), categories, R.layout.sportsitem, new String[] { "event_name"},
	                new int[] { R.id.eventName});
			
			setListAdapter(ext);
			count++;
			}
			 ListView l=getListView();
				l.setOnItemClickListener(new OnItemClickListener(){
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3){
						if(arg2==1){
							Intent i= new Intent(getActivity(), MapActivity.class);
							 startActivity(i);
						}
						if(arg2==0){
							Intent i= new Intent(getActivity(), HockeyActivity.class);
							 startActivity(i);
						}
						if(arg2==2){
							Intent i= new Intent(getActivity(), ParkActivity.class);
							 startActivity(i);
						}
						if(arg2==3){
							Intent i= new Intent(getActivity(), BikeActivity.class);
							 startActivity(i);
						}
					}
					
				});
			 
			
		}
	}
}
