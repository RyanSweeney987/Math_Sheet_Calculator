package com.ryanairth.mathsheetcalculator.Math;

import android.util.Log;

import com.ryanairth.mathsheetcalculator.Exceptions.InvalidMathOperatorException;

import java.util.ArrayList;
import java.util.List;

import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

/**
 * Created by Ryan Airth (Sweeney) on 22/05/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class BlockEvaluator {
    // TODO - go through all the sequences recursively

    /*
        List of blocks used for the calculations
     */
    private List<Block> blocks;

    /**
     * Block calculator, object designed to specifically calculate the total of all the blocks entered
     * by the user
     *
     * @param blocks reference of the list that contains all the blocks
     */
    public BlockEvaluator(List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Calculate and return the total of the maths equation entered by the user, used when all inputs
     * have been made and looking for the final value
     *
     * @return number representing the sum of the maths equation
     * @throws InvalidMathOperatorException exception thrown when an unexpected operator is found, cause would be unimplemented operators
     */
    public double calculateTotal() throws InvalidMathOperatorException {
        // If there aren't any elements in the blocks array, return zero
        if(blocks.size() <= 0) {
            Log.i(TAG, "Not enough blocks, returning zero");
            return 0.0;
        }

        return evaluate(this.blocks);
    }

    /**
     * Calculate and return the final evaluation of the maths equation entered by the user thus far
     *
     * @return number representing the sum of the maths equation
     * @throws InvalidMathOperatorException exception thrown when an unexpected operator is found, cause would be unimplemented operators
     */
    public double calculateCurrentTotal() throws InvalidMathOperatorException {
        // If there aren't any elements in the blocks array, return zero
        if(blocks.size() <= 0) {
            Log.i(TAG, "Not enough blocks, returning zero");
            return 0.0;
        }

        /*
            If there's only one or two elements return the current value as element 0 will be a number
            and element 1 will be a symbol/math operator
         */
        if(blocks.size() <= 2) {
            Log.i(TAG, "Not enough blocks, returning current value");
            return ((NumberBlock)blocks.get(0)).getValue();
        }

        return evaluate(this.blocks);
    }

    /**
     * Evaluates the current block array
     *
     * @param blockArray arrayContaining the blocks
     * @return the evaluation of the sum entered by the user
     * @throws InvalidMathOperatorException
     */
    private double evaluate(List<Block> blockArray) throws InvalidMathOperatorException {
        // TODO - support recursion within the method itself
        // Get the first number to start off
        double currentValue = 0.0;

        // We know there's at least a single element, so we know that blocks.get(0) will not fail
        currentValue = ((NumberBlock)blockArray.get(0)).getValue();

        boolean containsBracket = containsBracket(blockArray);

        // Otherwise we're fine to carry on with our usual operations
        for(int i = 1; i < blockArray.size(); i++) {
            // Test to see if final block is a symbol, if so, break, no need to continue
            if(i == blockArray.size() - 1 && blockArray.get(i) instanceof SymbolBlock) {
                break;
            }

            // Every time we loop we want the next thing to get to be the operator used
            MathOperator operator = ((SymbolBlock)blockArray.get(i++)).getValue();
            // After we get the operator to use, we get the number that will be used to modify the current value

            // TODO - find if right bracket exists, if so, add everything within bracket to new
            // TODO - array, do evaluation on new array, set next value to bracket
            // TODO - If no opposing right bracket exists, end here, don't calculate any further until
            // TODO - right bracket is added



            double nextValue = 0;

            // TODO - refactor!!!
            if(containsBracket) {
                // If we have an equal number of brackets we call this method again passing in the sub sequence
                if(hasBracketPairs(blockArray)) {
                    // Create a new array containing all values from the first an last bracket
                    int firstLoc = -1, lastLoc = -1;

                    for(int j = 0; j < blockArray.size(); j++) {
                        if(blockArray.get(j) instanceof SymbolBlock) {
                            if(blockArray.get(j).getValue() == MathOperator.LEFT_BRACKET && firstLoc == -1) {
                                firstLoc = j;

                                Log.i(TAG, "Left bracket location: " + j);
                            } else if(blockArray.get(j).getValue() == MathOperator.RIGHT_BRACKET && j > lastLoc) {
                                lastLoc = j;

                                Log.i(TAG, "Right bracket location: " + j);
                            }
                        }
                    }

                    for(Block elem : blockArray.subList(firstLoc + 1, lastLoc)) {
                        Log.i(TAG, "Sublist item: " + elem.getValue());
                    }

                    nextValue = evaluate(blockArray.subList(firstLoc + 1, lastLoc));

                    List<Block> sublistBefore = blockArray.subList(0, firstLoc);

                    for(Block elem : sublistBefore) {
                        Log.i(TAG, "Sublist before item: " + elem.getValue());
                    }

                    List<Block> sublistAfter = blockArray.subList(lastLoc + 1, blockArray.size());

                    for(Block elem : sublistAfter) {
                        Log.i(TAG, "Sublist before item: " + elem.getValue());
                    }

                    List<Block> newSublist = new ArrayList<>();

                    for(int j = 0; j < sublistAfter.size(); j++) {
                        sublistBefore.add(sublistAfter.get(j));
                    }

                    blockArray = newSublist;

                    Log.i(TAG, "First location: " + firstLoc);
                    Log.i(TAG, "Last location: " + lastLoc);
                } else {
                    int firstLoc = -1;

                    for(int j = 0; j < blockArray.size(); j++) {
                        if(blockArray.get(j) instanceof SymbolBlock) {
                            if(blockArray.get(j).getValue() == MathOperator.LEFT_BRACKET) {
                                firstLoc = j;

                                break;
                            }
                        }
                    }

                    return evaluate(blockArray.subList(0, firstLoc));
                }
            } else {
                nextValue = ((NumberBlock)blockArray.get(i)).getValue();
            }
            //double nextValue = ((NumberBlock)blockArray.get(i)).getValue();

            currentValue = performMathOperation(currentValue, nextValue, operator);
        }

        return currentValue;
    }

    private boolean hasBracketPairs(List<Block> blockList) {
        int leftBracketCount = 0;
        int rightBracketCount = 0;

        for(int i = 0; i < blockList.size(); i++) {
            if(blockList.get(i) instanceof SymbolBlock) {
                if(blockList.get(i).getValue() == MathOperator.LEFT_BRACKET) {
                    leftBracketCount++;
                } else if(blockList.get(i).getValue() == MathOperator.RIGHT_BRACKET) {
                    rightBracketCount++;
                }
            }

        }

        return leftBracketCount == rightBracketCount;
    }

    private boolean containsBracket(List<Block> blockList) {
        for(int i = 0; i < blockList.size(); i++) {
            if(blockList.get(i) instanceof SymbolBlock) {
                if(blockList.get(i).getValue() == MathOperator.LEFT_BRACKET) {
                    return true;
                }
            }
        }

        return false;
    }

    private double performMathOperation(double currentValue, double nextValue, MathOperator operator)
            throws InvalidMathOperatorException {
        switch (operator) {
            case PLUS:
                currentValue += nextValue;
                break;
            case MINUS:
                currentValue -= nextValue;
                break;
            case MULTIPLY:
                currentValue *= nextValue;
                break;
            case DIVIDE:
                currentValue /= nextValue;
                break;
            case PERCENTAGE:
                currentValue = (currentValue / 100) * nextValue;
                break;
            case NONE:
                // This should never occur, if it does, there's an issue when adding the operator
                // to the block manager in any class that has this as an object
            default:
                // Might not actually need to throw anything here, though, just hoping it would be useful
                // perhaps it's an incorrect usage, not too used to throwing errors and exceptions
                throw new InvalidMathOperatorException("Error calculating total, math operator is: "
                        + MathOperator.NONE);
        }

        return currentValue;
    }
}
