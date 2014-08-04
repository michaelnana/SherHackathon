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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;


/**
 * This is an another activity from an event app I built for an hackathon we had in Sherbrooke.
 * This is a fragment that displays a google map and then using jsonobject makes a json request
 * from the Sherbrooke city data bank and on the response, I drew out all the bike trails in the
 * city of Sherbrooke.
 */
public class BikeActivity extends ActionBarActivity {

    @
    Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @
    Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.bike, menu);
        return true;
    }

    @
    Override
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
        private GoogleMap mapper;
        double myLat;
        double myLong;
        ProgressDialog pd;

        public PlaceholderFragment() {}

        @
        Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bike, container,
                false);

            return rootView;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            pd = new ProgressDialog(getActivity());
            pd.setTitle("On cherche...");
            pd.setMessage("Juste un instant...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
            mapper = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
                
            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    myLong = location.getLongitude();
                    myLat = location.getLatitude();
                    POS = new LatLng(myLat, myLong);
                    Marker here = mapper.addMarker(new MarkerOptions().position(POS)
                        .title("You are here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    // Move the camera instantly to your location with a zoom of 25.
                    mapper.moveCamera(CameraUpdateFactory.newLatLngZoom(POS, 10));

                    // Zoom in, animating the camera.
                    mapper.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


                }

                @
                Override
                public void onProviderDisabled(String arg0) {
                    // TODO Auto-generated method stub

                }

                @
                Override
                public void onProviderEnabled(String provider) {
                    // TODO Auto-generated method stub

                }

                @
                Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // TODO Auto-generated method stub

                }
            };
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
            new BikePoints().execute();
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

        public void mapBikePath(BikePoints bp, ArrayList < ArrayList < LatLng >> paths) {

            for (int i = 0; i < paths.size(); i++) {
                Polyline line = mapper.addPolyline(new PolylineOptions()
                    .add()
                    .width(5)
                    .color(Color.RED));
                line.setPoints(bp.latlngs.get(i));
                line.setVisible(true);
            }
            pd.dismiss();
        }

        //This class fetches the json reponse from the Sherbrooke city bank and then parse out the response to get the name,
        //latitude, longitude, availability of the rink.
        class BikePoints extends AsyncTask < String, String, String > {

            double longitude;
            double latitude;
            ArrayList <double[]> coords = new ArrayList < double[] > ();
            ArrayList <ArrayList <LatLng >> latlngs = new ArrayList < ArrayList < LatLng >> ();

            protected String doInBackground(String...params) {
                try {
                    js = new JSONObject(readUrl("http://donnees.ville.sherbrooke.qc.ca/storage/f/2014-02-19T19%3A45%3A39.142Z/pistecyclable.json"));
                    JSONArray jArr = js.getJSONArray("features");
                    for (int i = 0; i < jArr.length(); i++) {
                        latlngs.add(new ArrayList < LatLng > ());
                        JSONObject singleEvent = jArr.getJSONObject(i);
                        JSONObject geom = singleEvent.getJSONObject("geometry");
                        JSONArray bikeCoordinates = geom.getJSONArray("coordinates");
                        for (int j = 0; j < bikeCoordinates.length(); j++) {
                            String coordj = bikeCoordinates.getString(j);
                            String[] lati_longi = coordj.split(",");
                            double longi = Double.parseDouble(lati_longi[0].substring(1, lati_longi[0].length() - 1));
                            double lati = Double.parseDouble(lati_longi[1].substring(0, lati_longi[1].length() - 2));
                            latlngs.get(i).add(new LatLng(lati, longi));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String file_url) {
                mapBikePath(this, latlngs);
            }
        }
    }

}