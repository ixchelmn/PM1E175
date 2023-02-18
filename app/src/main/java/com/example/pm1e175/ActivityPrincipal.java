package com.example.pm1e175;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm1e175.configuraciones.SQLiteConexion;
import com.example.pm1e175.transacciones.Transacciones;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityPrincipal extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Globales

    static final int REQUEST_IMAGE = 101;

    static final int PETICION_ACCESS_CAM = 201;

    EditText nombre, numero;

    Button btn_ingresar_contacto, btn_contactos_ingresados;

    ImageButton btn_foto;

    TextView nota ;

    Spinner combopais;

    ImageView avatar;

    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        nombre = (EditText) findViewById(R.id.nombre);
        numero = (EditText) findViewById(R.id.numero);
        btn_ingresar_contacto = (Button) findViewById(R.id.btn_actualizar_contacto);
        btn_contactos_ingresados = (Button) findViewById(R.id.btn_contactos_ingresados);
        btn_foto = (ImageButton) findViewById(R.id.btn_foto);
        nota = (TextView) findViewById(R.id.nota);
        combopais =(Spinner) findViewById(R.id.combopais);
        avatar = (ImageView) findViewById(R.id.avatar);

        btn_ingresar_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {IngresarContacto();}
        });

        btn_contactos_ingresados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityListView.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this,R.array.cod_pais, android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        combopais.setAdapter(adp);
        combopais.setOnItemSelectedListener(this);

        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE)
        {

            try {
                File foto = new File(currentPhotoPath);
                avatar.setImageURI(Uri.fromFile(foto));
            }
            catch (Exception ex)
            {
                ex.toString();
            }
        }
    }

    private void IngresarContacto(){
        try {
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put("nombre", nombre.getText().toString());
            valores.put("combopais", combopais.getSelectedItem().toString());
            valores.put("numero", numero.getText().toString());
            valores.put("nota", nota.getText().toString());



            Long Resultado = db.insert(Transacciones.tablacontactos, "id", valores);
            Toast.makeText(this, Resultado.toString(), Toast.LENGTH_SHORT).show();

            ClearScreen();
        }catch (Exception ex)
        {
            Toast.makeText(this,"No se pudo ingresar el contacto",Toast.LENGTH_LONG).show();
        }
    }

    private void permisos(){
        //metodo para obtener permisos
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PETICION_ACCESS_CAM);
        }
        else {
            dispatchTakePictureIntent();

        }
    }

    private void TomarFoto()
    {
        Intent intent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(intent,REQUEST_IMAGE);
        }
    }
    private void ClearScreen()
    {
        nombre.setText(Transacciones.Empty);
        numero.setText(Transacciones.Empty);
        nota.setText(Transacciones.Empty);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.toString();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.pm1e175.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE);
            }
        }
    }
}