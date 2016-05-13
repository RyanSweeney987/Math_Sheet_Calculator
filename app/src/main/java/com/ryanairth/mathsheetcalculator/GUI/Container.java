package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.ryanairth.mathsheetcalculator.MathSheetApp;
import com.ryanairth.mathsheetcalculator.R;

/**
 * Created by Ryan Airth (Sweeney) on 13-Sep-15.
 * Copyright information found in License.txt file.
 */
public class Container extends RelativeLayout {
    // TAG is statically imported from MainActivity
    private int xPos, yPos;
    private Box box;

    public Container(Context context, int xPos, int yPos) {
        super(context);

        this.xPos = xPos;
        this.yPos = yPos;

        init();
    }

    public int getXPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    private void init() {
        RelativeLayout contLayout = (RelativeLayout) View.inflate(MathSheetApp.getAppContext(), R.layout.container_layout, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        contLayout.setLayoutParams(params);

        addView(contLayout);
    }
}
