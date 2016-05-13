package com.ryanairth.mathsheetcalculator.GUI;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Ryan Airth (Sweeney) on 06/01/2016.
 * Copyright information found in accompanying License.txt file.
 */
public enum LineStates {
    PRESSED(new Paint(){
        {
            setColor(Color.argb(255, 235, 54, 54));
            setStrokeCap(Cap.ROUND);
        }
    }), RELEASED(new Paint() {
        {
            setColor(Color.argb(255, 183, 183, 183));
            setStrokeCap(Cap.ROUND);
        }
    }), DEFAULT(new Paint() {
        {
            setColor(Color.argb(255, 150, 150, 255));
            setStrokeCap(Cap.ROUND);
        }
    });

    private Paint paint;

    private LineStates(Paint paint) {
        this.paint = paint;
        setPaintValues(paint);
    }

    public Paint getPaint() {
        return paint;
    }

    private void setPaintValues(Paint paintValues) {
        paintValues.setStrokeWidth(5);
    }
}
