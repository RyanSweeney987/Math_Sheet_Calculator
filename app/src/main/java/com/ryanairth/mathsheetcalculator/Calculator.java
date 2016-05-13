package com.ryanairth.mathsheetcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ryanairth.mathsheetcalculator.GUI.CalculatorNumpad;
import com.ryanairth.mathsheetcalculator.GUI.CalculatorPreview;
import com.ryanairth.mathsheetcalculator.GUI.PadSlider;

/**
 * Created by Ryan Airth (Sweeney) on 20/01/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class Calculator extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.calculator_layout);

        CalculatorPreview preview = (CalculatorPreview) findViewById(R.id.calculator_preview);
        PadSlider padSlider = (PadSlider) findViewById(R.id.calculator_pad_slider);
        CalculatorNumpad numpad = (CalculatorNumpad) getLayoutInflater().inflate(R.layout.calculator_layout, null);
        padSlider.addView(numpad);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
