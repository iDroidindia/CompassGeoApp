package test_app.location.compass_location_application.compassgeoapp;


import android.Manifest;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.squareup.picasso.Picasso;

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
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class CompassActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener , ToolTipView.OnToolTipViewClickedListener{

    private static final String TAG = "CompassActivity";
    private static LatLng latlong1, latlong2;
    private Compass compass;
    //Get_Location GL;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    String Usr_Latitude ="", Usr_Lognitude="";
    private LocationRequest mLocationRequest;
    Connection_Detection CD;
    String F_distance;
    boolean p_status;

    private ToolTipView mUsrToolTipView, mRedToolTipView, mGreenToolTipView,mBlueToolTipView, mMagentaToolTipView, mGrayToolTipView;
    ToolTipRelativeLayout ToolTip_IV_Usr_Location, ToolTip_IV_Usr_1, ToolTip_IV_Usr_2, ToolTip_IV_Usr_3, ToolTip_IV_Usr_4, ToolTip_IV_Usr_5;
    ToolTip Tool_Tip_Usr,Tool_Tip_Red, Tool_Tip_Green, Tool_Tip_Blue, Tool_Tip_Gray, Tool_Tip_Magenta;

    ImageView IV_Usr_Loc, IV_Usr_1,IV_Usr_2,IV_Usr_3,IV_Usr_4,IV_Usr_5;
    String Loc_ID, Loc_Lat, Loc_Lng,Loc_Name, Loc_Desc, Loc_Distance_from_geo_point, Loc_Img_URL;
    ProgressDialog PD;
    private JSONObject JO;
    private JSONArray JA;
    ArrayList<String> Arr_Loc_ID;
    ArrayList<String> Arr_Loc_Lat;
    ArrayList<String> Arr_Loc_Lng;
    ArrayList<String> Arr_Loc_Add;
    ArrayList Arr_Loc_Name;
    ArrayList<String> Arr_Loc_Desc;
    ArrayList<String> Arr_Loc_Distance_from_geo_point;
    ArrayList<String> Arr_Loc_Img_URL;
    ArrayList<String> Arr_Loc_Distance_from_Usr;
    int i;
    HashMap<String, Double> N_I_Map;
    HashMap<String, Double> N_D_Map;
    HashMap<String, Double> N_A_Map;
    HashMap<String, Double> N_IS_Map ;
    HashMap<String, Double> N_SD_Map;
    HashMap<String, Double> N_AD_Add;
    ArrayList<String> G_H_LOC_Names;
    ArrayList<String> G_H_Distance;
    ArrayList<String> G_H_Places_Name;
    Toolbar toolbar;
    UserListDbHelper uldh;
    Cursor DB_Data_S;
    ArrayList<String> L_Loc_Name, L_Loc_Desc, L_Loc_Lat, L_Loc_Log,L_Loc_Img_Url;

    //double Final_Distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_act_1);

            compass = new Compass(this);

            compass.arrowView = (ImageView) findViewById(R.id.main_image_hands);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        //setSuppportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setWindowTitle("Hello World");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

     //   uldh = new UserListDbHelper(getApplicationContext());
       // uldh.createTable();

            IV_Usr_Loc = (ImageView) findViewById(R.id.iv_usr_location);
            IV_Usr_1 = (ImageView) findViewById(R.id.imageView1);
            IV_Usr_2 = (ImageView) findViewById(R.id.imageView2);
            IV_Usr_3 = (ImageView) findViewById(R.id.imageView3);
            IV_Usr_4 = (ImageView) findViewById(R.id.imageView4);
            IV_Usr_5 = (ImageView) findViewById(R.id.imageView5);

            ToolTip_IV_Usr_Location = (ToolTipRelativeLayout) findViewById(R.id.tooltip_iv_usr_location);
            ToolTip_IV_Usr_1 = (ToolTipRelativeLayout) findViewById(R.id.tooltip_iv_usr_loc_1);
            ToolTip_IV_Usr_2 = (ToolTipRelativeLayout) findViewById(R.id.tooltip_iv_usr_loc_2);
            ToolTip_IV_Usr_3 = (ToolTipRelativeLayout) findViewById(R.id.tooltip_iv_usr_loc_3);
            ToolTip_IV_Usr_4 = (ToolTipRelativeLayout) findViewById(R.id.tooltip_iv_usr_loc_4);
            ToolTip_IV_Usr_5 = (ToolTipRelativeLayout) findViewById(R.id.tooltip_iv_usr_loc_5);

            CD = new Connection_Detection();
            p_status = checkLocationPermission();

            if (p_status) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            buildGoogleApiClient();

            if (!Connection_Detection.getInstance(CompassActivity.this).isOnline()) {

               /* L_Loc_Name = new ArrayList<String>();
                L_Loc_Desc = new ArrayList<String>();
                L_Loc_Lat = new ArrayList<String>();
                L_Loc_Log = new ArrayList<String>();
                L_Loc_Img_Url = new ArrayList<String>();


                Cursor cursor = uldh.getSQLiteDBdata();

                L_Loc_Img_Url = uldh.getLoc_Img_URL_Data();
                L_Loc_Name= uldh.getLoc_name_Data();
                L_Loc_Desc = uldh.getLoc_description_Data();
                L_Loc_Lat = uldh.getLoc_latitude_Data();
                L_Loc_Log = uldh.getLoc_latitude_Data(); */


             /*    if (cursor.moveToFirst()) {
                    do {

                        L_Loc_Name.add(cursor.getString(cursor.getColumnIndex(uldh.LOC_Name)));
                        L_Loc_Dec.add(cursor.getString(cursor.getColumnIndex(uldh.LOC_Description)));
                        L_Loc_Lat.add(cursor.getString(cursor.getColumnIndex(uldh.LOC_Latitude)));
                        L_Loc_Log.add(cursor.getString(cursor.getColumnIndex(uldh.LOC_Longitude)));
                        L_Loc_Img_Url.add(cursor.getString(cursor.getColumnIndex(uldh.LOC_Image_URL)));




                    } while (cursor.moveToNext());
                } */


              /*  Toast.makeText(getApplicationContext(), "Size "+L_Loc_Img_Url.size(), Toast.LENGTH_LONG).show();
                update_ui(L_Loc_Img_Url);

                cursor.close();*/

                 AlertDialog.Builder AD_Internet = new AlertDialog.Builder(CompassActivity.this);
                AD_Internet.setTitle("Internet Alert");
                AD_Internet.setIcon(android.R.drawable.ic_dialog_alert);
                AD_Internet.setMessage(getResources().getString(R.string.loggedin_scr_internet_txt));
                AD_Internet.setCancelable(false);

                AD_Internet.setPositiveButton(
                        "Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                CompassActivity.this.finish();
                            }
                        });

                AlertDialog AL_Internet = AD_Internet.create();
                AL_Internet.show();





            } else {

                if(!isLocationEnabled(getApplicationContext()))
                {
                    AlertDialog.Builder AD_Loc= new AlertDialog.Builder(CompassActivity.this);
                    AD_Loc.setTitle("Location Alert");
                    AD_Loc.setIcon(android.R.drawable.ic_dialog_alert);
                    AD_Loc.setMessage(getResources().getString(R.string.loggedin_scr_location_txt));
                    AD_Loc.setCancelable(false);

                    AD_Loc.setPositiveButton(
                            "Exit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    CompassActivity.this.finish();
                                }
                            });

                    AlertDialog AL_Loc = AD_Loc.create();
                    AL_Loc.show();

                }
                else {

                   /* if (!(Usr_Latitude.isEmpty() && Usr_Lognitude.isEmpty())) {

                        fetch_data();
                     }
                    else {}*/
                }
            }

            IV_Usr_Loc.setOnClickListener(this);
            IV_Usr_1.setOnClickListener(this);
            IV_Usr_2.setOnClickListener(this);
            IV_Usr_3.setOnClickListener(this);
            IV_Usr_4.setOnClickListener(this);
            IV_Usr_5.setOnClickListener(this);
        }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        compass.start();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        compass.stop();
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
    protected void onRestart() {
        super.onRestart();
        mGoogleApiClient.connect();
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

            Log.i("Usr Lat", Usr_Latitude);
            Log.i("Usr Log", Usr_Lognitude);

            if (!(Usr_Latitude.isEmpty() && Usr_Lognitude.isEmpty())) {

                if (Connection_Detection.getInstance(CompassActivity.this).isOnline()) {

                    PD = new ProgressDialog(CompassActivity.this);
                    PD.setMessage("Fetching User Details from Database.....");
                    PD.setCancelable(true);
                    PD.show();
                    fetch_data();
                }
                else {}
            }
            else {}

           /*  Usr_Loc=new Location("Usr_Loc");
            Usr_Loc.setLatitude(Double.parseDouble(Usr_Lognitude));
            Usr_Loc.setLongitude(Double.parseDouble(Usr_Lognitude));*/
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
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.iv_usr_location:

                if (mUsrToolTipView == null) {
                    addUsrToolTipView(R.id.iv_usr_location);
                } else {
                    mUsrToolTipView.remove();
                    mUsrToolTipView = null;
                }


//                addUsrToolTipView(R.id.iv_usr_location);

    //            Toast.makeText(getApplicationContext(), Usr_Latitude + "\n" + Usr_Lognitude, Toast.LENGTH_LONG).show();
                break;

            case R.id.imageView1:
                /*S_ID = String.valueOf(v.getId());
                L_Char = S_ID.substring(S_ID.length() - 1);
                Log.i("L Char", L_Char);
                addRedToolTipView(Integer.parseInt(L_Char), Integer.parseInt(L_Char));*/
                //addRedToolTipView(R.id.imageView1);


                if (mRedToolTipView == null) {
                    addRedToolTipView(R.id.imageView1);
                } else {
                    mRedToolTipView.remove();
                    mRedToolTipView = null;
                }


                break;


            case R.id.imageView2:
               /* S_ID = String.valueOf(v.getId());
                L_Char = S_ID.substring(S_ID.length() - 1);
                Log.i("L Char", L_Char);
                addGreenToolTipView(Integer.parseInt(L_Char), Integer.parseInt(L_Char));*/
              //  addRedToolTipView(R.id.imageView2);
                //addGreenToolTipView();
                if (mGreenToolTipView == null) {
                    addGreenToolTipView(R.id.imageView2);
                } else {
                    mGreenToolTipView.remove();
                    mGreenToolTipView = null;
                }

                break;

            case R.id.imageView3:

            /*  S_ID = String.valueOf(v.getId());
                L_Char = S_ID.substring(S_ID.length() - 1);
                Log.i("L Char", L_Char);

                addBlueToolTipView(Integer.parseInt(L_Char), Integer.parseInt(L_Char));*/
                //addBlueToolTipView(R.id.imageView3);

                //Toast.makeText(getApplicationContext(), Arr_Loc_Name.get(3) + "\n" + "Distance: " + Arr_Loc_Distance_from_Usr.get(3), Toast.LENGTH_SHORT).show();                break;

                if (mBlueToolTipView == null) {
                    addBlueToolTipView(R.id.imageView3);
                } else {
                    mBlueToolTipView.remove();
                    mBlueToolTipView = null;
                }
                break;
            case R.id.imageView4:
               /* S_ID = String.valueOf(v.getId());
                L_Char = S_ID.substring(S_ID.length() - 1);
                Log.i("L Char", L_Char);

                addGrayToolTipView(Integer.parseInt(L_Char), Integer.parseInt(L_Char)); */
              //  addGrayToolTipView(R.id.imageView4);
                //Toast.makeText(getApplicationContext(), Arr_Loc_Name.get(4) + "\n" + "Distance: " + Arr_Loc_Distance_from_Usr.get(4), Toast.LENGTH_SHORT).show();

                if (mGrayToolTipView == null) {
                    addGrayToolTipView(R.id.imageView4);
                } else {
                    mGrayToolTipView.remove();
                    mGrayToolTipView = null;
                }
                break;

            case R.id.imageView5:
              /*  S_ID = String.valueOf(v.getId());
                L_Char = S_ID.substring(S_ID.length() - 1);
                Log.i("L Char", L_Char);

                //addMagentaToolTipView(Integer.parseInt(L_Char), Integer.parseInt(L_Char)); */
               // addMagentaToolTipView(R.id.imageView5);
                //Toast.makeText(getApplicationContext(), Arr_Loc_Name.get(5) + "\n" + "Distance: " + Arr_Loc_Distance_from_Usr.get(5), Toast.LENGTH_SHORT).show();

                if (mMagentaToolTipView == null) {
                    addMagentaToolTipView(R.id.imageView5);
                } else {
                    mMagentaToolTipView.remove();
                    mMagentaToolTipView = null;
                }
                break;

            default:

                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onToolTipViewClicked(ToolTipView toolTipView) {
        //toolTipView.remove();
       // Toast.makeText(getApplicationContext(), Latitude + "\n"+ Lognitude, Toast.LENGTH_LONG).show();
    }

    public void addUsrToolTipView(int ID) {

        String C_Add = getCompleteAddressString(Double.parseDouble(Usr_Latitude),Double.parseDouble(Usr_Lognitude));
        Tool_Tip_Usr = new ToolTip()
               // .withText("Me @ "+ "\n" +Usr_Latitude + "\n" + Usr_Lognitude)
                .withText("Me @ "+"\n"+Usr_Latitude+"  "+Usr_Lognitude+ "\n" + C_Add)
                .withTextColor(Color.WHITE)
                .withColor(Color.RED)
                .withAnimationType(ToolTip.AnimationType.FROM_TOP);

        mUsrToolTipView = new ToolTipView(this);
        //mUsrToolTipView = ToolTip_IV_Usr_1.showToolTipForView(Tool_Tip_Red, findViewById(R.id.iv_usr_location));
        mUsrToolTipView = ToolTip_IV_Usr_Location.showToolTipForView(Tool_Tip_Usr, findViewById(ID));
        mUsrToolTipView.setOnToolTipViewClickedListener(this);

    }

  //  public void addRedToolTipView(int pos_name, int pos_distance) {
    public void addRedToolTipView(int Id) {

            /*Tool_Tip_Red = new ToolTip()
                    .withText(Arr_Loc_Name.get(0) + "\n" + "Distance: " + Arr_Loc_Distance_from_Usr.get(0))
                    .withTextColor(Color.WHITE)
                    .withColor(Color.RED)
                    .withAnimationType(ToolTip.AnimationType.FROM_TOP);*/


            Tool_Tip_Red = new ToolTip()
                    .withText(G_H_LOC_Names.get(0) + "\n" + G_H_Places_Name.get(0) + "\n" + "Distance: " + G_H_Distance.get(0) + " km")
                    .withTextColor(Color.WHITE)
                    .withColor(Color.RED)
                    .withAnimationType(ToolTip.AnimationType.FROM_TOP);

            mRedToolTipView = new ToolTipView(this);
            //mRedToolTipView = ToolTip_IV_Usr_1.showToolTipForView(Tool_Tip_Red, findViewById(R.id.imageView1));
            mRedToolTipView = ToolTip_IV_Usr_1.showToolTipForView(Tool_Tip_Red, findViewById(Id));
            mRedToolTipView.setOnToolTipViewClickedListener(this);


    }

   // public void addGreenToolTipView(int pos_name, int pos_distance) {
   public void addGreenToolTipView(int Id) {

        Tool_Tip_Green = new ToolTip()
                //.withText(Arr_Loc_Name.get(1) + "\n" + "Distance: " + Arr_Loc_Distance_from_Usr.get(1))
                //.withText(G_H_LOC_Names.get(1) + "\n" + "Distance: " + G_H_Distance.get(1))
                .withText(G_H_LOC_Names.get(1) + "\n" + G_H_Places_Name.get(1)+"\n"+ "Distance: " + G_H_Distance.get(1)+" km")
                .withTextColor(Color.WHITE)
                .withColor(Color.GREEN)
                .withAnimationType(ToolTip.AnimationType.FROM_TOP);

        mGreenToolTipView = new ToolTipView(this);
        //mGreenToolTipView = ToolTip_IV_Usr_4.showToolTipForView(Tool_Tip_Green, findViewById(R.id.imageView2));
       mGreenToolTipView = ToolTip_IV_Usr_2.showToolTipForView(Tool_Tip_Green, findViewById(Id));
        mGreenToolTipView.setOnToolTipViewClickedListener(this);

    }
   // public void addBlueToolTipView(int pos_name, int pos_distance) {
   public void addBlueToolTipView(int ID) {

      Tool_Tip_Blue = new ToolTip()
                //.withText(Arr_Loc_Name.get(2) + "\n" + "Distance: " + Arr_Loc_Distance_from_Usr.get(2))
                //.withText(G_H_LOC_Names.get(2) + "\n" + "Distance: " + G_H_Distance.get(2))
              .withText(G_H_LOC_Names.get(2) + "\n" + G_H_Places_Name.get(2)+"\n"+ "Distance: " + G_H_Distance.get(2)+" km")
                .withTextColor(Color.WHITE)
                .withColor(Color.BLUE)
               .withAnimationType(ToolTip.AnimationType.FROM_TOP);

        mBlueToolTipView = new ToolTipView(this);
        mBlueToolTipView = ToolTip_IV_Usr_3.showToolTipForView(Tool_Tip_Blue, findViewById(ID));
        mBlueToolTipView.setOnToolTipViewClickedListener(this);

    }

    //public void addGrayToolTipView(int pos_name, int pos_distance) {
    public void addGrayToolTipView(int ID) {

        Tool_Tip_Gray = new ToolTip()
                //.withText(Arr_Loc_Name.get(3) + "\n" + "Distance: " + Arr_Loc_Distance_from_Usr.get(3))
               //  .withText(G_H_LOC_Names.get(3) + "\n" + "Distance: " + G_H_Distance.get(3))
                .withText(G_H_LOC_Names.get(3) + "\n" + G_H_Places_Name.get(3)+"\n"+ "Distance: " + G_H_Distance.get(3)+" km")
                .withTextColor(Color.WHITE)
                .withColor(Color.GRAY)
                .withAnimationType(ToolTip.AnimationType.FROM_TOP);

        mGrayToolTipView = new ToolTipView(this);

        mGrayToolTipView = ToolTip_IV_Usr_4.showToolTipForView(Tool_Tip_Gray, findViewById(ID));
        mGrayToolTipView.setOnToolTipViewClickedListener(this);

    }

    //public void addMagentaToolTipView(int pos_name, int pos_distance) {
    public void addMagentaToolTipView(int ID) {

        Tool_Tip_Magenta = new ToolTip()
                //.withText(Arr_Loc_Name.get(4) + "\n" + "Distance: \n" + Arr_Loc_Distance_from_Usr.get(4))
                //.withText(G_H_LOC_Names.get(4) + "\n" + "Distance: " + G_H_Distance.get(4))
                .withText(G_H_LOC_Names.get(4) + "\n" + G_H_Places_Name.get(4)+"\n"+ "Distance: " + G_H_Distance.get(4)+" km")
                .withTextColor(Color.WHITE)
                .withColor(Color.MAGENTA);

        mMagentaToolTipView = new ToolTipView(this);
        mMagentaToolTipView = ToolTip_IV_Usr_5.showToolTipForView(Tool_Tip_Magenta, findViewById(ID));
        mMagentaToolTipView.setOnToolTipViewClickedListener(this);

    }


    public void fetch_data(){

       /* PD = new ProgressDialog(CompassActivity.this);
        PD.setMessage("Fetching User Details from Database.....");
        PD.setCancelable(false);
        PD.show(); */
        //DB_Data_S = uldh.getSQLiteDBdata();

       // String Data_Fetch_URL = "http://droidworld.in/jiit_loc_apis/Form/old_get_location_data.php";

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
                Arr_Loc_Add = new ArrayList<String>();
                Arr_Loc_Img_URL = new ArrayList<String>();
                Arr_Loc_Distance_from_geo_point = new ArrayList<String>();
                Arr_Loc_Distance_from_Usr = new ArrayList<String>();
                N_I_Map = new HashMap<String, Double>();
                N_D_Map = new HashMap<String, Double>();
                N_A_Map = new HashMap<String, Double>();

             //   PD.dismiss();

                try {
                    JA = response.getJSONArray("location_details");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
             //   for(i =0 ; i < JA.length(); i++) {

              //  for(i =0 ; i < 5; i++) {
                for( i =JA.length()-1 ; i > JA.length()-6; i--) {

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

                        Log.i("JSON Loc_ID", Loc_ID);
                        Log.i("JSON Loc_Name", Loc_Name);
                        Log.i("JSON Loc_Desc", Loc_Desc);
                        Log.i("JSON Loc_Lat", Loc_Lat);
                        Log.i("JSON Loc_Lng", Loc_Lng);
                        Log.i("JSON Loc_Img_URL", Loc_Img_URL);
                        Log.i("JSON G_point", Loc_Distance_from_geo_point);

                        String Add = getCompleteAddressString(Double.parseDouble(Loc_Lat), Double.parseDouble(Loc_Lng));

                        Arr_Loc_ID.add(Loc_ID);
                        Arr_Loc_Name.add(Loc_Name);
                        Arr_Loc_Desc.add(Loc_Desc);
                        Arr_Loc_Lat.add(Loc_Lat);
                        Arr_Loc_Lng.add(Loc_Lng);
                        Arr_Loc_Img_URL.add(Loc_Img_URL);
                        Arr_Loc_Add.add(Add);
                        Arr_Loc_Distance_from_geo_point.add(Loc_Distance_from_geo_point);

                        Log.i(" Arr Img Size", String.valueOf(Arr_Loc_Img_URL.size()));
                        Log.i(" c Lat Size", String.valueOf(Arr_Loc_Lat.size()));
                        Log.i(" Arr Lng Size", String.valueOf(Arr_Loc_Lng.size()));
                        Log.i(" Arr LOC Size", String.valueOf(Arr_Loc_Add.size()));

                        Log.i("Fetched Lat",Loc_Lat);
                        Log.i("Fetched Lng", Loc_Lng);


                     // double Actual_Distance = distance_in_meter(Double.parseDouble(Usr_Lognitude), Double.parseDouble(Usr_Lognitude), Double.parseDouble(Loc_Lat), Double.parseDouble(Loc_Lng));
                     //Arr_Loc_Distance_from_Usr.add(Actual_Distance);

                       // Location LL = new Location("Usr_loc");
                        //float[] distance_results = new float[1];

                        //LL.distanceBetween(Double.parseDouble(Usr_Lognitude), Double.parseDouble(Usr_Lognitude), Double.parseDouble(Loc_Lat), Double.parseDouble(Loc_Lng), distance_results); // in km

                      // Arr_Loc_Distance_from_Usr.add(String.format("%.2f", distance_results[0] / 100000));

                      //  Log.i(" Arr Distance Size", String.valueOf(Arr_Loc_Distance_from_Usr.size()));

                 //   Log.i(" Arr Distance Arr", String.format("%.2f", distance_results[0] / 100000));
                   // Log.i(" Arr Distance Result", String.valueOf(distance_results[0] ));



                //    Location Fetched_Loc=new Location("Fetched_Loc");
                 //   Fetched_Loc.setLatitude(Double.parseDouble(Loc_Lat));
                  //  Fetched_Loc.setLongitude(Double.parseDouble(Loc_Lng));
                // double distance=Usr_Loc.distanceTo(Fetched_Loc);

                    //double Final_Distance = calculateDistance( Double.parseDouble(Loc_Lat), Double.parseDouble(Loc_Lng), Double.parseDouble(Usr_Latitude), Double.parseDouble(Usr_Lognitude));
                    //double Final_Distance =calculateDistance( 28.597638, 77.0483509, Double.parseDouble(Usr_Lognitude), Double.parseDouble(Usr_Lognitude));

                  //  Log.i(" Final Distance "+i, String.valueOf(Final_Distance));

                        Log.i("My Lat", Usr_Latitude);
                        Log.i("My Lng", Usr_Lognitude);


                        if(Usr_Latitude.isEmpty() || Usr_Lognitude.isEmpty())
                        {
                            F_distance = "No Data";
                        }
                    else {
                            double Final_Distance_1 = distanceKm(Double.parseDouble(Loc_Lat), Double.parseDouble(Loc_Lng), Double.parseDouble(Usr_Latitude), Double.parseDouble(Usr_Lognitude));
                            Log.i(" Final Distance 1 " + i, String.valueOf(Final_Distance_1));

                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                            // String F_distance = decimalFormat.format(Final_Distance_1);
                            F_distance = decimalFormat.format(Final_Distance_1);
                        }
                        Arr_Loc_Distance_from_Usr.add(F_distance);

                    //Arr_Loc_Distance_from_Usr.add(Final_Distance_1);

                  //  Log.i("Arr Distance", String.valueOf(Arr_Loc_Distance_from_Usr.get(i)));
                  //  Log.i("Arr Distance Arr", String.format("%.2f",(String.valueOf(distance))));


                    /*if(DB_Data_S.getCount() == 0)
                    {

                        uldh.insertData(Loc_Name, Loc_Desc, Loc_Lat, Loc_Lng, Loc_Img_URL);
                    }
                     else {


                    }*/
                }
                PD.dismiss();

                for(int z = 0; z<5; z++) {
                    if(Arr_Loc_Distance_from_Usr.get(0).equalsIgnoreCase("No data"))
                    {}
                    else {
                        N_I_Map.put(Arr_Loc_Img_URL.get(z).toString(), Double.parseDouble(Arr_Loc_Distance_from_Usr.get(z)));
                        N_D_Map.put(Arr_Loc_Name.get(z).toString(), Double.parseDouble(Arr_Loc_Distance_from_Usr.get(z)));
                        N_A_Map.put(Arr_Loc_Add.get(z).toString(), Double.parseDouble(Arr_Loc_Distance_from_Usr.get(z)));
                    }
                }
                Log.i("Size of Hash N_I_Map", String.valueOf(N_I_Map.size()));
                Log.i("Size of Hash N_D_Map", String.valueOf(N_D_Map.size()));

                N_IS_Map = sortHashMapByValuesD(N_I_Map);
                N_SD_Map = sortHashMapByValuesD(N_D_Map);
                N_AD_Add = sortHashMapByValuesD(N_A_Map);

                //update_ui();

                Log.i("Sorted Size Map N_I_Map", String.valueOf(N_IS_Map.size()));
                Log.i("Sorted Size Map N_D_Map", String.valueOf(N_SD_Map.size()));
                Log.i("Sorted Size Map N_A_Map", String.valueOf(N_AD_Add.size()));

               /* Cursor db_Cursor = uldh.getSQLiteDBdata();
                if(db_Cursor != null)
                {
                    Toast.makeText(getApplicationContext(), "Database already created", Toast.LENGTH_SHORT).show();
                }
                else {*/

                  //  Toast.makeText(getApplicationContext(), "Data inserted in to the Database", Toast.LENGTH_SHORT).show();
                //}

                update_new_ui(N_IS_Map);
                return_distance(N_SD_Map);
                return_key(N_SD_Map);
                return_places_name(N_AD_Add);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                PD.dismiss();
            }


            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);

                PD = new ProgressDialog(CompassActivity.this);
                PD.setMessage("Retrying.....");
                PD.setCancelable(false);
                PD.show();


            }
        });

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
            G_H_Distance.add(Distance);
        }

     //   return  H_Distance;

    }

    private void return_places_name(HashMap<String, Double> H_Map){

        G_H_Places_Name = new ArrayList<String>();

        for(String key : H_Map.keySet()){

            G_H_Places_Name.add(key);
        }

//       return  H_LOC_Names;

    }

    private void update_new_ui(HashMap<String, Double> H_Map) {

        Log.i("Size of H_Map ", String.valueOf(H_Map.size()));

        ArrayList<String> H_Img_URLs = new ArrayList<String>();
        ArrayList<String> H_Img_Dis = new ArrayList<String>();
        for(String key : H_Map.keySet()){

                  H_Img_URLs.add(key);
            }

        Log.i("Size of Img ArrayList ", String.valueOf(H_Img_URLs.size()));
        for(int a=0;a<5;a++)
        {
//            Log.i("ArrayList Elements "+a, H_Img_URLs.get(a));
        }
        for (Map.Entry<String,Double> entry : H_Map.entrySet()) {
            Log.i("ArrayList Elements ", entry.getValue().toString());
            H_Img_Dis.add( entry.getValue().toString());
        }

            Picasso.with(this).load(H_Img_URLs.get(0))
                    .error(R.mipmap.ic_launcher)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(IV_Usr_1);

            Picasso.with(this).load(H_Img_URLs.get(1))
                    .error(R.mipmap.ic_launcher)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(IV_Usr_2);

            Picasso.with(this).load(H_Img_URLs.get(2))
                    .error(R.mipmap.ic_launcher)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(IV_Usr_3);

            Picasso.with(this).load(H_Img_URLs.get(3))
                    .error(R.mipmap.ic_launcher)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(IV_Usr_4);


            Picasso.with(this).load(H_Img_URLs.get(4))
                    .error(R.mipmap.ic_launcher)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(IV_Usr_5);

    }


    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        int EARTH_RADIUS_KM = 6371;
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLonRad = Math.toRadians(lon2 - lon1);

        return Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)) * EARTH_RADIUS_KM;
    }

  /*  public static double calculateDistance(double fromLatitude,double fromLongitude,double toLatitude,double toLongitude)
        {
            float results[] = new float[1];
            try {
                Location.distanceBetween(fromLatitude,fromLongitude, toLatitude, toLongitude, results);
            } catch (Exception e) {
                if (e != null)
                    e.printStackTrace();
            }

            int dist = (int) results[0];
            if(dist<=0)
                return 0D;

            DecimalFormat decimalFormat = new DecimalFormat("#.#####");
            results[0]/=100000D;
            String distance = decimalFormat.format(results[0]);
            double dis = Double.parseDouble(distance);
            return dis;
        }*/

        private void update_ui(ArrayList<String> Arr_Loc_Img_URL) {

            Picasso.with(this).load(Arr_Loc_Img_URL.get(0))
                    .error(R.mipmap.ic_launcher)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                     .into(IV_Usr_1);

            Picasso.with(this).load(Arr_Loc_Img_URL.get(1))
                    .error(R.mipmap.ic_launcher)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(IV_Usr_2);

            Picasso.with(this).load(Arr_Loc_Img_URL.get(2))
                    .error(R.mipmap.ic_launcher)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(IV_Usr_3);

            Picasso.with(this).load(Arr_Loc_Img_URL.get(3))
                    .error(R.mipmap.ic_launcher)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(IV_Usr_4);


            Picasso.with(this).load(Arr_Loc_Img_URL.get(4))
                    .error(R.mipmap.ic_launcher)
                    .resize(100, 100)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(IV_Usr_5);


          /* .into(new Target(){

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //((FeedListRowHolder_Serializable_Renting) feedListRowHolder).IV_Book_Thumbnail.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));
                            ((FeedListRowHolder_Books_Added_By_Usr) holder).IV_Book_Search_Thumbnail.setImageDrawable(new BitmapDrawable(mContext.getResources(), bitmap));
                        }
                    }

                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable) {
                        Log.d("TAG", "FAILED");
                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable) {
                        Log.d("TAG", "Prepare Load");
                    }
                });*/

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(getApplicationContext(), "Permission Granted, Enjoy",Toast.LENGTH_SHORT).show();

                } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(getApplicationContext(), "Permission Denied",Toast.LENGTH_SHORT).show();
                        finish();
                }
                return;
            }

        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent I_Back = new Intent(getApplicationContext(), Act_custom_list.class);
        I_Back.setAction("Iamback");
        startActivity(I_Back);
        finish();
    }

    /*public HashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        HashMap sortedMap = new  HashMap();

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
    }*/

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

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }



    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String address = "", knownName= "", city="", state ="", country="", postalCode="";
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {

                address = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                knownName = addresses.get(0).getFeatureName();
            }
            else {
                Log.w("My Current address", "No Address returned!");
            }
            /*if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Cannot get Address!");
        }
        return address;

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Act_custom_list.class);
        myIntent.setAction("Iamback");
        startActivity(myIntent);
        finish();
        return true;

    }





}