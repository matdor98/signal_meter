package proyecto.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class guardarActivity extends AppCompatActivity {
    TextView latitude,longitude,intensidad,nombre;
    Button guardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar);

        recibirDatos();
        latitude.setVisibility(View.VISIBLE);
        longitude.setVisibility(View.VISIBLE);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarInfo();
            }
        });
    }
    private void recibirDatos() {
        Bundle extras = getIntent().getExtras();
        String d1 = extras.getString("latitud");
        String d2 = extras.getString("longitud");
        String d3 = extras.getString("intensidad");
        latitude = (TextView) findViewById(R.id.latitud);
        longitude = (TextView) findViewById(R.id.longitud);
        intensidad = (TextView) findViewById(R.id.intensidad);
        nombre = (TextView) findViewById(R.id.txtNombre);
        guardar = (Button) findViewById(R.id.btnguardar);
        latitude.setText(d1);
        longitude.setText(d2);
        intensidad.setText(d3);
    }

    private void guardarInfo(){
        String test = ""+nombre.getText();
        if(test.equals("")){
            Toast.makeText(this,"Pon un nombre",Toast.LENGTH_SHORT).show();
        }else {
            try {
                String txt = buscarGuardados();
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("ubicaciones.txt", Activity.MODE_PRIVATE));
                archivo.write(txt + nombre.getText() + "," + latitude.getText() + "," + longitude.getText() + "," + intensidad.getText());
                archivo.flush();
                archivo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "" + nombre.getText(), Toast.LENGTH_SHORT).show();
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
                String allUbicaciones = "";
                while (linea != null) {
                    allUbicaciones = allUbicaciones + linea + "\n";
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

}