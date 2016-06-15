package com.ryanairth.mathsheetcalculator.Math;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.ryanairth.mathsheetcalculator.MainActivity.*;

/**
 * Created by Ryan Airth (Sweeney) on 01/04/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class BlockManager {
    // TODO - implement recursion for addition and evaluation
    // TODO - left bracket starts new sequence, right bracket indicates end of sequence
    /*
       The primary sequence that the manager uses
     */
    private List<Block> blocks;
    /*
        The current sequence. In the event the user uses brackets
     */
    private BlockSequence currentSequence;
    /*
        Evaluator object used to calculate the total of all sequences
     */
    private BlockEvaluator evaluator;

    /**
     * BlockManager manages the numbers and symbols entered by the user in modular blocks, {@link BlockManager},
     * {@link SymbolBlock} or {@link BlockSequence}.
     *
     * @see NumberBlock
     * @see SymbolBlock
     * @see BlockSequence
     */
    public BlockManager() {
        blocks = new ArrayList<>();
        currentSequence = new BlockSequence(blocks);
        evaluator = new BlockEvaluator(blocks);
    }

    public BlockSequence getCurrentSequence() {
        return currentSequence;
    }

    public BlockEvaluator getBlockEvaluator() {
        return evaluator;
    }
    /**
     * Get the list of blocks from current sequence
     *
     * @return list containing all the blocks or an empty deque
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Returns the last block from the current sequence
     *
     * @return last block on the deque
     */
    public Block getFinalBlock() {
        return getBlocks().get(getBlocks().size() - 1);
    }

    /**
     * Returns the first block from the current sequence
     *
     * @return first block on the deque
     */
    public Block getFirstBlock() {
        return getBlocks().get(0);
    }

    /**
     * Composition method that calls the current sequences blocks' array.isEmpty() method
     *
     * @return whether or not the blocks array is empty
     */
    public boolean isEmpty() {
        return getBlocks().isEmpty();
    }

    /**
     * Removes the top element from the array in the current sequence
     */
    public void pop() {
        getBlocks().remove(getBlocks().size() - 1);
    }

    /**
     * Resets the array so that it is empty
     */
    public void reset() {
        getBlocks().clear();
    }

    /**
     * Creates a new NumberBlock and adds it to the current sequence
     *
     * @param number the value to be used in the new block
     *
     * @see NumberBlock
     */
    public void createAndAddBlock(double number) {
        Log.i(TAG, "Number being added: " + number);

        getBlocks().add(new NumberBlock(number));
    }

    /**
     * Creates a new SymbolBlock and adds it to the current sequence
     *
     * @param operator the symbol to be used in the new block
     *
     * @see SymbolBlock
     */
    public void createAndAddBlock(MathOperator operator) {
        Log.i(TAG, "Symbol being added: " + operator.name() + ":" + operator.getSymbol());

        // TODO - if user adds a bracket, start new sequence
        if(operator == MathOperator.LEFT_BRACKET) {
            BlockSequence nextSequence = new BlockSequence();

            currentSequence.getBlocks().add(nextSequence);

            currentSequence = nextSequence;

            Log.i(TAG, "Current sequence changed");
            Log.i(TAG, currentSequence.toString());
        } else if(operator == MathOperator.RIGHT_BRACKET) {

        } else {
            currentSequence.getBlocks().add(new SymbolBlock(operator));
        }

       // getBlocks().add(new SymbolBlock(operator));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");

        builder.append("Number of blocks: " + getBlocks().size());
        builder.append(lineSeparator);

        int numberCount = 0;
        int symbolCount = 0;

        for(Block elem : getBlocks()) {
            if(elem instanceof NumberBlock) {
                numberCount++;
            } else if(elem instanceof SymbolBlock) {
                symbolCount++;
            }
        }

        builder.append("Number of numbers: " + numberCount);
        builder.append(lineSeparator);
        builder.append("Number of symbols: " + symbolCount);
        builder.append(lineSeparator);

        builder.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        builder.append(lineSeparator);

        for(Block elem : getBlocks()) {
            builder.append(elem.toString());
            builder.append(lineSeparator);
        }

        return builder.toString();
    }
}
