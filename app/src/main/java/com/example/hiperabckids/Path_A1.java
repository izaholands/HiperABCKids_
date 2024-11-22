package com.example.hiperabckids;

import android.content.Context;
import android.util.AttributeSet;

public class Path_A1 extends Path_A_logica {
    public Path_A1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void createPath() {

        // Mova para o ponto inicial
        path.moveTo(21.61f, 139.74f); // Move para (21.61, 139.74)
        path.lineTo(35.77f, 103.23f); // Linha para (35.77, 103.23)

        path.moveTo(56.81f, 39.23f);  // Move para (56.81, 39.23)
        path.lineTo(63.89f, 20.97f);  // Linha para (63.89, 20.97)
        path.lineTo(70.97f, 2.72f);    // Linha para (70.97, 2.72)

        path.moveTo(40.22f, 89.91f);  // Move para (40.22, 89.91)
        path.lineTo(54.38f, 53.4f);    // Linha para (54.38, 53.4)

        path.moveTo(3f, 187.42f);      // Move para (3, 187.42)
        path.lineTo(17.16f, 150.91f);  // Linha para (17.16, 150.91)

        path.moveTo(110.61f, 92.92f);  // Move para (110.61, 92.92)
        path.lineTo(96.05f, 52.54f);    // Linha para (96.05, 52.54)

        path.moveTo(127.6f, 144.89f);  // Move para (127.6, 144.89)
        path.lineTo(113.04f, 104.52f);  // Linha para (113.04, 104.52)

        path.moveTo(92f, 41.38f);       // Move para (92, 41.38)
        path.lineTo(77.44f, 1f);        // Linha para (77.44, 1)

        path.moveTo(145f, 193f);        // Move para (145, 193)
        path.lineTo(130.44f, 152.62f);  // Linha para (130.44, 152.62)

        path.moveTo(82.29f, 144.89f);   // Move para (82.29, 144.89)
        path.lineTo(113.04f, 144.89f);  // Linha horizontal para (113.04, 144.89)

        path.moveTo(31.72f, 144.89f);   // Move para (31.72, 144.89)
        path.lineTo(62.47f, 144.89f);    // Linha horizontal para (62.47, 144.89)

    }
}
