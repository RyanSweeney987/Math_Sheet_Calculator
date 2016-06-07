package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.ryanairth.mathsheetcalculator.R;
import com.ryanairth.mathsheetcalculator.Util.PreviewUpdateDispatcher;
import com.ryanairth.mathsheetcalculator.Util.PreviewUpdateListener;

/**
 * Created by Ryan Airth (Sweeney) on 09/03/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class CalculatorScientificPad extends LinearLayout implements PreviewUpdateDispatcher {
    /**
     * Grid layout to put the buttons in, for some reason this is really needed or odd gaps
     * appear between the buttons
     */
    private GridLayout gridLayout;
    /**
     * Buttons that are symbols used with more scientific operations
     */
    private Button leftBracket, rightBracket;
    /**
     * Receiver for all the preview update events
     */
    private PreviewUpdateListener listener;

    public CalculatorScientificPad(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    /**
     * Initializes all the main stuff for the class
     *
     * @param context The context required to inflate the numpad layout which this class will get
     *                details from
     */
    private void init(Context context){
        setGravity(Gravity.CENTER);
        setId(R.id.calculator_scientific_pad);

        gridLayout = (GridLayout) inflate(context, R.layout.scientific_calc_layout, null);

        addView(gridLayout);

        leftBracket = (Button) gridLayout.findViewById(R.id.scientific_left_bracket);
        rightBracket = (Button) gridLayout.findViewById(R.id.scientific_right_bracket);

        setUpButtons();
    }

    @Override
    public void setPreviewUpdateListener(PreviewUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * Method to separate things up to make things much cleaner as opposed to having a god method
     * All buttons implement a custom OnClickListener that also sends the scientific symbol data to the
     * {@link PreviewUpdateListener} via {@link PreviewUpdateDispatcher}
     *
     * @see PreviewUpdateListener
     * @see PreviewUpdateDispatcher
     */
    private void setUpButtons() {
        leftBracket.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.scientific_left_bracket).charAt(0));
            }
        });

        rightBracket.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.scientific_right_bracket).charAt(0));
            }
        });
    }
}
