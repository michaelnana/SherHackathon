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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hack.sherhackathon.HockeyActivity.PlaceholderFragment;
import com.hack.sherhackathon.HockeyActivity.PlaceholderFragment.RinkPoints;

public class GeneralActivity extends ActionBarActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_hockey);

if (savedInstanceState == null) {
getSupportFragmentManager().beginTransaction()
.add(R.id.container, new PlaceholderFragment()).commit();
}
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {

// Inflate the menu; this adds items to the action bar if it is present.
//getMenuInflater().inflate(R.menu.hockey, menu);
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
double myLat;
double myLong;
ProgressDialog pd;
public PlaceholderFragment() {
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState) {
View rootView = inflater.inflate(R.layout.fragment_hockey,
container, false);
new InterestPoints().execute();
return rootView;
}
 
public void onActivityCreated(Bundle savedInstanceState){
super.onActivityCreated(savedInstanceState);
 
/*pd = new ProgressDialog(getActivity());
pd.setTitle("On cherche...");
pd.setMessage("Juste un instant...");
pd.setCancelable(false);
pd.setIndeterminate(true);
pd.show();*/
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
 /*  LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
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
lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);*/
 
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
public void mapStuff(InterestPoints ip,ArrayList<double[]> a){
//Log.d("Length of values", "Pairs: "+pairs.size());
for(int i=0; i<a.size(); i++){
map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
piscine=new LatLng((double)((double[])a.get(i))[0], (double)((double[])a.get(i))[1]);
//currName=(String)names.get(i);
Marker hamburg = null ;
if(ip.labs.get(i).equals("park")){
hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory
               .fromResource(R.drawable.park)));
}/*
else if(ip.labs.get(i).equals("primary"))
hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory
               .fromResource(R.drawable.primary)));
else if(ip.labs.get(i).equals("secondary"))
hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory
               .fromResource(R.drawable.secondary)));
else if(ip.labs.get(i).equals("college"))
hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory
               .fromResource(R.drawable.college)));*/
else if(ip.labs.get(i).equals("university"))
hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory
               .fromResource(R.drawable.university)));
else if(ip.labs.get(i).equals("pool"))
hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory
               .fromResource(R.drawable.pool)));
/*else if(ip.labs.get(i).equals("parking"))
hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory
               .fromResource(R.drawable.parking)));
/*else if(ip.labs.get(i).equals("library"))
hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory
               .fromResource(R.drawable.library)));
else if(ip.labs.get(i).equals("bridge"))
hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory
               .fromResource(R.drawable.bridge)));*/
else if(ip.labs.get(i).equals("arena"))
hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory
               .fromResource(R.drawable.arena)));
/*else hamburg = map.addMarker(new MarkerOptions().position(piscine)
       .title((String)ip.names.get(i))
       .snippet("")
       .icon(BitmapDescriptorFactory.defaultMarker()));*/
 //pd.dismiss();
map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
 
       public void onInfoWindowClick(Marker marker) {
        //handleClick();
        //Log.d("The Current name is",currName);
Intent it = new Intent(getActivity(), InfoHockeyActivity.class);
           Bundle extras=new Bundle();
           extras.putString("Name",marker.getTitle());
           it.putExtras(extras);
           startActivity(it);
 
       }
   });
 
map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

public boolean onMarkerClick(Marker marker) {
 
return false;
}
});
 
}
}
 
class InterestPoints extends AsyncTask<String, String, String> {
 
ArrayList<double[]> coords = new ArrayList<double[]>() ;
ArrayList<String> names = new ArrayList<String>() ;
ArrayList<String> labs = new ArrayList<String>() ;
protected String doInBackground(String... params) {
 
try {
js= new JSONObject(readUrl("http://donnees.ville.sherbrooke.qc.ca/storage/f/2014-03-18T17%3A56%3A56.169Z/sites-interet.json"));
 
JSONArray ja=js.getJSONArray("features") ;
Log.e("",""+ja.length()) ;
for(int i=0; i<ja.length(); i++){
JSONObject coordArr = ja.getJSONObject(i) ;
JSONObject geom = coordArr.getJSONObject("geometry") ;
Log.e("",""+geom) ;
JSONArray latlongArr = geom.getJSONArray("coordinates") ;
double longi = Double.parseDouble(latlongArr.get(0).toString());
double lati = Double.parseDouble(latlongArr.get(1).toString());
Log.e("" , " longi = " + longi + " lati = " + lati) ;
double[] d = {lati,longi} ;
coords.add(d) ;
 
JSONObject properties = coordArr.getJSONObject("properties") ;
Log.e("",""+properties) ;
String name = properties.getString("NOM") ;
Log.e("",""+name) ;
names.add(name) ;
labs.add(parseName(name)) ;
}
 
 
 
 
 
 
 
 
 
 
} catch (Exception e) {

e.printStackTrace();
}
return null;
}
private String parseName(String name){
if(name.contains("primaire")) 
return "primary" ;
if(name.contains("secondaire")) 
return "secondary" ;
if(name.contains("collége") || name.contains("college"))
return "college" ;
if(name.contains("Universit"))
return "university" ;
if(name.contains("Parc"))
return "park" ;
if(name.contains("Stationnement"))
return "parking" ;
if(name.contains("Aréna"))
return "arena" ;
if(name.contains("Pont"))
return "bridge" ;
if(name.contains("Piscine"))
return "pool" ;
if(name.contains("Bibliothèque") || name.contains("bibliothèque"))
return "library" ;
 
return "unkown" ;
 
}
protected void onPostExecute(String file_url) {
mapStuff(this,coords) ;
}
}
 
}
}