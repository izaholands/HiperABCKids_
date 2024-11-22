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

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Jogo_Memoria_Frutas2 extends AppCompatActivity {
    //CARTAS NAO VISIVEIS

    // Array com os IDs das imagens das cartas, cada par tem a mesma imagem
    private int[] cardImages = {
            R.drawable.tangerina, R.drawable.maca, R.drawable.melao, R.drawable.melancia,
            R.drawable.uva, R.drawable.banana,
            R.drawable.tangerina, R.drawable.maca, R.drawable.melao, R.drawable.melancia,
            R.drawable.uva, R.drawable.banana
    };

    private ImageView[] imageViews = new ImageView[12]; // Array de ImageView para armazenar as referências às imagens das cartas no layout
    private boolean[] cardFlipped = new boolean[12]; // Array para acompanhar se a carta foi virada ou não
    private int previousCard = -1; // Índice da carta virada, -1 indica nenhuma carta virada

    private int pairsFound = 0; // Contagem dos pares encontrados

    private boolean canClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jogo_memoria_frutas2);

        EmbaralhaCartas();

        // Grid que está no layout
        GridLayout gridLayout = findViewById(R.id.gridlayout);

        // Armazena cada ImageView em imageViews e define o OnClick para responder aos cliques
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

    // Método chamado quando a carta é clicada
    private void flipCard(ImageView imageView, int index) {
        cardFlipped[index] = true;
        imageView.setImageResource(cardImages[index]);
        imageView.setClickable(false); // Define a imagem da carta virada e a torna não clicável

        // Animação fade-in
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        imageView.startAnimation(alphaAnimation);

        // Se for -1, armazena o índice da carta virada
        if (previousCard == -1) {
            previousCard = index;
        } else {
            canClick = false;
            if (cardImages[previousCard] == cardImages[index]) { // Verifica o par
                // Par encontrado emite o som do nome da fruta
                if (cardImages[index] == R.drawable.tangerina) {
                    playSound(R.raw.tangerina_som);
                } else if (cardImages[index] == R.drawable.maca) {
                    playSound(R.raw.maca_som);
                } else if (cardImages[index] == R.drawable.melao) {
                    playSound(R.raw.melao_som);
                } else if (cardImages[index] == R.drawable.melancia) {
                    playSound(R.raw.melancia_som);
                } else if (cardImages[index] == R.drawable.uva) {
                    playSound(R.raw.uva_som);
                } else if (cardImages[index] == R.drawable.banana) {
                    playSound(R.raw.banana_som);
                }

                imageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeCards(previousCard, index);
                        previousCard = -1;
                        canClick = true;

                        pairsFound++; // Incrementa a contagem dos pares
                        if (pairsFound == cardImages.length / 2) {
                            PxmActivity(); // Tira esse quando colocar a celebração
                            // playCelebrationSound();
                        }
                    }
                }, 1000); // 1 segundo de atraso para remover as cartas

            } else {
                // Par não encontrado
                final int prev = previousCard;
                imageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageViews[prev].setImageResource(R.drawable.verso_jogomemoria); // Assumindo que hiperabc_4_2 é o verso da carta
                        imageViews[index].setImageResource(R.drawable.verso_jogomemoria);
                        cardFlipped[prev] = false;
                        cardFlipped[index] = false;
                        imageViews[prev].setClickable(true);
                        imageViews[index].setClickable(true);
                    }
                }, 1000); // 1 segundo de atraso para virar as cartas e poder clicar nelas novamente
                previousCard = -1;
                canClick = true;
            }
        }
    }

    // Método que torna as cartas invisíveis ao remover um par encontrado
    private void removeCards(int index1, int index2) {
        imageViews[index1].setVisibility(View.INVISIBLE);
        imageViews[index2].setVisibility(View.INVISIBLE);
    }

    private void PxmActivity() {
        Intent intent = new Intent(this, Jogo_Memoria_Alfabeto1.class);
        startActivity(intent);
        finish();
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

    /*private void playCelebrationSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.comemoracao);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                goToNextActivity(); // Inicia a próxima atividade após o término do som comemorativo
            }
        });
    }*/
}
