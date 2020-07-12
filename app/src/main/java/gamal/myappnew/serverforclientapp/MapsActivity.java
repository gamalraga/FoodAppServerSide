package gamal.myappnew.serverforclientapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.Moduel.Request;
import gamal.myappnew.serverforclientapp.Moduel.ShippingInformation;
import gamal.myappnew.serverforclientapp.Remote.IGoCoordinates;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks ,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private GoogleMap mMap;
    private final static int PLAY_SERVIECS_REQUESTS=1000;
    private final static int LOCATION_SERVIECS_REQUESTS=1001;
    private Location mLastloaction;
    GoogleApiClient mgoogleApiClient;
    public static final PatternItem Dot=new Dot();
    public static final PatternItem DASH=new Dash(20);
    public static final PatternItem GAP=new Gap(20);
    public static final List<PatternItem> styleline= Arrays.asList(GAP,DASH);
    LocationRequest mlocationRequest;
    private static int UPDATE_INTERVAL=1500;
    private static int FAST_INTERVAL=5000;
    private static int DISPLAYCEMINT=10;


//IGoCoordinates mServices;
DatabaseReference request,shipperorder;
    LatLng currentlocation;
     String lat,lng;
    Marker shippingmaker;


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, 1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
       // mServices= Common.getGeoCodeServices();
        request= FirebaseDatabase.getInstance().getReference(Common.REQUEST);
        shipperorder=FirebaseDatabase.getInstance().getReference("ShippingOrders");

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                    ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                        , REQUEST_CODE_ASK_PERMISSIONS);

            } else {
                if (CheckPlayServiec()) {
                    buildGoogleApiClient();
                    CreateLocationRequests();
            }
                }
                 displayLocation();
            }
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    private void displayLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                    ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}
                        , REQUEST_CODE_ASK_PERMISSIONS);

            } else {

                mLastloaction=LocationServices.FusedLocationApi.getLastLocation(mgoogleApiClient);
                if (mLastloaction!=null)
                {
                    double latitiude=mLastloaction.getLatitude();
                    double longtitude=mLastloaction.getLongitude();
                     currentlocation=new LatLng(latitiude,longtitude);
                    mMap.addMarker(new MarkerOptions().position(currentlocation).title("Your location")
                            .snippet(" The Shop Owner "));
                    // current
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f));

                    trakinglocation();




                }else {
                }
            }
        }
    }

    private void drawroute(final LatLng latLng, final Request currentrequest) {
                Geocoder geocoder=new Geocoder(getApplicationContext());
                try {

                    List<Address> addressList=  geocoder.getFromLocationName(currentrequest.getAdress(), 1);
                    if (addressList!=null&&addressList.size()>0) {
                        Address address = addressList.get(0);
                        lat=String.valueOf(address.getLatitude());
                        lng=String.valueOf(address.getLongitude());
                        Log.i("jfkewjbkew","Done");
                    }else {
                        Log.i("jfkewjbkew","null");

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Log.i("jfkewjbkew","ERROR");
                }



        LatLng orderlocation=new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
//                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.person);
//                    bitmap=Common.scalesBitmap(bitmap,70,70);
        //.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        MarkerOptions marker=new MarkerOptions()
                .title("Order of "+currentrequest.getPhone())
                .snippet("Name is :"+currentrequest.getName())
                .position(orderlocation);

        mMap.addMarker(marker);
//        mServices.getDirections(latLng.latitude+","+latLng.longitude,
//                orderlocation.latitude+","+orderlocation.longitude).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                new ParserTask().execute(response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
    }
  private class ParserTask extends AsyncTask<String,Integer,List<List<HashMap<String,String>>>>
  {
      ProgressDialog progressDialog=new ProgressDialog(getApplicationContext());

      @Override
      protected void onPreExecute() {
          super.onPreExecute();
          progressDialog.setMessage("Waiting....");
//          progressDialog.show();
      }

      @Override
      protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

        JSONObject jsonObject;
        List<List<HashMap<String,String>>> roue=null;
        try {
            jsonObject=new JSONObject(strings[0]);
            DirectionJSONParser parser=new DirectionJSONParser();
            roue=parser.parse(jsonObject);

        }catch (Exception e)
        {
e.printStackTrace();
        }
        return roue;
      }

      @Override
      protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
          super.onPostExecute(lists);
          ArrayList points=null;
          PolylineOptions polylineOptions=null;
          for (int i=0;i<lists.size();i++)
          {
              points=new ArrayList();
              polylineOptions=new PolylineOptions();
              List<HashMap<String,String>> path=lists.get(i);
              for (int j=0;j<path.size();j++)
              {
                  HashMap<String,String> point=path.get(j);
                  double lat=Double.parseDouble(point.get("lat"));
                  double lng=Double.parseDouble(point.get("lng"));
                  LatLng latLng=new LatLng(lat,lng);
                  points.add(latLng);
              }
              polylineOptions.addAll(points);
              polylineOptions.width(12);
              polylineOptions.color(Color.BLUE);
              polylineOptions.geodesic(true);
          }
          mMap.addPolyline(polylineOptions);
      }
  }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        boolean issuccess=mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.uber_style));
        if(!issuccess)
            Log.i("ERROR","Error in stylr");



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startlocationUpdates();
    }

    private void startlocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                    ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                return;

            }
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleApiClient,mlocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mgoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public void onLocationChanged(Location location) {
        mLastloaction=location;
        displayLocation();

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults.length>0) {
                    if (CheckPlayServiec())
                    {
                        buildGoogleApiClient();
                        CreateLocationRequests();
                        displayLocation();
                    }

                } else {
                    // Permission Denied
                    Toast.makeText(this, "Can't Access your location", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void CreateLocationRequests() {
        mlocationRequest=new LocationRequest();
        mlocationRequest.setInterval(UPDATE_INTERVAL);
        mlocationRequest.setFastestInterval(FAST_INTERVAL);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setSmallestDisplacement(DISPLAYCEMINT);
    }

    protected synchronized void buildGoogleApiClient() {
        mgoogleApiClient=new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API).build();
        mgoogleApiClient.connect();

    }

    private boolean CheckPlayServiec() {
        int resualtcode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resualtcode!=ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resualtcode)) {
                GooglePlayServicesUtil.getErrorDialog(resualtcode, this, PLAY_SERVIECS_REQUESTS).show();
            } else {
                Toast.makeText(this, "This device not support ", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckPlayServiec();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayLocationSettingsRequest(getApplicationContext());
        if (mgoogleApiClient!=null)
        {
            mgoogleApiClient.connect();
        }
    }
    private void trakinglocation() {
        request.child(Common.CURRENTREQUEST.getRequest_id())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       Request current_request=snapshot.getValue(Request.class);
                        Geocoder geocoder=new Geocoder(getApplicationContext());
                        try {

                            List<Address> addressList=  geocoder.getFromLocationName(current_request.getAdress(), 1);
                            if (addressList!=null&&addressList.size()>0) {
                                Address address = addressList.get(0);
                              final LatLng  locationrequest=new LatLng(address.getLatitude(),address.getLongitude());
                                MarkerOptions marker=new MarkerOptions()
                                        .title("location of order")
                                        .snippet("Name is :"+current_request.getName())
                                        .position(locationrequest)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mMap.addMarker(marker);
                                shipperorder.child(Common.CURRENTREQUEST.getRequest_id())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    ShippingInformation shippingInformation = snapshot.getValue(ShippingInformation.class);
                                                    LatLng locationofshipper = new LatLng(shippingInformation.getLat(), shippingInformation.getLng());
                                                    if (shippingmaker == null) {
                                                        shippingmaker = mMap.addMarker(new MarkerOptions()
                                                                .title("Shipper")
                                                                .snippet("Name is :" + shippingInformation.getName())
                                                                .position(locationofshipper)
                                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                                                        ;

                                                    } else {
                                                        shippingmaker.setPosition(locationofshipper);
                                                    }
                                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                                            .target(locationofshipper)
                                                            .zoom(8)
                                                            .bearing(0)
                                                            .tilt(45).build();
                                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                                    Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                                            .add(locationofshipper, locationrequest)
                                                            .width(5)
                                                            .color(Color.YELLOW));



                                                    Polyline polyline1=mMap.addPolyline(new PolylineOptions()
                                                            .add(locationofshipper,currentlocation)
                                                            .width(5)
                                                            .pattern(styleline)
                                                            .color(Color.BLUE));

                                                }else {
                                                    Toast.makeText(MapsActivity.this, "Shipper not move", Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                Log.i("jfkewjbkew","Done");
                            }else {
                                Log.i("jfkewjbkew","null");

                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            Log.i("jfkewjbkew","ERROR");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
