package com.example.hiperabckids;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class TelaAlfabeto extends AppCompatActivity {

    private Map<ImageButton, Integer> buttonSoundMap;
    // Mapeamento de botões para arquivos de áudio
    //declaração de um mapa que associa "ImageButton" a Ids de arquivos de audio
    //para reprodução do botão
    private MediaPlayer mediaPlayerAuto; // Para reprodução automática

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_alfabeto);

        // Inicializar o mapeamento de botões para arquivos de áudio
        //Cria o mapa associando cda botao ao id do audio correspondente
        buttonSoundMap = new HashMap<>();
        buttonSoundMap.put(findViewById(R.id.imageButtonA), R.raw.letra_a);
        buttonSoundMap.put(findViewById(R.id.imageButtonB), R.raw.letra_b);
        buttonSoundMap.put(findViewById(R.id.imageButtonC), R.raw.letra_c);
        buttonSoundMap.put(findViewById(R.id.imageButtonD), R.raw.letra_d);
        buttonSoundMap.put(findViewById(R.id.imageButtonE), R.raw.letra_e);
        buttonSoundMap.put(findViewById(R.id.imageButtonF), R.raw.letra_f);
        buttonSoundMap.put(findViewById(R.id.imageButtonG), R.raw.letra_g);
        buttonSoundMap.put(findViewById(R.id.imageButtonH), R.raw.letra_h);
        buttonSoundMap.put(findViewById(R.id.imageButtonI), R.raw.letra_i);
        buttonSoundMap.put(findViewById(R.id.imageButtonJ), R.raw.letra_j);
        buttonSoundMap.put(findViewById(R.id.imageButtonK), R.raw.letra_k);
        buttonSoundMap.put(findViewById(R.id.imageButtonL), R.raw.letra_l);
        buttonSoundMap.put(findViewById(R.id.imageButtonM), R.raw.letra_m);
        buttonSoundMap.put(findViewById(R.id.imageButtonN), R.raw.letra_n);
        buttonSoundMap.put(findViewById(R.id.imageButtonO), R.raw.letra_o);
        buttonSoundMap.put(findViewById(R.id.imageButtonP), R.raw.letra_p);
        buttonSoundMap.put(findViewById(R.id.imageButtonQ), R.raw.letra_q);
        buttonSoundMap.put(findViewById(R.id.imageButtonR), R.raw.letra_r);
        buttonSoundMap.put(findViewById(R.id.imageButtonS), R.raw.letra_s);
        buttonSoundMap.put(findViewById(R.id.imageButtonT), R.raw.letra_t);
        buttonSoundMap.put(findViewById(R.id.imageButtonU), R.raw.letra_u);
        buttonSoundMap.put(findViewById(R.id.imageButtonV), R.raw.letra_v);
        buttonSoundMap.put(findViewById(R.id.imageButtonW), R.raw.letra_w);
        buttonSoundMap.put(findViewById(R.id.imageButtonX), R.raw.letra_x);
        buttonSoundMap.put(findViewById(R.id.imageButtonY), R.raw.letra_y);
        buttonSoundMap.put(findViewById(R.id.imageButtonZ), R.raw.letra_z);



        // Inicializar o MediaPlayer para reprodução automática
        mediaPlayerAuto = MediaPlayer.create(this, R.raw.conheca_alfabeto);
        // Iniciar a reprodução automática
        if (mediaPlayerAuto != null) {
            mediaPlayerAuto.start();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //metodo para configurar os listeners dos botoes
        setupButtonListeners();
    }
    private void setupButtonListeners () {
        for (Map.Entry<ImageButton, Integer> entry : buttonSoundMap.entrySet()) { //"entry": significa entrada no mapa, cada entrada é uma chave e um valor

            // image button armazenado na variavel button para que seja possivel manipular o botao dentro do lop
            ImageButton button = entry.getKey(); //entrada do soundMap: chave é o imagebutton

            //o id foi armazenado na variavel soundID para acessar o id do arquivo do audio assoiado ao botao
            int soundID = entry.getValue(); //entrada do soundMaop: valor é o Integer que representa o ID do arquivo de audio associado ao botao

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //inicializar o audio associado ao botao
                    MediaPlayer mediaplayer = MediaPlayer.create(TelaAlfabeto.this, soundID); //"soundID": id do arquivo de audio associado ao botao
                    if(mediaplayer != null && !mediaplayer.isPlaying()){
                        mediaplayer.start();

                    }

                }

            });
        }
    }


    public void voltar_telaOpcoes (View view){
        Intent in = new Intent(TelaAlfabeto.this, Tela_menu.class);
        startActivity(in);
    }
    protected void onDestroy () {
        super.onDestroy();

        // Liberar recursos do MediaPlayer para reprodução automática
        if (mediaPlayerAuto != null) {
            mediaPlayerAuto.release();
            mediaPlayerAuto = null;
        }
    }
}