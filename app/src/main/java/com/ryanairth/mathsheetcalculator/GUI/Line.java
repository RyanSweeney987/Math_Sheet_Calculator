package com.ryanairth.mathsheetcalculator.GUI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.ryanairth.mathsheetcalculator.GUI.LineStates;
import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

/**
 * Created by Ryan Airth (Sweeney) on 06/01/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class Line implements CustomDrawable {
    private int x1, y1;
    private int x2, y2;
    private Paint paint;

    public Line(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
    }

    public Line(int x1, int y1, int x2, int y2, Paint paint) {
        this(x1, y1, x2, y2);

        this.paint = paint;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(x1, y1, x2, y2, paint);
    }
}
