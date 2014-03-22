package com.hack.sherhackathon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.*;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.content.Intent;

public class MapActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		//LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
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
		static final LatLng HAMBURG = new LatLng(53.558, 9.927);
		  static final LatLng KIEL = new LatLng(53.551, 9.993);
		  LatLng POS;
		  LatLng piscine;
		  private GoogleMap map;
		  private GoogleMap mapper;
		  JSONObject js;
		  JSONObject jse;
		  TextView tx;
		  double lat;
		  double longi;
		  ArrayList pairs=new ArrayList();
		  ArrayList names=new ArrayList();
		  double myLat;
		  double myLong;
		 String currName;
		  
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_map, container,
					false);
			return rootView;
		}
		
		public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);
			//tx=(TextView)getView().findViewById(R.id.testText);
			//map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			new SportPoints().execute();
			//piscine=new LatLng(myLat, myLong);
			mapper = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
			        .getMap();
			    /*Marker hamburg = mapper.addMarker(new MarkerOptions().position(POS)
			        .title("You are here"));*/
			    LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			    LocationListener locationListener = new LocationListener() {
				    public void onLocationChanged(Location location) {
				        myLong = location.getLongitude();
				        myLat = location.getLatitude();
				        POS=new LatLng(myLat, myLong);
						Marker here = mapper.addMarker(new MarkerOptions().position(POS)
						        .title("You are here"));
						 // Move the camera instantly to your location with a zoom of 25.
					    mapper.moveCamera(CameraUpdateFactory.newLatLngZoom(POS, 25));
					
					    // Zoom in, animating the camera.
					    mapper.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
					   
				        
				    }

					@Override
					public void onProviderDisabled(String arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {
						// TODO Auto-generated method stub
						
					}
				};
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
			
		}
		
		public void mapStuff(){
			Log.d("Length of values", "Pairs: "+pairs.size());
			for(int i=0; i<pairs.size(); i++){
				map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			piscine=new LatLng((double)((ArrayList)pairs.get(i)).get(0), (double)((ArrayList)pairs.get(i)).get(1));
			currName=(String)names.get(i);
			Marker hamburg = map.addMarker(new MarkerOptions().position(piscine)
	        .title((String)names.get(i)));
			 map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			        public void onInfoWindowClick(Marker marker) {
			        	//handleClick();
			        	Log.d("The Current name is",currName);
						Intent it = new Intent(getActivity(), InfoActivity.class);
			            Bundle extras=new Bundle();
			            extras.putString("Name",marker.getTitle());
			            it.putExtras(extras);
			            startActivity(it);

			        }
			    });
	
	   
			}
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
		
		class SportPoints  extends AsyncTask<String, String, String> {
			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				try {
					js= new JSONObject(readUrl("http://donnees.ville.sherbrooke.qc.ca/storage/f/2014-03-21T19%3A42%3A17.534Z/bain-libre-interieur.json"));
					JSONObject jss=js.getJSONObject("EVTS");
					JSONArray ja=jss.getJSONArray("EVT");
					for(int i=0; i<ja.length(); i++){
					JSONObject jo=ja.getJSONObject(i);
					
					
					String t=jo.getString("GEOM");
					names.add(jo.getString("LOC"));
					String s=t.substring(7,t.length()-1);
					
					String[] arr=s.split(" ");
					lat=Double.parseDouble(arr[0]);
					longi=Double.parseDouble(arr[1]);
					ArrayList values=new ArrayList();
					values.add(lat);
					values.add(longi);
					pairs.add(values);
					//tx.setText("Long: "+(lat+10));
					
					}
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
			protected void onPostExecute(String file_url) {
				
					mapStuff();

		    }
	}
	}

}
