package com.ryanairth.mathsheetcalculator.Util;

import android.content.Context;

import com.ryanairth.mathsheetcalculator.MainActivity;
import com.ryanairth.mathsheetcalculator.MathSheetApp;

/**
 * Created by ryan2 on 17/10/2015.
 */
public class Util {
    public int getValueInPixels(int resId) {
        return (int) MathSheetApp.getAppContext().getResources().getDimensionPixelSize(resId);
    }

    public double getAngle(double x1, double y1, double x2, double y2) {
        double deltaY = y2 - y1;
        double deltaX = x2 - x1;
        return Math.atan2(deltaY, deltaX) * 180 / Math.PI;
    }

    public double getLength(double x1, double y1, double x2, double y2) {
        double xSquare = (x1 - x2) * (x1 - x2);
        double ySquare = (y1 - y2) * (y1 - y2);

        return Math.sqrt(xSquare + ySquare);
    }
}
