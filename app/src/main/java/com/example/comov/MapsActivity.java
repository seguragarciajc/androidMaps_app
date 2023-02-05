package com.example.comov;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.example.comov.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_LOCATION_PERMISSION_FINE = 1;
    private static final int READ_PHONE_REQUEST_CODE = 2;
    private static String FILENAME;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private List<Etapa> etapas = new LinkedList<Etapa>();

    private Etapa etapaActual =  new Etapa();
    private List<Marca> marcasEtapa= new LinkedList<Marca>();

    TelephonyManager telephonyManager;
    Location location = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String valor = getIntent().getStringExtra("nombreMapa");
        FILENAME = valor + ".json";
        etapas.add(etapaActual);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location1 : locationResult.getLocations()) {
                    location = location1;
                    LatLng lat = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
                }
            }
        };
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Si no se ha recibido permiso hacemos una peticion al usuario
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Habria que mostrar la razon por la que el permiso se esta pidiendo(si es necesario)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_PERMISSION_FINE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, MY_LOCATION_PERMISSION_FINE);
            }
            return;
        }
        fusedLocationClient.requestLocationUpdates(LocationHelper.createLocationRequest(), locationCallback, null);
        mMap.setMyLocationEnabled(true);

    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Esta funcion procesa las respuestas de los usuarios a las peticiones de permisos
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_LOCATION_PERMISSION_FINE: {
                //If request is cancelled, the result arrays are empty
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //getLastLocation(fusedLocationClient, this);
                    startLocationUpdates();
                } else {
                    //No nos concede permiso, habria que desactivar la funcionalidad que depende de el
                }
                break;
            }
        }
    }

    public void searchNet(View v) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION}, READ_PHONE_REQUEST_CODE);
        } else {
            StringBuilder text = new StringBuilder();
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission")
            List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
            text.append("Found ").append(cellInfoList.size()).append(" cells\n");
            for (CellInfo info : cellInfoList) {
                if ((info instanceof CellInfoLte) && (info.isRegistered())) {

                    CellInfoLte cellInfoLte = (CellInfoLte) info;


                    Circle circle;
                    int level = cellInfoLte.getCellSignalStrength().getLevel();
                    int dbm = cellInfoLte.getCellSignalStrength().getDbm();
                    int asu = cellInfoLte.getCellSignalStrength().getAsuLevel();
                    int timingAdvance = cellInfoLte.getCellSignalStrength().getTimingAdvance();
                    String celda = cellInfoLte.getCellIdentity().toString();

                    if (level == 0){
                        circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                                .radius(20)
                                .strokeColor(Color.BLACK)
                                .fillColor(Color.BLACK));
                    } else if(level == 1){
                        circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                                .radius(20)
                                .strokeColor(Color.RED)
                                .fillColor(Color.RED));
                    } else if(level == 2){
                        circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                                .radius(20)
                                .strokeColor(Color.YELLOW)
                                .fillColor(Color.YELLOW));
                    } else if(level == 3){
                        circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                                .radius(20)
                                .strokeColor(Color.GREEN)
                                .fillColor(Color.GREEN));
                    } else {
                        circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                                .radius(20)
                                .strokeColor(Color.rgb(15, 120, 20))
                                .fillColor(Color.rgb(15, 120, 20)));
                    }
                    circle.setVisible(true);
                    Marca marca = new Marca(level, dbm, asu, timingAdvance, celda);
                    etapaActual = etapas.get(etapas.size()-1);
                    etapaActual.addLevel(level);
                    etapaActual.addDbm(dbm);
                    etapaActual.addAsu(asu);
                    etapaActual.addMarca(marca);
                    Button boton = findViewById(R.id.button_save);
                    boton.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    public void guardaInformacion(View v){
        etapaActual.calcularMediasYMax();
        JsonObject res = new JsonObject();
        JsonArray etapasArray = new JsonArray();

        int i = 0;
        for(Etapa e:etapas){
            JsonObject et = new JsonObject();

            etapasArray.add(e.toJson());
            etapasArray.add(et);
            i++;
        }
        res.add("Etapas",etapasArray);
        try {
            StorageHelper.saveStringToFile(FILENAME,res.toString(),this);
        } catch (IOException e) {
            Log.e("MapsActivity","Error saving file: ",e);
        }
    }

    public void hacerEtapa(View v){
        Toast toast = Toast.makeText(this, "Nueva etapa creada", Toast.LENGTH_SHORT);
        toast.show();
        etapaActual.calcularMediasYMax();
        creaEtapa();
    }


    public void creaEtapa(){
        Etapa etapa = new Etapa();
        etapas.add(etapa);
    }
}