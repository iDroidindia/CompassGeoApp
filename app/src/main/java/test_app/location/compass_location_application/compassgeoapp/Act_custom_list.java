package test_app.location.compass_location_application.compassgeoapp;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

/**
 * Created by babu on 11/19/2016.
 */
public class Act_custom_list extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, AdapterView.OnItemClickListener {

    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    String Usr_Latitude = "", Usr_Lognitude = "";
    private LocationRequest mLocationRequest;
    Connection_Detection CD;
    String F_distance, Loc_Distance, NN_distance;
    boolean p_status;

    String Loc_ID, Loc_Lat, Loc_Lng, Loc_Name, Loc_Desc, Loc_Distance_from_geo_point, Loc_Img_URL;
    ProgressDialog PD;
    private JSONObject JO;
    private JSONArray JA;
    ArrayList<String> Arr_Loc_ID;
    ArrayList<String> Arr_Loc_Lat;
    ArrayList<String> Arr_Loc_Lng;
    ArrayList Arr_Loc_Name;
    ArrayList<String> Arr_Loc_Desc;
    ArrayList<String> Arr_Loc_Distance_from_geo_point;
    ArrayList<String> Arr_Loc_Img_URL;
    ArrayList<String> Arr_Loc_Distance_from_Usr;
    int i;
    String AC ;
    HashMap<String, Double> N_I_Map;
    HashMap<String, Double> N_D_Map;
    HashMap<String, Double> N_De_Map;
    HashMap<String, Double> N_IS_Map ;
    HashMap<String, Double> N_SD_Map;
    HashMap<String, Double> N_AD_Add;

    ArrayList<String> G_H_LOC_Names;
    ArrayList<String> G_H_Distance;
    ArrayList<String> G_H_Loc_Desc;
    ArrayList<String> G_H_Img_URL;

    SensorManager SM;
    Sensor S_Type;
    ListView LV;
    Timer timer;
    CustomListAdapter adapter;
    double New_Distance;
    Handler handler;
    Runnable RT;

    ListView list;
    TextView Current_Usr_Details, Current_Usr_Lat_Lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_customlist);


        //TB_Toolbar = (Toolbar)findViewById(R.id.top_toolbar);
        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        //setSuppportActionBar(toolbar);
        //TB_Toolbar.setTitle("fsdfgsddsfsd");
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(false);
        //getSupportActionBar().setWindowTitle("Hello World");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

     //   SM = (SensorManager) getSystemService(SENSOR_SERVICE);
      //  S_Type = SM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Current_Usr_Details = (TextView)findViewById(R.id.tv_usr_details);
        Current_Usr_Lat_Lng = (TextView)findViewById(R.id.tv_usr_lat_lng);


        // buildGoogleApiClient();
     //   if(!(S_Type == null)) {
      //if(S_Type == null) {

            p_status = checkLocationPermission();

            if (p_status) {
            } else {
                ActivityCompat.requestPermissions(Act_custom_list.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            buildGoogleApiClient();
        //  Arr_Loc_Distance_from_Usr = new ArrayList<String>();
            // this.setListAdapter(new ArrayAdapter<String>(
            //this, R.layout.mylist,
            // R.id.Itemname,itemname));

            if (!Connection_Detection.getInstance(Act_custom_list.this).isOnline()) {
                AlertDialog.Builder AD_Internet = new AlertDialog.Builder(Act_custom_list.this);
                AD_Internet.setTitle("Internet Alert");
                AD_Internet.setIcon(android.R.drawable.ic_dialog_alert);
                AD_Internet.setMessage(getResources().getString(R.string.loggedin_scr_internet_txt));
                AD_Internet.setCancelable(false);

                AD_Internet.setPositiveButton(
                        "Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Act_custom_list.this.finish();
                            }
                        });

                AlertDialog AL_Internet = AD_Internet.create();
                AL_Internet.show();

            } else {

               // fetch_data();

            }
            //buildGoogleApiClient();
      /*}
        else
        {
            buildGoogleApiClient();

            AC = getIntent().getAction();
            if(AC.equalsIgnoreCase("Iamback")) {

            }
            else {
                Intent I_Go = new Intent(getApplicationContext(), CompassActivity.class);
                startActivity(I_Go);
                finish();
            }

        }*/



