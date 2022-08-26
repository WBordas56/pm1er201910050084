package com.example.pm1er201910050084;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaRecorder; //Importamos la clase para grabar video
import android.os.Bundle;
import android.widget.Button;

public class ActivityPrincipal extends AppCompatActivity {

    private MediaRecorder grabacion;
    private String archivoSalida = null;
    private Button btn_grabar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btn_grabar = (Button) findViewById(R.id.btn_rec);
    }
}