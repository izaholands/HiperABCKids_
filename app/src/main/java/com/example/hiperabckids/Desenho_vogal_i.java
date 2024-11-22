package com.example.hiperabckids;

import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.HashMap;
import java.util.Map;

public class Desenho_vogal_i extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private Map<ImageButton, Integer> buttonSoundMap;
    private MediaPlayer mediaPlayerAuto; // Para reprodução automática
    // Variáveis para controlar o clique dos botões
    private boolean button1Clicked = false;
    private boolean button2Clicked = false;

    // Variáveis para controlar o fim da animação e áudio de cada botão
    private boolean button1AudioFinished = false;
    private boolean button2AudioFinished = false;
    private boolean button1DialogDismissed = false;
    private boolean button2DialogDismissed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desenho_vogal_i);

        constraintLayout = findViewById(R.id.main);

        // Inicializar o mapeamento de botões para arquivos de áudio
        buttonSoundMap = new HashMap<>();
        ImageButton button = findViewById(R.id.imgbutt2_a);
        ImageButton button2 = findViewById(R.id.imgbutt4_a);
        if (button != null) {
            buttonSoundMap.put(button, R.raw.audio_ilha);
        }
        if(button2 != null){
            buttonSoundMap.put(button2, R.raw.audio_igreja);
        }

        // Inicializar o MediaPlayer para reprodução automática
        mediaPlayerAuto = MediaPlayer.create(this, R.raw.audio_instrucao_i);
        if (mediaPlayerAuto != null) {
            mediaPlayerAuto.start();
        }

        // Método para configurar os listeners dos botões
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        for (Map.Entry<ImageButton, Integer> entry : buttonSoundMap.entrySet()) {
            ImageButton button = entry.getKey();
            int soundID = entry.getValue();

            // Listener para o clique do botão
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(Desenho_vogal_i.this, soundID);
                    if (mediaPlayer != null) {
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();

                                // Marcar que o áudio foi concluído
                                if (soundID == R.raw.audio_ilha) {
                                    button1AudioFinished = true;
                                } else if (soundID == R.raw.audio_igreja) {
                                    button2AudioFinished = true;
                                }

                                // Verificar se ambos áudios e diálogos foram concluídos
                                checkIfReadyForNextActivity();
                            }
                        });
                        mediaPlayer.start();
                    }

                    // Marcar o botão como clicado
                    if (soundID == R.raw.audio_ilha) {
                        button1Clicked = true;
                        showDialogWithAnimation1();
                    } else if (soundID == R.raw.audio_igreja) {
                        button2Clicked = true;
                        showDialogWithAnimation2();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerAuto != null) {
            mediaPlayerAuto.release();
            mediaPlayerAuto = null;
        }
    }
    private void checkIfReadyForNextActivity() {
        // Verifica se ambos os botões foram clicados, seus áudios finalizados e diálogos fechados
        if (button1Clicked && button2Clicked && button1AudioFinished && button2AudioFinished
                && button1DialogDismissed && button2DialogDismissed) {
            goToFinalActivity();
        }
    }

    private void goToFinalActivity() {
        Intent intent = new Intent(Desenho_vogal_i.this, Desenho_vogal_o.class); // Altere para sua próxima atividade
        startActivity(intent);
        finish(); // Para garantir que o usuário não possa voltar para esta tela
    }

    private void showDialogWithAnimation1() {
        // Lógica do botão 1 com animação e diálogo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View main = findViewById(R.id.main);
                AlertDialog.Builder builder = new AlertDialog.Builder(Desenho_vogal_i.this, R.style.CombinacaoDialog);
                View dialogView = getLayoutInflater().inflate(R.layout.comemoracao1_i, null);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(dialogInterface -> blur(main));
                dialog.setOnDismissListener(dialogInterface -> removerBlur(main));
                dialog.show();

                MediaPlayer md = MediaPlayer.create(Desenho_vogal_i.this, R.raw.audio1_parabens_i);
                md.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }, 5000);  // 5000ms = 5 segundos

                dialog.setOnDismissListener(dialogInterface -> {
                    if (md != null && md.isPlaying()) {
                        md.stop();
                        md.release();
                    }
                    removerBlur(main);
                    button1DialogDismissed = true; // Marcar que o diálogo foi fechado
                    checkIfReadyForNextActivity(); // Verificar novamente se é hora de ir para a próxima tela
                });

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        }, 600);
    }

    private void showDialogWithAnimation2() {
        // Lógica do botão 2 com animação e diálogo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View main = findViewById(R.id.main);
                AlertDialog.Builder builder = new AlertDialog.Builder(Desenho_vogal_i.this, R.style.CombinacaoDialog);
                View dialogView = getLayoutInflater().inflate(R.layout.comemoracao2_i, null);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(dialogInterface -> blur(main));
                dialog.setOnDismissListener(dialogInterface -> removerBlur(main));
                dialog.show();

                MediaPlayer md = MediaPlayer.create(Desenho_vogal_i.this, R.raw.audio2_parabens_i);
                md.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }, 5000);  // 5000ms = 5 segundos

                dialog.setOnDismissListener(dialogInterface -> {
                    if (md != null && md.isPlaying()) {
                        md.stop();
                        md.release();
                    }
                    removerBlur(main);
                    button2DialogDismissed = true; // Marcar que o diálogo foi fechado
                    checkIfReadyForNextActivity(); // Verificar novamente se é hora de ir para a próxima tela
                });

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        }, 600);
    }
    private void blur(View v) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            RenderEffect blurefeito = RenderEffect.createBlurEffect(20,20, Shader.TileMode.CLAMP);
            v.setRenderEffect(blurefeito);
        }
    }

    private void removerBlur(View v){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            v.setRenderEffect(null);
        }
    }

    public void Voltar_menu(View view) {
        Intent in = new Intent(Desenho_vogal_i.this, Tela_menu.class);
        startActivity(in);
    }
}
