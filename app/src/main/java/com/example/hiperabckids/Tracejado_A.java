package com.example.hiperabckids;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Tracejado_A extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    public static boolean path1Completed = false;
    public static boolean path2Completed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tracejado_a);

        mediaPlayer = MediaPlayer.create(this, R.raw.audio_cubra_letra);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Se o áudio ainda estiver tocando, liberar recursos ao destruir a Activity
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void VoltarMenu(View view) {
        Intent in = new Intent(this, Tela_menu.class);
        startActivity(in);
    }
}