package proyecto.main;


import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    TelephonyManager telephonyManager;

    TextView longitude,latitude,dbs;
    Button getLocation,guardar, tablas,mapa;


    private final static int REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocation=findViewById(R.id.btn1);
        longitude=findViewById(R.id.longitud);
        latitude=findViewById(R.id.latitud);
        dbs=findViewById(R.id.dbs);
        guardar=findViewById(R.id.guardar);
        tablas=findViewById(R.id.vertabla);
        mapa=findViewById(R.id.mapa);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,mapasActivity.class);
                i.putExtra("latitud",latitude.getText());
                i.putExtra("longitud",longitude.getText());
                i.putExtra("intensidad",dbs.getText());
                startActivity(i);

            }
        });

        tablas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,registrosActivity.class);
                startActivity(i);
            }
        });

        getLocation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                getLastLocation();
                getDbs();
                latitude.setVisibility(View.VISIBLE);
                longitude.setVisibility(View.VISIBLE);
                guardar.setVisibility(View.VISIBLE);
                mapa.setVisibility(View.VISIBLE);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,guardarActivity.class);
                i.putExtra("latitud",latitude.getText());
                i.putExtra("longitud",longitude.getText());
                i.putExtra("intensidad",dbs.getText());
                startActivity(i);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void getDbs(){
        String texto = String.valueOf(telephonyManager.getSignalStrength());
        String[] texto2 = texto.split(" ");
        int num = 3;
        for(int i=0;i<texto2.length;i++){
            if(texto2[i].contains("rsrp")){
                dbs.setText(texto2[i].replaceAll("rsrp=","")+" dB");
                num = Integer.parseInt(texto2[i].replaceAll("rsrp=",""));
            }else if(texto2[i].contains("rscp")){
                dbs.setText(texto2[i].replaceAll("rscp=","")+" dB");
                num = Integer.parseInt(texto2[i].replaceAll("rscp=",""));
            }
        }
        if(num>=-80){
            dbs.setTextColor(Color.GREEN);
        }else if(num<-80 && num>=-90){
            dbs.setTextColor(Color.YELLOW);
        }else if(num<=-100){
            dbs.setTextColor(Color.RED);
        }else {
            dbs.setTextColor(Color.MAGENTA);
        }
    }

    private void getLastLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(Location location) {
                    if(location !=null){
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses= null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            latitude.setText(""+addresses.get(0).getLatitude());
                            longitude.setText(""+addresses.get(0).getLongitude());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }else{
            askPermission();
        }
    }

    private void askPermission(){
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE },REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else{
                Toast.makeText(this,"Requiere permisos",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean ArchivoExiste(String archivos [],String NombreArchivo){
        for(int i =0;i< archivos.length;i++){
            if(NombreArchivo.equals(archivos[i])){
                return true;
            }
        }
        return false;
    }
}