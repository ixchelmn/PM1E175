package com.example.pm1e175;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm1e175.configuraciones.SQLiteConexion;
import com.example.pm1e175.transacciones.Contactos;
import com.example.pm1e175.transacciones.Transacciones;

import java.util.ArrayList;

public class ActivityActualizar extends AppCompatActivity {

    //Variables Globales

    SQLiteConexion conexion;

    Button btn_actualizar_contacto,btn_contactos_ingresados;

    EditText nombre, numero;

    Spinner combopais;

    TextView nota;

    Contactos contacto;

    ArrayList<Contactos> listacontactos;

    ArrayList<String> Arreglocontactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);
        nombre = (EditText) findViewById(R.id.nombre);
        numero = (EditText) findViewById(R.id.numero);
        nota = (TextView) findViewById(R.id.nota);
        combopais =(Spinner) findViewById(R.id.combopais);
        btn_actualizar_contacto = (Button) findViewById(R.id.btn_actualizar_contacto);
        btn_contactos_ingresados = (Button) findViewById(R.id.btn_contactos_ingresados);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null,1);

        ObtenerListaContactos();

        Intent intent = getIntent();
        contacto = (Contactos) intent.getSerializableExtra("contacto_actualizar");

        String x = contacto.getNombre();

        nombre.setText(contacto.getNombre());
        numero.setText(Integer.toString(contacto.getNumero()));
        String nota_text = contacto.getNota();
        if(nota_text == null)
            nota_text = "";
        nota.setText(nota_text);
        combopais.setSelection(((ArrayAdapter<String>)combopais.getAdapter()).getPosition(contacto.getCombopais()));

        btn_actualizar_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {ActualizarContacto();}
        });

        btn_contactos_ingresados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityListView.class);
                startActivity(intent);
            }
        });


    }

    private void ObtenerListaContactos(){

        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contact = null;
        listacontactos = new ArrayList<Contactos>();

        //Cursor
        Cursor cursor = db.rawQuery("SELECT * FROM contactos", null);

        while(cursor.moveToNext()){
            contact = new Contactos();
            contact.setId(cursor.getInt(0));
            contact.setNombre(cursor.getString(1));
            contact.setCombopais(cursor.getString(2));
            contact.setNumero(cursor.getInt(3));
            contact.setNota(cursor.getString(4));


            listacontactos.add(contact);

        }

        cursor.close();


    }

    private void ActualizarContacto(){
        try {
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put("nombre", nombre.getText().toString());
            valores.put("combopais", combopais.getSelectedItem().toString());
            valores.put("numero", numero.getText().toString());
            valores.put("nota", nota.getText().toString());

            String[] args = new String[]{Integer.toString(contacto.getId())};
            db.update(Transacciones.tablacontactos, valores, "id=?", args);
            Intent intent = new Intent(getApplicationContext(),ActivityListView.class);
            startActivity(intent);


        }catch (Exception ex)
        {
            Toast.makeText(this,"No se pudo ingresar el contacto",Toast.LENGTH_LONG).show();
        }
    }

}