/*
 * Created by Ryan Airth (Sweeney) on 2016.
 * Copyright information found in accompanying License.txt file.
 */

package com.ryanairth.mathsheetcalculator.Math;

import android.util.Log;

import com.ryanairth.mathsheetcalculator.Exceptions.InvalidMathOperatorException;

import java.util.ArrayList;
import java.util.List;

import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

/**
 * Created by Ryan Airth (Sweeney) on 04/06/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class BlockSequence extends BlockEvaluator implements Block<Double> {
    /*
        Value of this sequence
     */
    private Double value;

    public BlockSequence() {
        this(new ArrayList<Block>());
    }

    public BlockSequence(List<Block> blocks) {
        super(blocks);
    }

    @Override
    public Double getValue() {
        try {
            if(value != null) {
                return value;
            } else {
                value = calculateCurrentTotal();
                return value;
            }
        } catch (InvalidMathOperatorException e) {
            String exceptionString = Log.getStackTraceString(e);

            Log.e(TAG, exceptionString);

            return -10000000000.0;
        }
    }

    @Override
    public String toString() {
        return "Type: BlockSequence, Value: " + String.valueOf(getValue());
    }
}
