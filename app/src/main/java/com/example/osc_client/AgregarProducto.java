package com.example.osc_client;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class AgregarProducto extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;

    private EditText etTitulo;
    private EditText etDescripcion;
    private EditText etPrecio;
    private TextView tvImagen;
    private Button btnGuardar;
    private Button btnBuscarImagen;

    private Plato p;

    private Uri uriImagenSeleccionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        getSupportActionBar().setTitle("Administracion de productos");

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        p = new Plato();

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        tvImagen = findViewById(R.id.tvImagen);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnBuscarImagen = findViewById(R.id.btnBuscarImagen);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long precio = Long.parseLong(etPrecio.getText().toString());
                p.setPrecio(precio);
                p.setTitulo(etTitulo.getText().toString());
                p.setDescripcion(etDescripcion.getText().toString());

                if (tvImagen.getText() == "Imagen seleccionada") {
                    String img = System.currentTimeMillis() + "." + getFileExtension(uriImagenSeleccionada);

                    final StorageReference ref = mStorageRef.child(img);
                    ref.putFile(uriImagenSeleccionada).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                p.setImg(downloadUri.toString());
                                guaradarPlato();
                            }
                        }
                    });
                } else {
                    guaradarPlato();
                }

            }
        });

        btnBuscarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        cargarDatos();
    }

    private void guaradarPlato(){
        DatabaseReference refPlato = null;
        if (p.getId() == null)
            refPlato = mDatabase.child("platos").push();
        else{
            refPlato = mDatabase.child("platos").child(p.getId());
        }
        refPlato.setValue(p);

        Intent i = new Intent(getApplicationContext(), PanelNavegacion.class);
        startActivity(i);
    }

    private void cargarDatos() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            p.setTitulo(extras.getString("titulo"));
            p.setId(extras.getString("id"));
            p.setDescripcion(extras.getString("descripcion"));
            p.setImg(extras.getString("img"));
            p.setPrecio(extras.getLong("precio"));

            etTitulo.setText(p.getTitulo());
            etDescripcion.setText(p.getDescripcion());
            etPrecio.setText(p.getPrecio().toString());
            tvImagen.setText(p.getImg() != null ? "Imagen anterior" : "No hay imagen");
            uriImagenSeleccionada = Uri.parse(extras.getString("img"));
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione Imagen"), 1);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if(tvImagen.getText().toString().equals("Imagen anterior")){
                borrarImagen();
            }
            uriImagenSeleccionada = data.getData();
            tvImagen.setText("Imagen seleccionada");
        }
    }

    public void borrarImagen(){
        StorageReference r = FirebaseStorage.getInstance().getReferenceFromUrl(uriImagenSeleccionada.toString());
        r.delete();

    }

}
