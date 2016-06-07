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
public class BlockSequence implements Block<Double> {
    // TODO - implement recursion for evaluation and addition
    /*
        List containing all the number/symbol blocks as well as other block sequences
     */
    private List<Block> blocks;
    /*
        Evaluator object used to calculate the total of this sequence
     */
    private BlockEvaluator evaluator;

    public BlockSequence() {
        blocks = new ArrayList<>();
        evaluator = new BlockEvaluator(this);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Get the {@link BlockEvaluator} used to calculate the final value of all the blocks entered by the user
     *
     * @see BlockEvaluator
     *
     * @return block evaluator object
     */
    public BlockEvaluator getBlockEvaluator() {
        return evaluator;
    }

    @Override
    public Double getValue() {
        try {
            return evaluator.calculateTotal();
        } catch (InvalidMathOperatorException e) {
            String exceptionString = Log.getStackTraceString(e);

            Log.e(TAG, exceptionString);

            return -1.0;
        }
    }
}
