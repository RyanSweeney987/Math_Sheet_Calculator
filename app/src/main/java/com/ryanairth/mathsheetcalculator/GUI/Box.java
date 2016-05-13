package com.ryanairth.mathsheetcalculator.GUI;

import android.graphics.Paint;

/**
 * Created by Ryan Airth (Sweeney) on 08/01/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class Box {
    private Line[] lines;
    private Paint paint;

    private LineStates lineStates;

    public Box(Line[] lines) {
        this.lines = lines;
    }

    public Line[] getLines() {
        return lines;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;

        for(Line elem : lines) {
            elem.setPaint(paint);
           // elem.setPaint(paint);
        }
    }
}
