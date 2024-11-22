package com.example.hiperabckids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Tela_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_menu);

        /*continua o audio
        Som_app.tocarMusica(this);*/

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void Tela_alfabeto(View view) {

        /*pausa o audio quando seleciona um jogo
        Som_app.pausarMusica();*/


        Intent in = new Intent(Tela_menu.this, TelaAlfabeto.class);
        startActivity(in);
    }

    /*public void Tela_Traco_A(View view){
        Intent in= new Intent(Tela_menu.this, Tela_traco_letraA.class); startActivity(in);
    }*/
    public void Tela_Memoria(View view){

        /* pausa
        Som_app.pausarMusica();*/

        Intent in= new Intent(Tela_menu.this, Jogo_Memoria_Animais2.class);
        startActivity(in);
    }

    public void Tela_Traco_A(View view) {
        Intent in = new Intent(Tela_menu.this, Tracejado_A.class);
        startActivity(in);
    }

    public void Tela_Vogal(View view) {
        Intent in = new Intent(Tela_menu.this, Desenho_vogal_a.class);
        startActivity(in);
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        // Retomar o áudio quando voltar ao menu
        Som_app.tocarMusica(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausar o áudio quando sair do menu
        Som_app.pausarMusica();
    }*/

}