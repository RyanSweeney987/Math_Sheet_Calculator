package com.ryanairth.mathsheetcalculator.Util;

import android.app.Activity;
import android.content.Context;

import com.ryanairth.mathsheetcalculator.MainActivity;
import com.ryanairth.mathsheetcalculator.MathSheetApp;
import com.ryanairth.mathsheetcalculator.R;

/**
 * Created by ryan2 on 13/10/2015.
 */
public class GridUtil {
    private int totalWidth, totalHeight;
    private int contSize;
    public final int ROW = 30, COLUMN = 20;

    public GridUtil() {
        contSize = MathSheetApp.getAppContext().getResources().getDimensionPixelSize(R.dimen.box_dp);
        totalWidth = COLUMN * contSize;
        totalHeight = ROW * contSize;
    }

    public int getContSize() {
        return contSize;
    }

    public int getTotalWidth() {
        return totalWidth;
    }

    public int getTotalHeight() {
        return totalHeight;
    }
}
