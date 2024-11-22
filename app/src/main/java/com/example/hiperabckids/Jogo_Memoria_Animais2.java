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

public class Jogo_Memoria_Animais2 extends AppCompatActivity {
    //CARTAS ESTAO VISIVEIS

    private int[] cardImages = {
            R.drawable.borboleta1, R.drawable.borboleta2,
            R.drawable.abelha1, R.drawable.abelha2,
            R.drawable.ra1, R.drawable.ra2,
    };

    private ImageView[] imageViews = new ImageView[6];
    private boolean[] cardFlipped = new boolean[6];
    private int previousCard = -1;
    private int pairsFound = 0;
    private boolean canClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jogo_memoria_animais2);

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
                        imageViews[prev].setImageResource(R.drawable.verso_jogomemoria); //é o verso da carta
                        imageViews[index].setImageResource(R.drawable.verso_jogomemoria);
                        cardFlipped[prev] = false;
                        cardFlipped[index] = false;
                        imageViews[prev].setClickable(true);
                        imageViews[index].setClickable(true);

                    }
                }, 1000);
                previousCard = -1;
                canClick = true; // Habilita cliques em novas cartas após a verificação
            }
        }
    }

    private boolean areMatchingCards(int index1, int index2) {
        //return Math.abs(cardImages[index1] - cardImages[index2]) == 1; IDEIA ANTERIOR.
        return (cardImages[index1] == R.drawable.borboleta1 && cardImages[index2] == R.drawable.borboleta2) ||
                (cardImages[index1] == R.drawable.borboleta2 && cardImages[index2] == R.drawable.borboleta1) ||
                (cardImages[index1] == R.drawable.abelha1 && cardImages[index2] == R.drawable.abelha2) ||
                (cardImages[index1] == R.drawable.abelha2 && cardImages[index2] == R.drawable.abelha1) ||
                (cardImages[index1] == R.drawable.ra1 && cardImages[index2] == R.drawable.ra2) ||
                (cardImages[index1] == R.drawable.ra2 && cardImages[index2] == R.drawable.ra1);
    }

    private void removeCards(int index1, int index2) {
        imageViews[index1].setVisibility(View.INVISIBLE);
        imageViews[index2].setVisibility(View.INVISIBLE);
    }

    private void goToNextActivity() {
        Intent intent = new Intent(this, Jogo_Memoria_Animais3.class);
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
        if (cardImage == R.drawable.borboleta1 || cardImage == R.drawable.borboleta2) {
            playSound(R.raw.borboleta_som);
        } else if (cardImage == R.drawable.abelha1 || cardImage == R.drawable.abelha2) {
            playSound(R.raw.abelha_som);
        } else if (cardImage == R.drawable.ra1 || cardImage == R.drawable.ra2) {
            playSound(R.raw.sapo_som);
        }
    }
}