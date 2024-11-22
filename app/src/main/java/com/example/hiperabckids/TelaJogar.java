package com.example.hiperabckids;

import android.media.MediaPlayer;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaJogar extends AppCompatActivity {
    //MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_jogar);


        /*inicio o mediaplay e toca o meu audio
        mediaPlayer = MediaPlayer.create(this, R.raw.som_iniciando_app);
        mediaPlayer.start();*/

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void telaOpcoes(View view) {
        Intent in = new Intent(TelaJogar.this, Tela_menu.class);
        startActivity(in); }
}