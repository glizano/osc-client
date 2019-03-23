package com.example.osc_client;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FragmentMenu extends Fragment {

    private ListView lvMenu;
    private List<Plato> platos;
    private ArrayAdapter<Plato> adapter;
    private List<String> ids;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;


    public FragmentMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        lvMenu = view.findViewById(R.id.lvMenu);
        platos = new ArrayList<>();
        ids = new ArrayList<>();
        adapter = new MyListAdapter();
        lvMenu.setAdapter(adapter);

        addChildEventListener();

        RegistrarClicks();

        registerForContextMenu(lvMenu);

        return view;
    }

    private void addChildEventListener() {
        mDatabase.child("platos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Plato p = dataSnapshot.getValue(Plato.class);
                p.setId(dataSnapshot.getKey());
                ids.add(dataSnapshot.getKey());
                adapter.add(p);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Plato p = dataSnapshot.getValue(Plato.class);
                p.setId(dataSnapshot.getKey());
                int index = ids.indexOf(dataSnapshot.getKey());
                if (index != -1) {
                    Plato pc = platos.get(index);
                    platos.remove(pc);
                    platos.add(index, p);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int index = ids.indexOf(dataSnapshot.getKey());
                if (index != -1) {
                    platos.remove(index);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Plato p = platos.get(info.position);
        switch (item.getItemId()) {
            case 1:
                Intent intento = new Intent(getActivity(),
                        AgregarProducto.class);
                Bundle paquete = new Bundle();
                paquete.putString("id", p.getId());
                paquete.putString("img", p.getImg());
                paquete.putString("titulo", p.getTitulo());
                paquete.putString("descripcion", p.getDescripcion());
                paquete.putLong("precio",p.getPrecio());
                intento.putExtras(paquete);
                startActivity(intento);
                break;

            case 2:
                Mensaje("Funcion por realizar");
                break;

            default:
                Mensaje("No clasificado");
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Administrar Producto");
        menu.add(0, 1, 0, "Editar");
        menu.add(0, 2, 0, "Borrar");

    }


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
//            cargarImagen(itemView, platoActual.getImg());
            ImageView ivPlato = itemView.findViewById(R.id.ivPlato);
            Picasso.get()
                    .load(platoActual.getImg())
                    .error(R.drawable.ic_launcher_foreground)
                    .into(ivPlato);
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
