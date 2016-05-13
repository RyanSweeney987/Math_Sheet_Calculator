package com.ryanairth.mathsheetcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ryanairth.mathsheetcalculator.GUI.MultiScrollView;

/**
 * Created by Ryan Airth (Sweeney) on 08/03/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class MathGrid extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scrollable_number_grid_layout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
