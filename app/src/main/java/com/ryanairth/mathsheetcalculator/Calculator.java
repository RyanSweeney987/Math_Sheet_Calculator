package com.ryanairth.mathsheetcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.ryanairth.mathsheetcalculator.GUI.CalculatorNumpad;
import com.ryanairth.mathsheetcalculator.GUI.CalculatorPreview;
import com.ryanairth.mathsheetcalculator.GUI.CalculatorScientificPad;
import com.ryanairth.mathsheetcalculator.GUI.Menu;
import com.ryanairth.mathsheetcalculator.GUI.PadSlider;
import com.ryanairth.mathsheetcalculator.Math.BlockManager;
import com.ryanairth.mathsheetcalculator.Util.PreviewInputProcessor;

/**
 * Created by Ryan Airth (Sweeney) on 20/01/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class Calculator extends AppCompatActivity {
    public final static String TAG = "Ryan: ";

    private LinearLayout multiScrollView;
    private Intent selectionWheel;
    private int requestCode = 0;
    private int intentCount = 0;

    private PreviewInputProcessor inputProcessor;
    private BlockManager blockManager;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.calculator_layout);

        blockManager = new BlockManager();


        //CalculatorPreview preview = (CalculatorPreview) findViewById(R.id.calculator_preview);
        //if(preview == null) {
        //    Log.i(TAG, "Preview is null!");
       // }

        inputProcessor = new PreviewInputProcessor(blockManager, (CalculatorPreview) findViewById(R.id.calculator_preview));

        PadSlider padSlider = (PadSlider) findViewById(R.id.calculator_pad_slider);
        if(padSlider == null) {
            Log.e(TAG, "Padslider is null!");
        }

        Menu menu = new Menu(this, null);
        menu.setOrientation(LinearLayout.VERTICAL);
        if(menu == null) {
            Log.e(TAG, "Menu is null!");
        }

        CalculatorNumpad numpad = new CalculatorNumpad(this, null);
        numpad.setPreviewUpdateListener(inputProcessor);
        if(numpad == null) {
            Log.e(TAG, "Numpad is null!");
        }

        CalculatorScientificPad scientificPad = new CalculatorScientificPad(this, null);
        //
        if(scientificPad == null) {
            Log.e(TAG, "Scientific is null!");
        }

        if(padSlider != null && numpad != null && scientificPad != null) {
            padSlider.addPagerView(menu);
            padSlider.addPagerView(numpad);
            padSlider.addPagerView(scientificPad);

            padSlider.setCurrentPage(1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "Return vale: " + data.getStringExtra("VALUE"));

            Bundle b = data.getExtras();
            String s = b.getString("VALUE");
        }

        super.onActivityResult(requestCode, resultCode, data);

        intentCount--;
        // TODO - on return add data to math grid
    }
}
