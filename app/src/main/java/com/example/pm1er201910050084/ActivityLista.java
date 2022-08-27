package com.example.pm1er201910050084;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityLista extends AppCompatActivity {

    Button btnactualizar, btneliminar, btnllamar;
    EditText txtlatitud,txtlongitud,txtdescripcion,txttelefono,txtnombre;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private List<Contactos> contactosList = new ArrayList<Contactos>();
    ArrayAdapter<Contactos> contactosArrayAdapter;
    ListView DatosContacto;

    private static final int REQUEST_CALL=1;

    Contactos contacSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        DatosContacto =(ListView) findViewById(R.id.listaContactos);

        txtlatitud = (EditText) findViewById(R.id.txt_latitud);
        txtlongitud = (EditText) findViewById(R.id.txt_longitud);
        txtdescripcion = (EditText) findViewById(R.id.txt_descripcion);
        txtnombre = (EditText) findViewById(R.id.txt_nombre);
        txttelefono = (EditText) findViewById(R.id.txt_telefono);
        btnactualizar = (Button) findViewById(R.id.btn_actualizar);
        btneliminar = (Button) findViewById(R.id.btn_eliminar);
        btnllamar = (Button) findViewById(R.id.btn_llamar);

        iniciarFirebase();
        listaDatos();

        DatosContacto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contacSelected = (Contactos) parent.getItemAtPosition(position);
                txtlatitud.setText(contacSelected.getLatitud());
                txtlongitud.setText(contacSelected.getLongitud());
                txtdescripcion.setText(contacSelected.getDireccion());
                txtnombre.setText(contacSelected.getNombre());
                txttelefono.setText(contacSelected.getTelefono());

            }
        });

        btnactualizar.setOnClickListener(this::onClickActualizar);
        btneliminar.setOnClickListener(this::onClickEliminar);
        btnllamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLlamar();
            }
        });

    }// fin de metodo onCreate

    private void onClickLlamar() {
        String numero = txttelefono.getText().toString();

        if (numero.trim().length() > 0){

            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CALL_PHONE
                },REQUEST_CALL);
            }else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                alerta.setMessage("¿Desea llamar a " + txtnombre.getText().toString() + "?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.cancel();

                                String dial = "tel:" + numero;
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Atencion");
                titulo.show();


            }

        } else{
            Toast.makeText(this, "Debe seleccionar un contacto", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onClickLlamar();
            }else{
                Toast.makeText(this,"Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void onClickActualizar(View view) {
        Contactos c = new Contactos();
        c.setUid(contacSelected.getUid());
        c.setLatitud(txtlatitud.getText().toString().trim());
        c.setLongitud(txtlongitud.getText().toString().trim());
        c.setDireccion(txtdescripcion.getText().toString().trim());
        c.setNombre(txtnombre.getText().toString().trim());
        c.setTelefono(txttelefono.getText().toString().trim());
        databaseReference.child("Contactos").child(c.getUid()).setValue(c);
        Toast.makeText(this, "Contacto Actualizado", Toast.LENGTH_SHORT).show();
        LimpiarCajasTexto();
    }

    private void onClickEliminar(View view) {
        Contactos c = new Contactos();
        c.setUid(contacSelected.getUid());
        databaseReference.child("Contactos").child(c.getUid()).removeValue();
        Toast.makeText(this, "Contacto Eliminado", Toast.LENGTH_SHORT).show();
        LimpiarCajasTexto();
    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void LimpiarCajasTexto() {
        txtlatitud.setText("");
        txtlongitud.setText("");
        txtdescripcion.setText("");
        txtnombre.setText("");
        txttelefono.setText("");
    }

    private void listaDatos() {
        databaseReference.child("Contactos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactosList.clear();

                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Contactos contactos = objSnaptshot.getValue(Contactos.class);
                    contactosList.add(contactos);

                    contactosArrayAdapter = new ArrayAdapter<Contactos>(ActivityLista.this, android.R.layout.simple_list_item_1, contactosList);
                    DatosContacto.setAdapter(contactosArrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}