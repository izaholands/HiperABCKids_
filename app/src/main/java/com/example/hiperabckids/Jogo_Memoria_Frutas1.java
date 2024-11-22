package com.example.hiperabckids;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jogo_Memoria_Frutas1 extends AppCompatActivity {

    //CARTAS ESTAO VISIVEIS, MENOSS QUASE O MAMAO MAS DA PRA RELEVAR


    private int[] cardImages = {
            R.drawable.abacaxi1, R.drawable.abacaxi2,
            R.drawable.cereja1, R.drawable.cereja2,
            R.drawable.morango1, R.drawable.morango2,
            R.drawable.pera1, R.drawable.pera2,
            R.drawable.mamao1, R.drawable.mamao2,
    };


    private ImageView[] imageViews = new ImageView[10];
    private boolean[] cardFlipped = new boolean[10];
    private int previousCard = -1;
    private int pairsFound = 0;
    private boolean canClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jogo_memoria_frutas1);

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
            if (areMatchingCards(previousCard, index)) {
                // Par encontrado
                playCorrespondingSound(cardImages[index]);
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

    private boolean areMatchingCards(int index1, int index2) {
        return (cardImages[index1] == R.drawable.abacaxi1 && cardImages[index2] == R.drawable.abacaxi2) ||
                (cardImages[index1] == R.drawable.abacaxi2 && cardImages[index2] == R.drawable.abacaxi1) ||
                (cardImages[index1] == R.drawable.morango1 && cardImages[index2] == R.drawable.morango2) ||
                (cardImages[index1] == R.drawable.morango2 && cardImages[index2] == R.drawable.morango1) ||
                (cardImages[index1] == R.drawable.cereja1 && cardImages[index2] == R.drawable.cereja2) ||
                (cardImages[index1] == R.drawable.cereja2 && cardImages[index2] == R.drawable.cereja1) ||
                (cardImages[index1] == R.drawable.pera1 && cardImages[index2] == R.drawable.pera2) ||
                (cardImages[index1] == R.drawable.pera2 && cardImages[index2] == R.drawable.pera1) ||
                (cardImages[index1] == R.drawable.mamao1 && cardImages[index2] == R.drawable.mamao2) ||
                (cardImages[index1] == R.drawable.mamao2 && cardImages[index2] == R.drawable.mamao1);
    }

    private void removeCards(int index1, int index2) {
        imageViews[index1].setVisibility(View.INVISIBLE);
        imageViews[index2].setVisibility(View.INVISIBLE);
    }

    private void goToNextActivity() {
        Intent intent = new Intent(this, Jogo_Memoria_Frutas2.class); // Substitua NextActivity pela sua próxima atividade
        startActivity(intent);
        finish(); // Fecha a atividade atual
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

    private void playCorrespondingSound(int cardImage) {
        if (cardImage == R.drawable.cereja1 || cardImage == R.drawable.cereja2) {
            playSound(R.raw.cereja_som);
        } else if (cardImage == R.drawable.morango1 || cardImage == R.drawable.morango2) {
            playSound(R.raw.morango_som);
        } else if (cardImage == R.drawable.abacaxi1 || cardImage == R.drawable.abacaxi2) {
            playSound(R.raw.abacaxi_som);
        } else if (cardImage == R.drawable.pera1 || cardImage == R.drawable.pera2) {
            playSound(R.raw.pera_som);
        } else if (cardImage == R.drawable.mamao1 || cardImage == R.drawable.mamao2) {
            playSound(R.raw.mamao_som);
        }
    }
}