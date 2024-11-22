package com.example.hiperabckids;

import android.content.Context;
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

public abstract class Path_A_logica extends View {
    protected Paint paint;
    protected Path path;
    protected Path userPath;
    protected float tolerance = 50; // Tolerância para considerar o toque válido
    protected boolean isCorrect = true;

    protected float centerX;
    protected float centerY;
    protected Path correctPath = new Path(); // Para armazenar o caminho correto


    // Adicionar variável para o Bitmap de fundo
    private Bitmap backgroundImage;

    // MediaPlayers para os sons de acerto e erro
    private MediaPlayer mediaPlayerAcerto;
    private MediaPlayer mediaPlayerErro;




    public Path_A_logica(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            paint = new Paint();

            int alpha = 0;
            //paint.setColor(Color.argb(alpha,0,0,0));
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(20);
            paint.setAntiAlias(true);

            // Aplicar efeito pontilhado no caminho, 20=compriento, 10 espacamento, 0=deslocamento inicial do padrão ( ajustado para criar variações no efeito).
            //paint.setPathEffect(new DashPathEffect(new float[]{30, 10}, 0));


            path = new Path();
            userPath = new Path();

            // Carregar a imagem de fundo
            backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.tracejado_a);

            // Define os segmentos do path
            createPath();

            // Escalar o path
            float scaleFactor = 2.5f;
            path = scalePath(path, scaleFactor);

            centerPath();

            // Inicializar os MediaPlayers para os áudios
            mediaPlayerAcerto = MediaPlayer.create(context, R.raw.som_acerto); // Som de acerto
            mediaPlayerErro = MediaPlayer.create(context, R.raw.som_erro); // Som de erro
        }

    protected abstract void createPath();

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
            path.computeBounds(bounds, true);

            // Calcular o deslocamento necessário para centralizar
            float pathWidth = bounds.width();
            float pathHeight = bounds.height();

            // Centraliza horizontal e verticalmente
            float translateX = (getWidth() - pathWidth) / 2 - bounds.left;
            float translateY = (getHeight() - pathHeight) / 2 - bounds.top;

            // Aplica o deslocamento ao path
            path.offset(translateX, translateY);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // Desenhar a imagem de fundo
            if (backgroundImage != null) {
                // Definir o novo tamanho da imagem manualmente
                int newWidth = 510;  // Ajuste a largura desejada
                int newHeight = 600; // Ajuste a altura desejada

                // Posicionamento da imagem (ajuste a posição conforme necessário)
                int offsetX = 0; // Posição horizontal
                int offsetY = 70; // Posição vertical (abaixar a imagem)

                // Definir o retângulo de destino onde a imagem será desenhada
                Rect destRect = new Rect(offsetX, offsetY, offsetX + newWidth, offsetY + newHeight);

                // Desenhar a imagem redimensionada
                canvas.drawBitmap(backgroundImage, null, destRect, null);

                //canvas.drawBitmap(backgroundImage, 10, 70, null); // Desenha a imagem no canto superior esquerdo
            }

            // Desenhar o contorno da letra
            canvas.drawPath(path, paint);

            // Desenhar o caminho do usuário
            Paint userPaint = new Paint();
            userPaint.setColor(Color.RED);
            userPaint.setStyle(Paint.Style.STROKE);
            userPaint.setStrokeWidth(100);
            canvas.drawPath(userPath, userPaint);

            // Desenhar o caminho correto, se existir
            if (!correctPath.isEmpty()) {
                Paint correctPaint = new Paint();
                correctPaint.setColor(Color.GREEN); // Cor para o caminho correto
                correctPaint.setStyle(Paint.Style.STROKE);
                correctPaint.setStrokeWidth(100);
                canvas.drawPath(correctPath, correctPaint);
            }

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    userPath.moveTo(x, y);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    userPath.lineTo(x, y);
                    invalidate();
                    checkIfOnPath(x, y);
                    return true;
                case MotionEvent.ACTION_UP:
                    if (isCorrect) {
                            correctPath.set(userPath); //salva o caminho correto
                        //audio de acerto
                        if(mediaPlayerErro.isPlaying()){
                            mediaPlayerErro.stop();
                        }
                        mediaPlayerAcerto.start();
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
            PathMeasure pm = new PathMeasure(path, false);

            for (float distance = 0; distance < pm.getLength(); distance += tolerance) {
                pm.getPosTan(distance, point, null);
                if (Math.abs(x - point[0]) < tolerance && Math.abs(y - point[1]) < tolerance) {
                    isCorrect = true;
                    return;
                }
            }
            isCorrect = false; // Se não estiver sobre o caminho, marca como incorreto
        }

        private void reset() {
            userPath.reset();
            invalidate();
        }
    }
