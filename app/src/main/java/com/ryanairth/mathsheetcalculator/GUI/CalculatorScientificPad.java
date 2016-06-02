package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.LinearLayout;

/**
 * Created by Ryan Airth (Sweeney) on 09/03/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class CalculatorScientificPad extends LinearLayout {
    // TODO - better link to xml file

    private GridLayout gridLayout;

    public CalculatorScientificPad(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        setBackgroundColor(Color.argb(255, 125, 51, 51));

        LayoutParams params = new LayoutParams(200, 200);
    }
}
