package com.hack.sherhackathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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

public class EventsActivity extends ActionBarActivity {

    @
    Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @
    Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

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

    public static class PlaceholderFragment extends Fragment {
        ArrayList < HashMap < String, Object >> categories = new ArrayList < HashMap < String, Object >> ();
        ExtendedSimpleAdapter ext;
        JSONObject js;
        ListView lv;
        public PlaceholderFragment() {}

        @
        Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_events, container,
                false);

            //new showItems()
            return rootView;
        }
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            lv = (ListView) getView().findViewById(R.id.eList);
            InterestPoints ip = new InterestPoints();
            ip.execute();
            Log.e("", " ************************ title size = " + ip.titles.size());



        }

        class InterestPoints extends AsyncTask < String, String, String > {

            ArrayList < double[] > coords = new ArrayList < double[] > ();
            ArrayList < String > names = new ArrayList < String > ();
            ArrayList < String > labs = new ArrayList < String > ();
            ArrayList < String > titles = new ArrayList < String > ();
            ArrayList < String > dates = new ArrayList < String > ();
            ArrayList < String > contacts = new ArrayList < String > ();
            ArrayList < String > locs = new ArrayList < String > ();
            ArrayList < String > descriptions = new ArrayList < String > ();

            protected String doInBackground(String...params) {

                try {
                    js = new JSONObject(readUrl("http://donnees.ville.sherbrooke.qc.ca/storage/f/2014-03-20T19%3A17%3A22.302Z/evenements.json"));
                    JSONObject jobj = js.getJSONObject("EVTS");
                    JSONArray ja = jobj.getJSONArray("EVT");
                    Log.e("", "" + ja.length());
                    for (int i = 0; i < ja.length(); i++) {
                        String categ = ja.getJSONObject(i).getString("CATEG");
                        Log.e("", "category = " + categ);
                        String title = "n/a";
                        String point = "n/a";
                        String adr = "n/a";
                        String date = "n/a";
                        String contact = "n/a";
                        String loc = "n/a";
                        String description = "n/a";
                        if (categ.equals("[\"Événements sportifs\"]")) {
                            title = ja.getJSONObject(i).getString("TITRE");
                            Log.e("", " TITLE = " + title);
                            titles.add(title);
                            try {
                                adr = ja.getJSONObject(i).getString("AD");
                            } catch (JSONException e) {}
                            try {
                                date = ja.getJSONObject(i).getString("DT01");
                            } catch (JSONException e) {}
                            try {
                                loc = ja.getJSONObject(i).getString("LOC");
                            } catch (JSONException e) {}
                            try {
                                contact = ja.getJSONObject(i).getString("CONTACT");
                            } catch (JSONException e) {}
                            try {
                                description = ja.getJSONObject(i).getString("DESCRIP");
                            } catch (JSONException e) {}


                            dates.add(date);
                            locs.add(loc);
                            contacts.add(contact);
                            descriptions.add(description);


                            Log.e("", " AD = " + adr);
                        }

                    }




                } catch (Exception e) {

                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String file_url) {
                for (int i = 0; i < titles.size(); i++) {
                    HashMap < String, Object > map = new HashMap < String, Object > ();
                    map.put("event_name", titles.get(i));
                    map.put("event_descrip", descriptions.get(i));
                    map.put("event_date", dates.get(i));
                    map.put("event_address", locs.get(i));
                    map.put("event_contact", contacts.get(i));
                    categories.add(map);
                }

                ext = new ExtendedSimpleAdapter(getActivity(), categories, R.layout.menusport, new String[] {
                    "event_name", "event_descrip", "event_date", "event_address", "event_contact"
                }, new int[] {
                    R.id.eventeName, R.id.eventDescript, R.id.eventDate, R.id.eventLoc, R.id.eventContact
                });
                lv.setAdapter(ext);


                lv.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView <? > arg0, View arg1, int arg2,
                        long arg3) {

                        Log.e("", titles.get(arg2));

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
    }
}