package com.example.osc_client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {

    private ListView lvPlatos;
    private List<Plato> listaPlatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        listaPlatos = new ArrayList<Plato>();
        lvPlatos = findViewById(R.id.lvPlatos);
    }



    private void LlenarListaObjetos() {
        listaPlatos.add(new Plato(new Long(1), "A1-02", ""));
        listaPlatos.add(new Plato(new Long(1), "A2-02", ""));
        listaPlatos.add(new Plato(new Long(1), "A3-02", ""));
        listaPlatos.add(new Plato(new Long(1), "A4-02", ""));
    }
}
