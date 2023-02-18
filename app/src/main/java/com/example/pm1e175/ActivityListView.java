package com.example.pm1e175;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pm1e175.configuraciones.SQLiteConexion;
import com.example.pm1e175.transacciones.Contactos;
import com.example.pm1e175.transacciones.Transacciones;

import java.util.ArrayList;

public class ActivityListView extends AppCompatActivity {

    //Variables Globales

    SQLiteConexion conexion;

    ListView listView;

    ArrayList<Contactos> listacontactos;

    ArrayList<String> Arreglocontactos;

    Contactos currentuser;

    Button btn_actualizar,btn_eliminar,btn_compartir,btn_llamar;

    TextView instrucciones;

    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE};
    private static final int REQUEST_PHONE_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null,1);
        listView = (ListView) findViewById(R.id.listview);
        btn_actualizar = (Button) findViewById(R.id.btn_actualizar);
        btn_llamar = (Button) findViewById(R.id.btn_llamar);

        

        ObtenerListaContactos();

        ArrayAdapter adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arreglocontactos);
        listView.setAdapter(adp);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentuser = listacontactos.get(i);
            }
        });

        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( currentuser == null ) return;
                Intent intent = new Intent(getApplicationContext(),ActivityActualizar.class);
                intent.putExtra("contacto_actualizar",currentuser);
                startActivity(intent);
            }
        });
        btn_llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissions();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + currentuser.getNumero()));
                    startActivity(callIntent);
                }
            }
        }
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
        FillList();

    }

    private void FillList(){
        Arreglocontactos = new ArrayList<String>();
        for(int i = 0; i < listacontactos.size(); i++){
            Arreglocontactos.add(listacontactos.get(i).getId() + " | "+
                    listacontactos.get(i).getNombre() + " | " +
                    listacontactos.get(i).getCombopais() +
                    listacontactos.get(i).getNumero() + " | " +
                    listacontactos.get(i).getNota());

        }
    }

    private void permissions(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + currentuser.getNumero()));
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
        else
        {
            startActivity(callIntent);
        }
    }



}