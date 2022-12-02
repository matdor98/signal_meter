package proyecto.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> implements View.OnClickListener {

    ArrayList<String> listDatos;
    private View.OnClickListener listener;
    public AdapterDatos(ArrayList<String> listDatos) {
        this.listDatos = listDatos;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView dato, inten,lat,lng;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            dato= (TextView) itemView.findViewById(R.id.idDato);
            inten= (TextView) itemView.findViewById(R.id.inten);
            lat= (TextView) itemView.findViewById(R.id.txtlat);
            lng= (TextView) itemView.findViewById(R.id.txtlng);
        }

        public void asignarDatos(String datos) {
            String[] d2 = datos.split(",");
            dato.setText(d2[0]);
            inten.setText(d2[3]);
            lat.setText("Latitud:"+d2[1]);
            lng.setText("Longitud:"+d2[2]);
        }
    }
}
