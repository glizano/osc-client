package com.example.osc_client;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FragmentMenu extends Fragment {

    ListView lvMenu;
    List<Plato> platos;
    List<Uri> urlImagenesPlatos;

    private StorageReference mStorageRef;

    public FragmentMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        lvMenu = view.findViewById(R.id.lvMenu);
        platos = new ArrayList<>();
        urlImagenesPlatos = new ArrayList<>();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        auxLlenarPlatos();
        LlenarListView();
        RegistrarClicks();


        return view;
    }

    public void cargarImagen(View itemView, String ruta) {
        final ImageView ivPlato = (ImageView) itemView.findViewById(R.id.ivPlato);
        StorageReference imgRef = mStorageRef.child("uploads/" + ruta);
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri.toString())
                        .error(R.drawable.ic_launcher_foreground)
                        .into(ivPlato);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


    }

    public void auxLlenarPlatos() {
        platos.add(new Plato(new Long(1), "Casado", "Esto es un casado....", "casado.png", new Double(1500)));
        platos.add(new Plato(new Long(2), "Pollo frito", "Delicioso pollo frito...", "pollo.jpg", new Double(1000)));
        platos.add(new Plato(new Long(3), "Mondongo", "Sopa de mondongo con mucho mondongo...", "sopa.jpg", new Double(750)));
        platos.add(new Plato(new Long(4), "Plato 4", "Descripcion de plato 4", "", new Double(50)));

    }

    private void LlenarListView() {
        ArrayAdapter<Plato> adapter = new MyListAdapter();
        lvMenu.setAdapter(adapter);
    }

    private void RegistrarClicks() {
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {
                Plato platoEscogido = platos.get(position);
                String message = "Elegiste item No.  " + (1 + position)
                        + " que es un objeto cuyo primer atributo es  ";
                Mensaje(message);
            }
        });
    }

    public void Mensaje(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    ;

    private class MyListAdapter extends ArrayAdapter<Plato> {
        public MyListAdapter() {
            super(FragmentMenu.this.getActivity(), R.layout.plato, platos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = FragmentMenu.this.getActivity().getLayoutInflater().inflate(R.layout.plato, parent, false);
            }
            Plato platoActual = platos.get(position);
            // Fill the view
            cargarImagen(itemView, platoActual.getRutaFoto());
            TextView tvTitulo = itemView.findViewById(R.id.tvTituloPlato);
            tvTitulo.setText(platoActual.getTitulo());
            TextView tvDescripcion = itemView.findViewById(R.id.tvDescripcionPlato);
            tvDescripcion.setText(platoActual.getDescripcion());
            TextView tvPrecio = itemView.findViewById(R.id.tvPrecio);
            DecimalFormat df = new DecimalFormat("₡###,###.###");
            tvPrecio.setText(df.format(platoActual.getPrecio()));
            return itemView;
        }
    }
}
