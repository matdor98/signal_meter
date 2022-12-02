package proyecto.main;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class mapasActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    GoogleMap mMap;
    String mapint;
    String nombre;
    TextView titulo;
    float maplong,maplat;
    Button borrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        titulo = findViewById(R.id.txtIntensidad);
        borrar = findViewById(R.id.btnBorrar);

        recibirDatos();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarDato();
            }
        });

    }

    private void recibirDatos() {
        Bundle extras = getIntent().getExtras();
        String d1 = extras.getString("latitud");
        String d2 = extras.getString("longitud");
        String d3 = extras.getString("intensidad");
        String d4 = ""+extras.getString("ubicacion");
        nombre=d4;
        if(d4.equals("null")){
            titulo.setText(d3);
        }else {
            titulo.setText(d4 + " " + d3);
        }
        maplong= Float.parseFloat(d2);
        maplat= Float.parseFloat(d1);
        mapint=d3;
    }


    private void borrarDato(){
        if(nombre.equals("null")){
            Toast.makeText(this,"El archivo no está guardado",Toast.LENGTH_SHORT).show();
        }else {
            try {
                String txt = buscarGuardados();
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("ubicaciones.txt", Activity.MODE_PRIVATE));
                archivo.write(txt);
                archivo.flush();
                archivo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, nombre+" eliminado", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

    }
    private String buscarGuardados(){
        String archivos[]=fileList();
        if(ArchivoExiste(archivos,"ubicaciones.txt")) {
            try {
                InputStreamReader archivo = new InputStreamReader(openFileInput("ubicaciones.txt"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String allUbicaciones ="";
                while (linea != null) {
                    if (!linea.contains(nombre)) {
                        allUbicaciones = allUbicaciones + linea + "\n";
                    }
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
                return allUbicaciones;
            } catch (IOException e) {

            }
        }
        return "";
    }
    private boolean ArchivoExiste(String archivos [],String NombreArchivo){
        for(int i =0;i< archivos.length;i++){
            if(NombreArchivo.equals(archivos[i])){
                return true;
            }
        }
        return false;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        LatLng mark1 = new LatLng(maplat, maplong);
        float color;

        int num = Integer.parseInt(mapint.split(" ")[0]);
        if(num>=-80){
            color = HUE_GREEN;
        }else if(num<-80 && num>=-90){
            color = HUE_YELLOW;
        }else if(num<=-100){
            color = HUE_RED;
        }else{
            color = HUE_VIOLET;
        }
        mMap.addMarker(new MarkerOptions().position(mark1).title(mapint).icon(BitmapDescriptorFactory.defaultMarker(color)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark1));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }
}