package com.hack.sherhackathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hack.sherhackathon.InfoHockeyActivity.PlaceholderFragment.addInfo;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class ParkInfoActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_park_info);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.park_info, menu);
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
	public static class PlaceholderFragment extends Fragment {
		String c;
		Bitmap bitm;
		ArrayList categories=new ArrayList();
		ExtendedSimpleAdapter ext;
		JSONObject js;
		JSONObject jse;
		String telphone;
		String contact;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_park_info,
					container, false);
			return rootView;
		}
		
		public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);
			Intent in= getActivity().getIntent();
			Bundle extras=in.getExtras();
			c=extras.getString("Name");
			TextView tx=(TextView)getView().findViewById(R.id.parkInfo);
			tx.setText(c); 
			new addInfo().execute();
			
		}
		

		public void placeImage(){
			ImageView img=(ImageView)getView().findViewById(R.id.parkPic);
            img.setImageBitmap(bitm);
            ListView lv=(ListView)getView().findViewById(R.id.parkStuff);
            HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("event_name",contact);
			categories.add(map);
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("event_name",telphone);
			categories.add(map1);
			ext= new ExtendedSimpleAdapter(getActivity(), categories, R.layout.infoitem, new String[] { "event_name"},
	                new int[] { R.id.eventName});
			 lv.setAdapter(ext);
		}
		
		private static String readUrl(String urlString) throws Exception {
		    BufferedReader reader = null;
		    try {
		        URL url = new URL(urlString);
		        reader = new BufferedReader(new InputStreamReader(url.openStream()));
		        StringBuffer buffer = new StringBuffer();
		        int read;
		        char[] chars = new char[1024];
		        while ((read = reader.read(chars)) != -1)
		            buffer.append(chars, 0, read); 

		        return buffer.toString();
		    } finally {
		        if (reader != null)
		            reader.close();
		    }
		}
		
		public static Bitmap getBitmapFromURL(String src) {
	        try {
	            //Log.e("src",src);
	            URL url = new URL(src);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoInput(true);
	            connection.connect();
	            InputStream input = connection.getInputStream();
	            Bitmap myBitmap = BitmapFactory.decodeStream(input);
	            //Log.e("Bitmap","returned");
	            return myBitmap;
	        } catch (IOException e) {
	            e.printStackTrace();
	            //Log.e("Exception",e.getMessage());
	            return null;
	        }
	    }
		class addInfo extends AsyncTask<String, String, String>{

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
					bitm=  getBitmapFromURL("http://p7.storage.canalblog.com/79/85/303679/90341491_p.jpg");
					try {
						js= new JSONObject(readUrl("http://donnees.ville.sherbrooke.qc.ca/storage/f/2014-03-18T14%3A48%3A15.227Z/structures-recreatives.json"));
						JSONObject jss=js.getJSONObject("STRUCTURES_RECREATIVES");
						JSONArray ja=jss.getJSONArray("STRUCTURE_RECREATIVE");
						for(int i=0; i<ja.length(); i++){
						JSONObject jo=ja.getJSONObject(i);
						
						
						//String t=jo.getString("GEOM");
						if(c.equals(jo.getString("LOC"))){
							telphone=jo.getString("TEL1");
							contact=jo.getString("CONTACT");
						}
						
						}
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				return null;
			}
			protected void onPostExecute(String file_url) {
				placeImage();


		    }
			
		}
	}

}
