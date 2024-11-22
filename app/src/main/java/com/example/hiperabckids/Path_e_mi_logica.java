package com.example.hiperabckids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public abstract class Path_e_mi_logica extends View{

    protected Paint paint;
    protected Path path2;
    protected Path userPath2;
    protected float tolerance = 50; // Tolerância para considerar o toque válido
    protected boolean isCorrect2 = false;

    protected float correctDistance = 0f; // Distância correta percorrida
    protected float totalPathLength = 0f;
    protected float centerX;
    protected float centerY;
    protected Path correctPath2 = new Path(); // Para armazenar o caminho correto

    // Adicionar variável para o Bitmap de fundo
    private Bitmap backgroundImage;

    // MediaPlayers para os sons de acerto e erro
    private MediaPlayer mediaPlayerAcerto;
    private MediaPlayer mediaPlayerErro;

    public Path_e_mi_logica(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);

        path2 = new Path();
        userPath2 = new Path();

        // Carregar a imagem de fundo
        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.tracejado_e2);

        // Define os segmentos do path
        createPath2();

        // Escalar o path
        float scaleFactor = 3.5f;
        path2 = scalePath(path2, scaleFactor);

        // Obter o comprimento total do Path
        PathMeasure pm = new PathMeasure(path2, false);
        totalPathLength = pm.getLength();

        centerPath();

        // Inicializar os MediaPlayers para os áudios
        mediaPlayerAcerto = MediaPlayer.create(context, R.raw.som_acerto); // Som de acerto
        mediaPlayerErro = MediaPlayer.create(context, R.raw.som_erro); // Som de erro
    }

    protected abstract void createPath2();

    private Path scalePath(Path path, float scaleFactor) {
        Path scaledPath = new Path();
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleFactor, scaleFactor);
        path.transform(scaleMatrix, scaledPath);
        return scaledPath;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Calcular o centro da tela
        centerX = w / 2;
        centerY = h / 2;

        // Centralizar o path
        centerPath();
    }

    private void centerPath() {
        // Obter os limites do path
        RectF bounds = new RectF();
        path2.computeBounds(bounds, true);

        // Calcular o deslocamento necessário para centralizar
        float pathWidth = bounds.width();
        float pathHeight = bounds.height();

        // Centraliza horizontal e verticalmente
        float translateX = (getWidth() - pathWidth) / 2 - bounds.left;
        float translateY = (getHeight() - pathHeight) / 2 - bounds.top;

        // Aplica o deslocamento ao path
        path2.offset(translateX, translateY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Desenhar a imagem de fundo
        if (backgroundImage != null) {
            // Definir o novo tamanho da imagem manualmente
            int newWidth = 520;  // Ajuste a largura desejada
            int newHeight = 600; // Ajuste a altura desejada

            // Posicionamento da imagem (ajuste a posição conforme necessário)
            int offsetX = -15; // Posição horizontal
            int offsetY = 60; // Posição vertical (abaixar a imagem)

            // Definir o retângulo de destino onde a imagem será desenhada
            Rect destRect = new Rect(offsetX, offsetY, offsetX + newWidth, offsetY + newHeight);

            // Desenhar a imagem redimensionada
            canvas.drawBitmap(backgroundImage, null, destRect, null);
        }

        // Desenhar o contorno da letra
        canvas.drawPath(path2, paint);

        // Desenhar o caminho do usuário
        Paint userPaint = new Paint();
        userPaint.setColor(Color.RED);
        userPaint.setStyle(Paint.Style.STROKE);
        userPaint.setStrokeWidth(100);
        canvas.drawPath(userPath2, userPaint);

        // Desenhar o caminho correto, se existir
        if (!correctPath2.isEmpty()) {
            Paint correctPaint = new Paint();
            correctPaint.setColor(Color.GREEN); // Cor para o caminho correto
            correctPaint.setStyle(Paint.Style.STROKE);
            correctPaint.setStrokeWidth(100);
            canvas.drawPath(correctPath2, correctPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                userPath2.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                userPath2.lineTo(x, y);
                invalidate();
                checkIfOnPath(x, y);
                return true;
            case MotionEvent.ACTION_UP:
                // Verifica se pelo menos 90% do caminho foi percorrido corretamente
                if (correctDistance / totalPathLength >= 1.0f) {
                    isCorrect2 = true;
                    correctPath2.set(userPath2); // Salva o caminho correto
                    //audio de acerto
                    if(mediaPlayerErro.isPlaying()){
                        mediaPlayerErro.stop();
                    }
                    mediaPlayerAcerto.start();
                    // Iniciar a nova Activity após o áudio de acerto
                    mediaPlayerAcerto.setOnCompletionListener(mp -> {
                        Intent intent = new Intent(getContext(), Tracejado_F.class); // Substitua 'NovaActivity' pelo nome da sua nova Activity
                        getContext().startActivity(intent);
                    });
                } else {
                    //audio de erro
                    if (mediaPlayerAcerto.isPlaying()) {
                        mediaPlayerAcerto.stop();  // Para o áudio de acerto se estiver tocando
                    }
                    mediaPlayerErro.start(); // Toca o áudio de erro
                }
                reset();
                return true;
        }
        return false;
    }

    private void checkIfOnPath(float x, float y) {
        // Verifica se o ponto atual está próximo do caminho da letra
        float[] point = new float[2];
        PathMeasure pm = new PathMeasure(path2, false);
        boolean isOnPath = false;

        for (float distance = 0; distance < pm.getLength(); distance += tolerance) {
            pm.getPosTan(distance, point, null);
            if (Math.abs(x - point[0]) < tolerance && Math.abs(y - point[1]) < tolerance) {
                isOnPath = true;
                correctDistance += tolerance; // Aumenta a distância percorrida corretamente
                break;
            }
        }

        if (!isOnPath) {
            isCorrect2 = false;
        }
    }

    private void reset() {
        userPath2.reset();
        correctDistance = 0f; // Reseta a distância correta percorrida
        invalidate();
    }
}
