package com.ryanairth.mathsheetcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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

        inputProcessor = new PreviewInputProcessor(blockManager, (CalculatorPreview) findViewById(R.id.calculator_preview));

        PadSlider padSlider = (PadSlider) findViewById(R.id.calculator_pad_slider);

        if(padSlider != null) {
            padSlider.setCurrentPage(1);

            // TODO - implement menu functionality
            Menu menu = (Menu) padSlider.findViewById(R.id.menu);
            //Menu menu = new Menu(this, null);

            CalculatorNumpad numpad = (CalculatorNumpad) padSlider.findViewById(R.id.calculator_number_pad);
            //CalculatorNumpad numpad = new CalculatorNumpad(this, null);
            numpad.setPreviewUpdateListener(inputProcessor);

            CalculatorScientificPad scientificPad = (CalculatorScientificPad) padSlider.findViewById(R.id.calculator_scientific_pad);
            //CalculatorScientificPad scientificPad = new CalculatorScientificPad(this, null);
            scientificPad.setPreviewUpdateListener(inputProcessor);

            //padSlider.addPagerView(menu);
            //padSlider.addPagerView(numpad);
            //padSlider.addPagerView(scientificPad);

            //padSlider.setCurrentPage(1);
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
