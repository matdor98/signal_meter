package proyecto.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class registrosActivity extends AppCompatActivity {

    Button sincronizar;
    RecyclerView tabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        tabla =(RecyclerView) findViewById(R.id.tabla);
        sincronizar =(Button) findViewById(R.id.btnSubir);
        //tabla.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        tabla.setLayoutManager(new GridLayoutManager(this,1));
        buscarGuardados();

        sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sincronizar("http://10.0.0.23/prueba.php");
            }
        });

    }

    private void buscarGuardados(){
        String archivos[]=fileList();
        if(ArchivoExiste(archivos,"ubicaciones.txt")){
            try{
                InputStreamReader archivo = new InputStreamReader(openFileInput("ubicaciones.txt"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                ArrayList<String> listaDatos = new ArrayList<String>();
                while(linea!=null){

                    listaDatos.add(""+linea);
                    linea=br.readLine();
                }
                br.close();
                archivo.close();
                AdapterDatos adapter = new AdapterDatos(listaDatos);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = tabla.getChildAdapterPosition(view);
                        String[] datos=listaDatos.get(pos).split(",");
                        Intent i = new Intent(registrosActivity.this,mapasActivity.class);
                        i.putExtra("latitud",datos[1]);
                        i.putExtra("longitud",datos[2]);
                        i.putExtra("intensidad",datos[3]);
                        i.putExtra("ubicacion",datos[0]);
                        startActivity(i);

                    }
                });
                tabla.setAdapter(adapter);
            }catch (IOException e){

            }
        }
    }
    private boolean ArchivoExiste(String archivos [],String NombreArchivo){
        for(int i =0;i< archivos.length;i++){
            if(NombreArchivo.equals(archivos[i])){
                return true;
            }
        }
        return false;
    }
    private void sincronizar(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {

                }else{

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(registrosActivity.this,"fuera",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("dato1","prueba");
                return parametros;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}