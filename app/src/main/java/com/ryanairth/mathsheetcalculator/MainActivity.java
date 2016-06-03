package com.ryanairth.mathsheetcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.ryanairth.mathsheetcalculator.Math.BlockManager;
import com.ryanairth.mathsheetcalculator.Util.PreviewInputProcessor;

/**
 * Created by Ryan Airth (Sweeney) on 2015.
 * Copyright information found in License.txt file.
 */
public class MainActivity extends AppCompatActivity {
    public final static String TAG = "Ryan: ";

    private LinearLayout multiScrollView;
    private Intent selectionWheel;
    private int requestCode = 0;
    private int intentCount = 0;

    private PreviewInputProcessor inputProcessor;
    private BlockManager blockManager;

    // TODO - test stuff
    private ViewFlipper viewFlipper;
    private float lastX;

    // TODO - provide simple icons for extra information whilst main app is fullscreen
    // TODO - add more types of mathematics to the point of scientific calculator
    // TODO - add support for multiple saves of math grid as well as auto-save backup
    // TODO - set up container and grid to use co-ordinates
    // TODO - remake math grid to just render lines instead of images
    // TODO - separate each main part (multiscrollview, gridui and mathgrid) so they can be used in other ways

    // TODO - set up actual calculator instead of selection wheel

    // TODO - make it so that the actual button dictates what happens to the text in the preview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*setContentView(R.layout.calculator_layout);

        blockManager = new BlockManager();


        CalculatorPreview preview = (CalculatorPreview) findViewById(R.id.calculator_preview);
        if(preview == null) {
            Log.i(TAG, "Preview is null!");
        }

        inputProcessor = new PreviewInputProcessor(blockManager, preview);

        PadSlider padSlider = (PadSlider) findViewById(R.id.calculator_pad_slider);
        if(padSlider == null) {
            Log.e(TAG, "Padslider is null!");
        }

        CalculatorNumpad numpad = new CalculatorNumpad(this, null, preview);
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
            padSlider.addPagerView(numpad);
            padSlider.addPagerView(scientificPad);
        }*/
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
