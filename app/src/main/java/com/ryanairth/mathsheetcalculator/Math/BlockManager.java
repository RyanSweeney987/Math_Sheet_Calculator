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
    /*
        List containing "blocks", each block represents a symbol or a number
     */
    private List<Block> blocks;

    /**
     * BlockManager manages the numbers and symbols entered by the user in modular blocks, {@link BlockManager}
     * and {@link SymbolBlock}. These are stored in an ArrayList
     *
     * @see NumberBlock
     * @see SymbolBlock
     */
    public BlockManager() {
        blocks = new ArrayList<>();
    }

    /**
     * Get the list of blocks
     *
     * @return deque containing all the blocks or an empty deque
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Returns the last block from the list
     *
     * @return last block on the deque
     */
    public Block getFinalBlock() {
        return blocks.get(blocks.size() - 1);
    }

    /**
     * Returns the first block from the list
     *
     * @return first block on the deque
     */
    public Block getFirstBlock() {
        return blocks.get(0);
    }

    /**
     * Composition method that calls the blocks' array.isEmpty() method
     *
     * @return whether or not the blocks array is empty
     */
    public boolean isEmpty() {
        return blocks.isEmpty();
    }

    /**
     * Removes the top element from the array
     */
    public void pop() {
        blocks.remove(blocks.size() - 1);
    }

    /**
     * Calculate and return the sum of the maths equation entered by the user
     *
     * @return number representing the sum of the maths equation
     */
    public double getSum() {
        // FIXME - fix so it actually calculates the sum of the math equation

        double sum = 0.0;
        double currentNumber = 0.0;
        MathOperator currentSymbol = MathOperator.NONE;

        Block currentBlock = null;
        Block previousBlock = null;

        for(int i = 0; i < blocks.size(); i++) {
            currentBlock = blocks.get(i);

            if(currentBlock instanceof NumberBlock) {
                sum = ((NumberBlock)currentBlock).getValue();
            } else if(currentBlock instanceof SymbolBlock) {

            }
        }

        return sum;
    }

    /**
     * Resets the array so that it is empty
     */
    public void reset() {
        blocks.clear();
    }

    /**
     * Creates a new NumberBlock and adds it to the array
     *
     * @param number the value to be used in the new block
     *
     * @see NumberBlock
     */
    public void createAndAddBlock(double number) {
        Log.i(TAG, "Number being added: " + number);

        if(blocks.size() > 0) {
            blocks.add(new NumberBlock(number, getFinalBlock()));
        } else {
            blocks.add(new NumberBlock(number));
        }
    }

    /**
     * Creates a new SymbolBlock and adds it to the array
     *
     * @param operator the symbol to be used in the new block
     *
     * @see SymbolBlock
     */
    public void createAndAddBlock(MathOperator operator) {
        Log.i(TAG, "Symbol being added: " + operator.name() + ":" + operator.getSymbol());

        if (blocks.size() > 0) {
            blocks.add(new SymbolBlock(operator, getFinalBlock()));
        } else {
            blocks.add(new SymbolBlock(operator));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");

        builder.append("Number of blocks: " + blocks.size());
        builder.append(lineSeparator);

        int numberCount = 0;
        int symbolCount = 0;

        for(Block elem : blocks) {
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

        for(Block elem : blocks) {
            builder.append(elem.toString());
            builder.append(lineSeparator);
        }

        return builder.toString();
    }
}
