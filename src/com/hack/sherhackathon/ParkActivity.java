package com.hack.sherhackathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class ParkActivity extends ActionBarActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_park);

if (savedInstanceState == null) {
getSupportFragmentManager().beginTransaction()
.add(R.id.container, new PlaceholderFragment()).commit();
}
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {

// Inflate the menu; this adds items to the action bar if it is present.
//getMenuInflater().inflate(R.menu.park, menu);
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
JSONObject js;
JSONObject jse;
LatLng POS;
LatLng piscine;
private GoogleMap map;
private GoogleMap mapper;
ArrayList<String> names=new ArrayList<String>();
ArrayList<String> finalNames = new ArrayList<String>() ;
ArrayList<String> finalInfos = new ArrayList<String>() ;
double myLat;
double myLong;
ProgressDialog pd;
public PlaceholderFragment() {
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState) {
View rootView = inflater.inflate(R.layout.fragment_map, container,
false);
pd = new ProgressDialog(getActivity());
pd.setTitle("On cherche...");
pd.setMessage("Juste un instant...");
pd.setCancelable(false);
pd.setIndeterminate(true);
pd.show();
new ParkPoints().execute();
return rootView;
}
 
public void onActivityCreated(Bundle savedInstanceState){
super.onActivityCreated(savedInstanceState);
 
mapper = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
.getMap();
POS=new LatLng(45.4,-71.9);
Marker here = mapper.addMarker(new MarkerOptions().position(POS)
.title("You are here")
.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
// Move the camera instantly to your location with a zoom of 25.
mapper.moveCamera(CameraUpdateFactory.newLatLngZoom(POS, 12));

// Zoom in, animating the camera.
mapper.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
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
 
public void mapStuff(ArrayList<double[]> a){
//Log.d("Length of values", "Pairs: "+pairs.size());
for(int i=0; i<a.size(); i++){
map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
piscine=new LatLng((double)((double[])a.get(i))[0], (double)((double[])a.get(i))[1]);
//currName=(String)names.get(i);
Marker hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)finalNames.get(i)));
hamburg.setSnippet(finalInfos.get(i));
pd.dismiss();
map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
 
       public void onInfoWindowClick(Marker marker) {
        //handleClick();
        //Log.d("The Current name is",currName);
Intent it = new Intent(getActivity(), ParkInfoActivity.class);
           Bundle extras=new Bundle();
           extras.putString("Name",marker.getTitle());
           it.putExtras(extras);
           startActivity(it);
           //startActivity(new Intent(getActivity(), InfoParkActivity.class));
 
       }
   });

  
}
}
 
class ParkPoints extends AsyncTask<String, String, String> {
 
//Geocoder coder = new Geocoder(this);
//List<Address> address;
double longitude ;
double latitude ;
ArrayList<double[]> coords = new ArrayList<double[]>() ;
ArrayList<String>addresses = new ArrayList<String>() ;
ArrayList<String>infos = new ArrayList<String>() ;
 
 
protected String doInBackground(String... params) {
 
try {
js = new JSONObject(readUrl("http://donnees.ville.sherbrooke.qc.ca/storage/f/2014-03-18T14%3A48%3A15.227Z/structures-recreatives.json"));
JSONObject jObj = js.getJSONObject("STRUCTURES_RECREATIVES") ;
JSONArray jArr = jObj.getJSONArray("STRUCTURE_RECREATIVE") ;
/*
for(int i=0;i<jArr.length();i++){
JSONObject singlePark = jArr.getJSONObject(i) ;
String adr = singlePark.getString("AD") ;
if(getLatLong(getLocationInfo(adr))!= false){
Log.e("","i = " + Integer.toString(i)) ;
names.add(singlePark.getString("LOC"));
Log.e(""," address = " + adr) ;
addresses.add(adr) ;
double[] lat_long = {latitude,longitude} ;
coords.add(lat_long) ;
}
else Log.e("","BAD ADDRESS = " + adr) ;
}*/
 
 
for(int i=0;i<jArr.length();i++){
//Log.e("","i = " + Integer.toString(i)) ;
JSONObject singlePark = jArr.getJSONObject(i) ;
String adr = singlePark.getString("AD") ;
String name = singlePark.getString("LOC") ;
String info = singlePark.getString("TYPE_SR") ;
names.add(name) ;
addresses.add(adr) ;
infos.add(info) ;
//Log.e(""," address = " + adr) ;
}
 
} catch (Exception e) {
e.printStackTrace();
}
for(int i=0;i<addresses.size();i++){
String address = addresses.get(i) ;
if(getLatLong(getLocationInfo(address))){ ;
double[] lat_long = {latitude,longitude} ;
coords.add(lat_long) ;
finalNames.add(names.get(i)) ;
finalInfos.add(infos.get(i)) ;
}
}
return null;
}
 
 
public JSONObject getLocationInfo(String address) {
      StringBuilder stringBuilder = new StringBuilder();
      try {

      address = address.replaceAll(" ","%20");    

      HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
      HttpClient client = new DefaultHttpClient();
      HttpResponse response;
      stringBuilder = new StringBuilder();


          response = client.execute(httppost);
          HttpEntity entity = response.getEntity();
          InputStream stream = entity.getContent();
         // Log.e("","response = " + response + " entity = " + entity + " stream = "+ stream) ;
          int b;
          while ((b = stream.read()) != -1) {
              stringBuilder.append((char) b);
          }
      } catch (ClientProtocolException e) {
    //   e.printStackTrace();
      } catch (IOException e) {
    //  e.printStackTrace();
      }

      JSONObject jsonObject = new JSONObject();
      try {
          jsonObject = new JSONObject(stringBuilder.toString());
      } catch (JSONException e) {
          // TODO Auto-generated catch block
         // e.printStackTrace();
      }

      return jsonObject;
  }
 
public boolean getLatLong(JSONObject jsonObject) {

      try {

          longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
              .getJSONObject("geometry").getJSONObject("location")
              .getDouble("lng");

          latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
              .getJSONObject("geometry").getJSONObject("location")
              .getDouble("lat");

      } catch (JSONException e) {
     // e.printStackTrace();
          return false;

      }

      return true;
  }
protected void onPostExecute(String file_url) {
mapStuff(coords);
  }  
}
 
 
}

}