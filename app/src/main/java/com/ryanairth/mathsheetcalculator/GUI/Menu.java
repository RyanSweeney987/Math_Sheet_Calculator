package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ryanairth.mathsheetcalculator.R;

/**
 * Created by Ryan Airth (Sweeney) on 03/06/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class Menu extends LinearLayout {
    // TODO - complete

    private Button save, load, toGrid, help;


    public Menu(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        setId(R.id.menu);

        LinearLayout linearLayout = (LinearLayout) inflate(context, R.layout.menu_layout, null);

        save = (Button)linearLayout.findViewById(R.id.menu_save);
        load = (Button)linearLayout.findViewById(R.id.menu_load);
        toGrid = (Button)linearLayout.findViewById(R.id.menu_to_grid);
        help = (Button)linearLayout.findViewById(R.id.menu_help);

        linearLayout.removeAllViews();

        addView(save);
        addView(load);
        addView(toGrid);
        addView(help);
    }
}
