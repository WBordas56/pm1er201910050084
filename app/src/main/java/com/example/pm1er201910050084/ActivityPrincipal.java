package com.example.pm1er201910050084;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder; //Importamos la clase para grabar video
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class ActivityPrincipal extends AppCompatActivity {

    private MediaRecorder grabacion;
    private String archivoSalida = null;
    private Button btn_grabar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btn_grabar = (Button) findViewById(R.id.btn_rec);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityPrincipal.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }
    }


    public void Grabar(View view){
        if(grabacion == null){
            archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Grabacion.mp3";
            grabacion = new MediaRecorder();
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
            grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            grabacion.setOutputFile(archivoSalida);

            try{
                grabacion.prepare();
                grabacion.start();
            }catch (Exception e){

            }

            Toast.makeText(this, "Grabando", Toast.LENGTH_SHORT).show();
        } else if(grabacion != null){
            grabacion.stop();
            grabacion.release();
            grabacion = null;
            Toast.makeText(this, "Grabacion Finalizada", Toast.LENGTH_SHORT).show();
        }
    } //fin de funcion grabar()

    public void Reproducir(View view) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(archivoSalida);
            mediaPlayer.prepare();
        }catch (Exception e){

        }

        mediaPlayer.start();
        Toast.makeText(this, "Reproduciendo audio", Toast.LENGTH_SHORT).show();

    } //fin de funcion reproducir()


}