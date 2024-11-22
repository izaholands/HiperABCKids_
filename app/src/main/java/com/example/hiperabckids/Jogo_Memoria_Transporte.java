package com.example.hiperabckids;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jogo_Memoria_Transporte extends AppCompatActivity {


    private int[] cardImages = {
            R.drawable.navio, R.drawable.navio,
            R.drawable.trem, R.drawable.trem,
            R.drawable.aviao, R.drawable.aviao,
            R.drawable.caminhao, R.drawable.caminhao,
    };

    private ImageView[] imageViews = new ImageView[8];
    private boolean[] cardFlipped = new boolean[8];
    private int previousCard = -1;
    private int pairsFound = 0;
    private boolean canClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jogo_memoria_transporte);

        EmbaralhaCartas();

        GridLayout gridLayout = findViewById(R.id.gridlayout);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageViews[i] = imageView;
            imageView.setTag(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canClick) {
                        int index = (int) v.getTag();
                        if (!cardFlipped[index]) {
                            flipCard(imageViews[index], index);
                        }
                    }
                }
            });
        }
    }

    private void EmbaralhaCartas() {
        List<Integer> cardList = new ArrayList<>();
        for (int card : cardImages) {
            cardList.add(card);
        }
        Collections.shuffle(cardList);
        for (int i = 0; i < cardImages.length; i++) {
            cardImages[i] = cardList.get(i);
        }
    }

    private void flipCard(ImageView imageView, int index) {
        cardFlipped[index] = true;
        imageView.setImageResource(cardImages[index]);
        imageView.setClickable(false);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        imageView.startAnimation(alphaAnimation);

        if (previousCard == -1) {
            previousCard = index;
        } else {
            canClick = false; // Desabilita cliques em novas cartas enquanto as duas estão sendo verificadas

            if (cardImages[previousCard] == cardImages[index]) { // Verifica o par
                if (cardImages[index] == R.drawable.aviao) {
                    playSound(R.raw.aviao_som);
                } else if (cardImages[index] == R.drawable.navio) {
                    playSound(R.raw.navio_som);
                } else if (cardImages[index] == R.drawable.trem) {
                    playSound(R.raw.trem_som);
                } else if (cardImages[index] == R.drawable.caminhao) {
                    playSound(R.raw.caminhao_som);
                }

                imageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeCards(previousCard, index);
                        previousCard = -1;
                        canClick = true; // Habilita cliques em novas cartas após a verificação

                        pairsFound++;
                        if (pairsFound == cardImages.length / 2) {
                            goToNextActivity();

                        }
                    }
                }, 1000);
            } else {
                // Par não encontrado
                final int prev = previousCard;
                imageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageViews[prev].setImageResource(R.drawable.verso_jogomemoria); // Assumindo que logo_jogomemoria é o verso da carta
                        imageViews[index].setImageResource(R.drawable.verso_jogomemoria);
                        cardFlipped[prev] = false;
                        cardFlipped[index] = false;
                        imageViews[prev].setClickable(true);
                        imageViews[index].setClickable(true);
                        previousCard = -1;
                        canClick = true; // Habilita cliques em novas cartas após a verificação
                    }
                }, 1000);
            }
        }
    }


    private void removeCards(int index1, int index2) {
        imageViews[index1].setVisibility(View.INVISIBLE);
        imageViews[index2].setVisibility(View.INVISIBLE);
    }

    private void goToNextActivity() {
        //inicia a pxm tela
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                View main = findViewById(R.id.main);
                AlertDialog.Builder builder = new AlertDialog.Builder(Jogo_Memoria_Transporte.this, R.style.CombinacaoDialog);
                View dialogView = getLayoutInflater().inflate(R.layout.activity_parabenizacao, null);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(dialogInterface -> blur(main));

                dialog.setOnDismissListener(dialogInterface -> removerBlur(main));

                dialog.show();

                MediaPlayer md = MediaPlayer.create(Jogo_Memoria_Transporte.this, R.raw.audio_palmas );
                md.start();

                dialog.setOnDismissListener(dialogInterface -> {
                    if(md !=null && md.isPlaying()){
                        md.stop();
                        md.release();
                    }
                });


                //dialog.getWindow().setLayout(1900, 1240);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                dialogView.findViewById(R.id.ButtonCasa).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(Jogo_Memoria_Transporte.this, Tela_menu.class);
                        startActivity(in);
                        finish();

                    }
                });
                dialogView.findViewById(R.id.ButtonRepetir).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(Jogo_Memoria_Transporte.this, Jogo_Memoria_Animais1.class);
                        startActivity(in);
                        finish();

                    }
                });



            }

        }, 300);

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

    private void playSound(int soundResId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
}
