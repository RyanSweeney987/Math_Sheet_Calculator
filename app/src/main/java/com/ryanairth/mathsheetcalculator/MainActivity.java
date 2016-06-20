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

    // TODO - make it so that the actual button dictates what happens to the text in the preview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
