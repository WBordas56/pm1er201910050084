package com.example.pm1er201910050084;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class ActivityPrincipal extends AppCompatActivity {

    Contactos contactos = new Contactos();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Button btnguardar, btnlistado,btntomarft,btnseleccionarft;
    EditText txtlatitud,txtlongitud,txtdescripcion,txttelefono,txtnombre;
    ImageView ivfoto,btngrabar,btnreproducir,btnubicacion;


    Bitmap imagen;

    static final int RESULT_GALLERY_IMG = 200;
    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;

    public static String latitud = "";
    public static String longitud = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //validacion de camara - permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {

        }

        btnubicacion = findViewById(R.id.btn_ubicacion);
        ivfoto = (ImageView) findViewById(R.id.Foto);
        btntomarft = (Button) findViewById(R.id.btn_tomar_foto);
        btnseleccionarft = (Button) findViewById(R.id.btn_seleccionar_foto);
        btngrabar = (ImageView) findViewById(R.id.btn_grabar);
        btnreproducir = (ImageView) findViewById(R.id.btn_reproducir);
        txtlatitud = (EditText) findViewById(R.id.txt_latitud);
        txtlongitud = (EditText) findViewById(R.id.txt_longitud);
        txtdescripcion = (EditText) findViewById(R.id.txt_descripcion);
        txtnombre = (EditText) findViewById(R.id.txt_nombre);
        txttelefono = (EditText) findViewById(R.id.txt_telefono);
        btnguardar = (Button) findViewById(R.id.btn_actualizar);
        btnlistado = (Button) findViewById(R.id.btn_eliminar);

        iniciarFirebase();

        btntomarft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });

        btnseleccionarft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GaleriaImagenes();
            }
        });

        //validacion de ubicacion - permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Guardar();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Debe de tomarse una foto con ALERT DIALOG",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationStart();
            }
        });

        btnlistado.setOnClickListener(this::verLista);
    } // fin OnCreate

    private void verLista(View view) {
        Intent intent = new Intent(this, ActivityLista.class);
        startActivity(intent);
    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Guardar
    private void Guardar() {
        String LATITUD = txtlatitud.getText().toString();
        String LONGITUD = txtlongitud.getText().toString();
        String DIRECCION = txtdescripcion.getText().toString();
        String NOMBRE = txtnombre.getText().toString();
        String TELEFONO = txttelefono.getText().toString();
        String fotoString = GetStringImage(imagen);

        if(fotoString.equals("") || LATITUD.equals("")||LONGITUD.equals("")|| DIRECCION.equals("")||NOMBRE.equals("")||TELEFONO.equals("")){
            Validar();
        }
        else {
            Toast.makeText(this, "Contacto Guardado", Toast.LENGTH_SHORT).show();

            contactos.setUid(UUID.randomUUID().toString());
            contactos.setFoto(fotoString);
            contactos.setLatitud(LATITUD);
            contactos.setLongitud(LONGITUD);
            contactos.setDireccion(DIRECCION);
            contactos.setNombre(NOMBRE);
            contactos.setTelefono(TELEFONO);
            databaseReference.child("Contactos").child(contactos.getUid()).setValue(contactos);
            Toast.makeText(this, "Medicamento Guardado", Toast.LENGTH_SHORT).show();
            LimpiarCajasTexto();
        }
    }

    private void Validar() {
        String LATITUD = txtlatitud.getText().toString();
        String LONGITUD = txtlongitud.getText().toString();
        String DIRECCION = txtdescripcion.getText().toString();
        String NOMBRE = txtnombre.getText().toString();
        String TELEFONO = txttelefono.getText().toString();
        String fotoString = GetStringImage(imagen);


        if(fotoString.equals("")){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setMessage("Debe tomar una foto.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Alerta");
            titulo.show();

        }
        else if ( LATITUD.equals("") && LONGITUD.equals("")){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setMessage("GPS no esta activo.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Alerta");
            titulo.show();
        }
        else if (DIRECCION.equals("")){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setMessage("Debe describir la ubicación. ¡Es su direccion actual!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Alerta");
            titulo.show();

            txtdescripcion.setError("Required");
        }
        else if(NOMBRE.equals("")){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setMessage("Debe escribir almenos un nombre.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Alerta");
            titulo.show();

            txtnombre.setError("Required");
        }
        else if (TELEFONO.equals("")){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setMessage("Debe escribir almenos un telefono valido.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Alerta");
            titulo.show();

            txttelefono.setError("Required");
        }


    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Permisos

    private void permisos() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_ACCESO_CAM);
        }else{
            tomarFoto();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtener Foto

    private void tomarFoto() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic,TAKE_PIC_REQUEST);
        }
    }

    private void GaleriaImagenes() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, RESULT_GALLERY_IMG);
    }

    private String GetStringImage(Bitmap photo) {

        try {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 70, ba);
            byte[] imagebyte = ba.toByteArray();
            String encode = Base64.encodeToString(imagebyte, Base64.DEFAULT);

            return encode;
        }catch (Exception ex)
        {
            ex.toString();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri;
        //obtener la imagen por el almacenamiento interno
        if(resultCode==RESULT_OK && requestCode==RESULT_GALLERY_IMG)
        {

            imageUri = data.getData();
            ivfoto.setImageURI(imageUri);
            try {
                imagen=MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);

            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Error al seleccionar imagen", Toast.LENGTH_SHORT).show();
            }
        }
        //obtener la imagen por la camara
        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            ivfoto.setImageBitmap(imagen);
        }


    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Obtener Ubicacion

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

    }

    public class Localizacion implements LocationListener{

        ActivityPrincipal mainActivity;

        public void setMainActivity(ActivityPrincipal mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            location.getLatitude();
            location.getLongitude();

            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + location.getLatitude() + "\n Long = " + location.getLongitude();


            ActivityPrincipal.setLatitud(location.getLatitude()+"");
            ActivityPrincipal.setLongitud(location.getLongitude()+"");
            txtlatitud.setText(location.getLatitude()+"");
            txtlongitud.setText(location.getLongitude()+"");
            this.mainActivity.setLocation(location);
        }
    }

    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void setLatitud(String latitud) {
        ActivityPrincipal.latitud = latitud;
    }

    public static void setLongitud(String longitud) {
        ActivityPrincipal.longitud = longitud;
    }



    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // limpieza de Edittext


    private void LimpiarCajasTexto() {
        txtlatitud.setText("");
        txtlongitud.setText("");
        txtdescripcion.setText("");
        txtnombre.setText("");
        txttelefono.setText("");
        ivfoto.setImageBitmap(null);
    }

}