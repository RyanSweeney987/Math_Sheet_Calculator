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
     * @throws InvalidMathOperatorException exception thrown when an unexpected operator is found,
     * cause would be unimplemented operators
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
     * @throws InvalidMathOperatorException exception thrown when an unexpected operator is found, cause
     * would be unimplemented operators
     */
    public double calculateCurrentTotal() throws InvalidMathOperatorException {
        // If there aren't any elements in the blocks array, return zero
        if(blocks.size() <= 0) {
            Log.i(TAG, "Not enough blocks, returning zero");
            return 0.0;
        }

        // If there's only one or two elements return the current value as element 0 will be a number
        // and element 1 will be a symbol/math operator
        if(blocks.size() <= 2) {
            Log.i(TAG, "Not enough blocks, returning current value");
            return ((NumberBlock)blocks.get(0)).getValue();
        }

        return evaluate(this.blocks);
    }

    /**
     * Evaluates the current block array
     *
     * @param blockList arrayContaining the blocks
     * @return the evaluation of the sum entered by the user
     * @throws InvalidMathOperatorException
     */
    private double evaluate(List<Block> blockList) throws InvalidMathOperatorException {
        // Get the first number to start off
        double currentValue = 0.0;

        // We know there's at least a single element, so we know that blocks.get(0) will not fail
        currentValue = ((NumberBlock)blockList.get(0)).getValue();

        // Otherwise we're fine to carry on with our usual operations
        for(int i = 1; i < blockList.size(); i++) {
            // Test to see if final block is a symbol, if so, break, no need to continue
            if(i == blockList.size() - 1 && blockList.get(i) instanceof SymbolBlock) {
                break;
            }

            // Every time we loop we want the next thing to get to be the operator used
            MathOperator operator = ((SymbolBlock)blockList.get(i++)).getValue();
            // After we get the operator to use, we get the number that will be used to modify the current value

            double nextValue = 0;

            if(containsBracket(blockList)) {
                // If we have an equal number of brackets we call this method again passing in the sub sequence
                if(hasBracketPairs(blockList)) {
                    // Set next value to what ever the value in the brackets is
                    nextValue = evaluate(getInnerEquation(blockList));
                    // Set block list to the outer equation so that the inner equation counts as a number
                    // and we resume as if nothing happened
                    blockList = extractOuterEquation(blockList);
                } else {
                    // If there aren't bracket pairs, just evaluate everything before the first bracket
                    return evaluate(blockList.subList(0, getOpenBracketPosition(blockList)));
                }
            } else {
                // If there isn't a bracket carry on as normal and use the next number
                nextValue = ((NumberBlock)blockList.get(i)).getValue();
            }
            currentValue = performMathOperation(currentValue, nextValue, operator);
        }

        return currentValue;
    }

    /**
     * This method checks to see if the current list contains any brackets at all
     *
     * @param blockList list containing all of the math blocks, numbers and symbols
     * @return whether or not we have bracket pairs
     */
    private boolean containsBracket(List<Block> blockList) {
        // Iterate through the list, as soon as we hit a left bracket if there is any, we return true
        // otherwise it's definitely false
        for(Block elem : blockList) {
            if(elem instanceof SymbolBlock) {
                if(elem.getValue() == MathOperator.LEFT_BRACKET) {
                    return true;
                }
            }
        }

        return false;
    }

    // TODO - potentially remove this method and move to "PreviewInputProcessor"
    /**
     * This method checks to see if the current list contains bracket pairs, without them we can't properly
     * compute the equation. For each open bracket the MUST be a closing bracket.
     *
     * @param blockList list containing all of the math blocks, numbers and symbols
     * @return whether or not we have bracket pairs
     */
    private boolean hasBracketPairs(List<Block> blockList) {
        int leftBracketCount = 0;
        int rightBracketCount = 0;

        // For each bracket we encounter we increment the counters
        for(Block elem : blockList) {
            if(elem instanceof SymbolBlock) {
                if(elem.getValue() == MathOperator.LEFT_BRACKET) {
                    leftBracketCount++;
                } else if(elem.getValue() == MathOperator.RIGHT_BRACKET) {
                    rightBracketCount++;
                }
            }
        }
        // If the counts for both opening and closing brackets are not the same then we are missing some
        return leftBracketCount == rightBracketCount;
    }

    /**
     *
     * @param blockList
     * @return
     */
    private List<Block> getInnerEquation(List<Block> blockList) {
        // Create a new array containing all values from the first an last bracket
        int firstLoc = -1, lastLoc = -1;

        for(int j = 0; j < blockList.size(); j++) {
            if(blockList.get(j) instanceof SymbolBlock) {
                if(blockList.get(j).getValue() == MathOperator.LEFT_BRACKET && firstLoc == -1) {
                    firstLoc = j;
                } else if(blockList.get(j).getValue() == MathOperator.RIGHT_BRACKET && j > lastLoc) {
                    lastLoc = j;
                }
            }
        }

        return blockList.subList(getOpenBracketPosition(blockList) + 1, getCloseBracketPosition(blockList));
    }

    /**
     *
     * @param blockList
     * @return
     */
    private List<Block> extractOuterEquation(List<Block> blockList) {
        List<Block> subListBase = blockList.subList(0, getOpenBracketPosition(blockList));
        List<Block> subListAfter = blockList.subList(getCloseBracketPosition(blockList) + 1, blockList.size());

        subListBase.addAll(subListAfter);

        return subListBase;
    }

    /**
     *
     * @param blockList
     * @return
     */
    private int getOpenBracketPosition(List<Block> blockList) {
        for(int i = 0; i < blockList.size(); i++) {
            if(blockList.get(i) instanceof SymbolBlock) {
                if(blockList.get(i).getValue() == MathOperator.LEFT_BRACKET) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     *
     * @param blockList
     * @return
     */
    private int getCloseBracketPosition(List<Block> blockList) {
        for(int i = blockList.size()-1; i > 0; i--) {
            if(blockList.get(i) instanceof SymbolBlock) {
                if(blockList.get(i).getValue() == MathOperator.RIGHT_BRACKET) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     *
     * @param currentValue
     * @param nextValue
     * @param operator
     * @return
     * @throws InvalidMathOperatorException
     */
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