//        CustomListAdapter adapter = new CustomListAdapter(this, itemname, imgid);
      //  CustomListAdapter adapter = new CustomListAdapter(Act_custom_list.this, Arr_Loc_Img_URL,  Arr_Loc_Distance_from_Usr );
       /// list = getListView();
        //list.setAdapter(adapter);

       /* list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Slecteditem = itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();


            }


        }); */
       //    }


    }



    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        //Arr_Loc_Distance_from_Usr = new ArrayList<String>();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(RT);
        mGoogleApiClient.disconnect();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

       // final Handler handler = new Handler();
        handler = new Handler();

         RT= new Runnable() {
             //   handler.postDelayed( new Runnable() {


             @Override
             public void run() {

                 //   handler.postDelayed( this, 60 * 500 );
                 handler.postDelayed(RT, 60 * 500);
                 Toast.makeText(getApplicationContext(), "Updating Distance in the View...", Toast.LENGTH_SHORT).show();
                 startLocationUpdates();
                 //  Arr_Loc_Distance_from_Usr = new ArrayList<String>();
                 AC = getIntent().getAction();
                 if (AC.equalsIgnoreCase("Iamback") || AC.equalsIgnoreCase("IamfromSplash")) {

                 }

                 if (Arr_Loc_Distance_from_Usr.size() == 0) {
                 } else {

                     Arr_Loc_Distance_from_Usr.clear();
                     //    Arr_Loc_Distance_from_Usr.add("0");
                 }
                /*-Arr_Loc_Name.add("Me");
                Arr_Loc_Img_URL.add("http://www.idroidindia.com/wp-content/themes/lms/images/footer-logo.png");
                Arr_Loc_Desc.add("HEllo");
                Arr_Loc_Distance_from_Usr.add("0"); */

                 if (Usr_Latitude.isEmpty() || Usr_Lognitude.isEmpty()) {
                     NN_distance = "No Data";
                 } else {

                     Log.i("New Usr Lat", Usr_Latitude);
                     Log.i("New Usr Lng", Usr_Lognitude);
                     //Arr_Loc_Distance_from_Usr.add(Usr_Latitude + "  " + Usr_Lognitude);
                     Current_Usr_Details.setText("ME @ ");
                     Current_Usr_Lat_Lng.setText(Usr_Latitude +"\n"+Usr_Lognitude);

                     for (int i = 0; i < Arr_Loc_Lat.size(); i++) {

                         New_Distance = distanceKm(Double.parseDouble(Arr_Loc_Lat.get(i)), Double.parseDouble(Arr_Loc_Lng.get(i)), Double.parseDouble(Usr_Latitude), Double.parseDouble(Usr_Lognitude));
                         Log.i(" New Distance " + i, String.valueOf(New_Distance));

                         DecimalFormat DFF = new DecimalFormat("#.##");
                         NN_distance = DFF.format(New_Distance);

                         if (NN_distance.equalsIgnoreCase("No Data")) {
                             Arr_Loc_Distance_from_Usr.add(NN_distance);
                         } else {
                            // Arr_Loc_Distance_from_Usr.add(NN_distance + " km from the user");
                             Arr_Loc_Distance_from_Usr.add(NN_distance );
                         }
                         Log.i(" New Distance List " + i, Arr_Loc_Distance_from_Usr.get(i).toString() + "i");
                         Log.i(" New Distance List size", String.valueOf(Arr_Loc_Distance_from_Usr.size()));
                         Log.i(" New Distance LNG size", String.valueOf(Arr_Loc_Lat.size()));
                     }
                 }
                 //update_new_ui(Arr_Loc_Distance_from_Usr);
                 //  adapter.notifyDataSetChanged();


                 for(int z = 0; z<5; z++) {
                     if(Arr_Loc_Distance_from_Usr.get(0).equalsIgnoreCase("No data"))
                     {}
                     else {
                         N_I_Map.put(Arr_Loc_Img_URL.get(z).toString(), Double.parseDouble(Arr_Loc_Distance_from_Usr.get(z)));
                         N_D_Map.put(Arr_Loc_Name.get(z).toString(), Double.parseDouble(Arr_Loc_Distance_from_Usr.get(z)));
                         N_De_Map.put(Arr_Loc_Desc.get(z).toString(), Double.parseDouble(Arr_Loc_Distance_from_Usr.get(z)));
                     }
                 }
                 Log.i("Hash N_I_Map Update", String.valueOf(N_I_Map.size()));
                 Log.i("Hash N_D_Map Update", String.valueOf(N_D_Map.size()));
                 Log.i("Hash N_De_Map Update", String.valueOf(N_De_Map.size()));

                 N_IS_Map = sortHashMapByValuesD(N_I_Map);
                 N_SD_Map = sortHashMapByValuesD(N_D_Map);
                 N_AD_Add = sortHashMapByValuesD(N_De_Map);


                 return_distance(N_SD_Map);
                 return_key(N_SD_Map);
                 return_places_name(N_AD_Add);
                 return_img_urls(N_IS_Map);

                 update_ui();
             }
             // }, 60 * 500 );
         };

        handler.postDelayed(RT, 60 * 500);

    }


    @Override
    protected void onStop() {
        super.onStop();
      //  handler.removeCallbacks(RT);
        mGoogleApiClient.disconnect();
    }



    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Usr_Latitude = String.valueOf(mLastLocation.getLatitude());
            Usr_Lognitude = String.valueOf(mLastLocation.getLongitude());

            Log.i("Lat", Usr_Latitude);
            Log.i("Log", Usr_Lognitude);

            if (!(Usr_Latitude.isEmpty() && Usr_Lognitude.isEmpty())) {

                if (Connection_Detection.getInstance(Act_custom_list.this).isOnline()) {

                    PD = new ProgressDialog(Act_custom_list.this);
                    PD.setMessage("Fetching User Details from Database.....");
                    PD.setCancelable(true);
                    PD.show();

                    fetch_data();
                }
                else {}
            }
            else {}


        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        buildGoogleApiClient();


    }

    @Override
    public void onLocationChanged(Location location) {

        Usr_Latitude = String.valueOf(location.getLatitude());
        Usr_Lognitude = String.valueOf(location.getLongitude());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(RT);
        mGoogleApiClient.disconnect();
    }


    public void fetch_data() {
        String Data_Fetch_URL = "http://droidworld.in/jiit_loc_apis/Form/get_location_data.php";

        RequestParams RP = new RequestParams();
        RP.put("salt_key", "tata_namak");

        AsyncHttpClient Client = new AsyncHttpClient();
        Client.post(Data_Fetch_URL, RP, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.i("JSON Response", response.toString());

                Arr_Loc_ID = new ArrayList<String>();
                Arr_Loc_Name = new ArrayList<>();
                Arr_Loc_Desc = new ArrayList<String>();
                Arr_Loc_Lat = new ArrayList<String>();
                Arr_Loc_Lng = new ArrayList<String>();
                Arr_Loc_Img_URL = new ArrayList<String>();
                Arr_Loc_Distance_from_geo_point = new ArrayList<String>();
                Arr_Loc_Distance_from_Usr = new ArrayList<String>();

                //Arr_Loc_Name.add("Me");
                //Arr_Loc_Img_URL.add("http://www.idroidindia.com/wp-content/themes/lms/images/footer-logo.png");
                //Arr_Loc_Desc.add("This is me");

                if (Usr_Latitude.isEmpty() || Usr_Lognitude.isEmpty()) {
                    F_distance = "No Data";
                  //  Arr_Loc_Distance_from_Usr.add(F_distance);
                    Current_Usr_Details.setText("ME @ "+F_distance);
                }
                else {
                   /* double Usr_Distance = distanceKm(Double.parseDouble(Usr_Latitude), Double.parseDouble(Usr_Lognitude), Double.parseDouble(Usr_Latitude), Double.parseDouble(Usr_Lognitude));
                    Log.i(" Usr Distance ", String.valueOf(Usr_Distance));

                    DecimalFormat DF = new DecimalFormat("#.##");
                    // String F_distance = decimalFormat.format(Final_Distance_1);
                    Loc_Distance = DF.format(Usr_Distance);*/
                    //Arr_Loc_Desc.add("This is me");
                    //Arr_Loc_Distance_from_Usr.add(Usr_Latitude +"   "+ Usr_Lognitude);
                    Current_Usr_Details.setText("ME @ ");
                    Current_Usr_Lat_Lng.setText(Usr_Latitude +"\n"+Usr_Lognitude);
                }

                //   PD.dismiss();

                try {
                    JA = response.getJSONArray("location_details");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (i = JA.length() - 1; i > JA.length() - 6; i--) {

                    try {
                        JO = JA.getJSONObject(i);
                        Loc_ID = JO.getString("loc_id");
                        Loc_Name = JO.getString("loc_name");

                        Loc_Desc = JO.getString("loc_description");
                        Loc_Lat = JO.getString("loc_latitude");
                        Loc_Lng = JO.getString("loc_lognitude");
                        Loc_Img_URL = JO.getString("loc_img_url");
                        Loc_Distance_from_geo_point = JO.getString("loc_distance_from_geo_point");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Arr_Loc_Lat.add(Loc_Lat);
                    Arr_Loc_Lng.add(Loc_Lng);
                    Arr_Loc_Name.add(Loc_Name);
                    Arr_Loc_Img_URL.add(Loc_Img_URL);
                    Arr_Loc_Desc.add(Loc_Desc);

                    N_I_Map = new HashMap<String, Double>();
                    N_D_Map = new HashMap<String, Double>();
                    N_De_Map = new HashMap<String, Double>();

                    N_IS_Map = new HashMap<String, Double>();
                    N_AD_Add = new HashMap<String, Double>();
                    N_SD_Map = new HashMap<String, Double>();



                    /*Log.i("JSON Loc_ID", Loc_ID);
                    Log.i("JSON Loc_Name", Loc_Name);
                    Log.i("JSON Loc_Desc", Loc_Desc);
                    Log.i("JSON Loc_Lat", Loc_Lat);
                    Log.i("JSON Loc_Lng", Loc_Lng);
                    Log.i("JSON Loc_Img_URL", Loc_Img_URL);
                    Log.i("JSON G_point", Loc_Distance_from_geo_point);

                    Arr_Loc_ID.add(Loc_ID);
                    Arr_Loc_Name.add(Loc_Name);
                    Arr_Loc_Desc.add(Loc_Desc);
                    Arr_Loc_Lat.add(Loc_Lat);
                    Arr_Loc_Lng.add(Loc_Lng);
                    Arr_Loc_Img_URL.add(Loc_Img_URL);
                    Arr_Loc_Distance_from_geo_point.add(Loc_Distance_from_geo_point);

                    Log.i(" Arr Img Size", String.valueOf(Arr_Loc_Img_URL.size()));
                    Log.i(" c Lat Size", String.valueOf(Arr_Loc_Lat.size()));
                    Log.i(" Arr Lng Size", String.valueOf(Arr_Loc_Lng.size()));*/

                    Log.i("Fetched Lat", Loc_Lat);
                    Log.i("Fetched Lng", Loc_Lng);

                    Log.i("My Lat", Usr_Latitude);
                    Log.i("My Lng", Usr_Lognitude);

                    if (Usr_Latitude.isEmpty() || Usr_Lognitude.isEmpty()) {

                        F_distance = "No Data";

                    } else {
                        double Final_Distance_1 = distanceKm(Double.parseDouble(Loc_Lat), Double.parseDouble(Loc_Lng), Double.parseDouble(Usr_Latitude), Double.parseDouble(Usr_Lognitude));
                        Log.i(" Final Distance 1 " + i, String.valueOf(Final_Distance_1));

                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        // String F_distance = decimalFormat.format(Final_Distance_1);
                        F_distance = decimalFormat.format(Final_Distance_1);
                    }

                    if(F_distance.equalsIgnoreCase("No Data"))
                    {
                        Arr_Loc_Distance_from_Usr.add(F_distance);
                    }
                    else {
                        //Arr_Loc_Distance_from_Usr.add(F_distance + " km from the user");
                        Arr_Loc_Distance_from_Usr.add(F_distance );

                    }
                   //Arr_Loc_Distance_from_Usr.add(F_distance+" km from the user");



                    //Arr_Loc_Distance_from_Usr.add(Final_Distance_1);

                    //  Log.i("Arr Distance", String.valueOf(Arr_Loc_Distance_from_Usr.get(i)));
                    //  Log.i("Arr Distance Arr", String.format("%.2f",(String.valueOf(distance))));

                }
               // PD.dismiss();
               /* CustomListAdapter adapter = new CustomListAdapter(Act_custom_list.this,Arr_Loc_Name ,  Arr_Loc_Img_URL, Arr_Loc_Desc,Arr_Loc_Distance_from_Usr );
                list = getListView();
                list.setAdapter(adapter); */

                 for(int z = 0; z<5; z++) {
                    if(Arr_Loc_Distance_from_Usr.get(0).equalsIgnoreCase("No data"))
                    {}
                    else {
                        N_I_Map.put(Arr_Loc_Img_URL.get(z).toString(), Double.parseDouble(Arr_Loc_Distance_from_Usr.get(z)));
                        N_D_Map.put(Arr_Loc_Name.get(z).toString(), Double.parseDouble(Arr_Loc_Distance_from_Usr.get(z)));
                        N_De_Map.put(Arr_Loc_Desc.get(z).toString(), Double.parseDouble(Arr_Loc_Distance_from_Usr.get(z)));
                    }
                }
                Log.i("Size of Hash N_I_Map", String.valueOf(N_I_Map.size()));
                Log.i("Size of Hash N_D_Map", String.valueOf(N_D_Map.size()));
                Log.i("Size of Hash N_De_Map", String.valueOf(N_De_Map.size()));

                N_IS_Map = sortHashMapByValuesD(N_I_Map);
                N_SD_Map = sortHashMapByValuesD(N_D_Map);
                N_AD_Add = sortHashMapByValuesD(N_De_Map);



                return_distance(N_SD_Map);
                return_key(N_SD_Map);
                return_places_name(N_AD_Add);
                return_img_urls(N_IS_Map);

                update_ui();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }
        });

    }

    public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String)key, (Double)val);
                    break;
                }

            }

        }
        return sortedMap;
    }

    private void return_key(HashMap<String, Double> H_Map){

        G_H_LOC_Names = new ArrayList<String>();

        for(String key : H_Map.keySet()){

            G_H_LOC_Names.add(key);
        }

//       return  H_LOC_Names;

    }

    private void return_distance(HashMap<String, Double> H_Map){

        G_H_Distance = new ArrayList<String>();

        for (Map.Entry<String,Double> entry : H_Map.entrySet()) {
            System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
            String Distance = entry.getValue().toString();
            G_H_Distance.add(Distance+" km from the user");
        }

        //   return  H_Distance;

    }

    private void return_places_name(HashMap<String, Double> H_Map){

        G_H_Loc_Desc = new ArrayList<String>();

        for(String key : H_Map.keySet()){

            G_H_Loc_Desc.add(key);
        }

//       return  H_LOC_Names;

    }

    private void return_img_urls(HashMap<String, Double> H_Map){

        G_H_Img_URL = new ArrayList<String>();

        for(String key : H_Map.keySet()){

            G_H_Img_URL.add(key);
        }

//       return  H_LOC_Names;

    }


    public static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        int EARTH_RADIUS_KM = 6371;
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLonRad = Math.toRadians(lon2 - lon1);

        return Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)) * EARTH_RADIUS_KM;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "Permission Granted, Enjoy", Toast.LENGTH_SHORT).show();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

        }
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);

    }

    public void update_ui(){

        //Currnet_Usr = (TextView)findViewById(R.id.tv_current_usr);
        //Currnet_Usr.setText("Me" + "\n"+ "This is me "+"0 Km");

      //  adapter = new CustomListAdapter(Act_custom_list.this,Arr_Loc_Name ,  Arr_Loc_Img_URL, Arr_Loc_Desc,Arr_Loc_Distance_from_Usr );
        adapter = new CustomListAdapter(Act_custom_list.this, G_H_LOC_Names, G_H_Img_URL, G_H_Loc_Desc, G_H_Distance );
        //list = getListView();
        list = (ListView)findViewById(R.id.usr_list);
        list.setAdapter(adapter);
        PD.dismiss();
        list.setOnItemClickListener(Act_custom_list.this);

    }

    /*private void update_new_ui(ArrayList<String> New_Arr_loc_distance_from_usr) {


        adapter = new CustomListAdapter(Act_custom_list.this,Arr_Loc_Name ,  Arr_Loc_Img_URL, Arr_Loc_Desc,New_Arr_loc_distance_from_usr );
        list = getListView();
        list.setAdapter(adapter);


    }*/

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        if(!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            Log.i("mGoogleApiClient status", String.valueOf(mGoogleApiClient.isConnected()));
        }
        else {

        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {

            Usr_Latitude = String.valueOf(mLastLocation.getLatitude());
            Usr_Lognitude = String.valueOf(mLastLocation.getLongitude());

            Log.i("New Lat", Usr_Latitude);
            Log.i("New Lng", Usr_Lognitude);
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

     String Slecteditem = G_H_LOC_Names.get(i).toString();
     //   String Slecteditem = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(getApplicationContext(),  Slecteditem, Toast.LENGTH_SHORT).show();

        if(Slecteditem.equalsIgnoreCase("iDroidIndia"))
        {
            handler.removeCallbacks(RT);
            Intent I_Go = new Intent(getApplicationContext(), CompassActivity.class);
            startActivity(I_Go);
            finish();

        }
        else
        {}
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Act_custom_list.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();

    }


}



