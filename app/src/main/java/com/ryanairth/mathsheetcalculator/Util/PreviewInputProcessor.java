package com.ryanairth.mathsheetcalculator.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ryanairth.mathsheetcalculator.Exceptions.InvalidMathOperatorException;
import com.ryanairth.mathsheetcalculator.GUI.CalculatorPreview;
import com.ryanairth.mathsheetcalculator.GUI.MathPreview;
import com.ryanairth.mathsheetcalculator.Math.Block;
import com.ryanairth.mathsheetcalculator.Math.BlockManager;
import com.ryanairth.mathsheetcalculator.Math.MathOperator;
import com.ryanairth.mathsheetcalculator.MathSheetApp;
import com.ryanairth.mathsheetcalculator.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

/**
 * Created by Ryan Airth (Sweeney) on 03/06/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class PreviewInputProcessor implements PreviewUpdateListener {
    final String DASH_SEPARATOR = "------------------------------------------------------";
    final String HASH_SEPARATOR = "######################################################";

    private MathPreview preview;
    /*
        Block manager that manages all of the numbers and symbols used for the maths
     */
    private BlockManager manager;
    /*
        The current string that's either a symbol or a number, eventually gets added to the block
        manager
     */
    private String currentBlockString;
    /*
        Current text that's in the preview text
     */
    private String currentText;
    /*
        Last character in current text
     */
    private char lastChar;
    /*
        Second last character in current text
     */
    private char secondLastChar;
    /*
        Boolean to see if the equation has currently been evaluated (equals sign has been pressed)
     */
    private boolean isEvaluated;

    public PreviewInputProcessor(BlockManager manager, CalculatorPreview preview) {
        this.manager = manager;
        this.preview = preview;

        init();
    }

    /**
     * Initializes all the main stuff for the class
     */
    private void init() {
        // Initialize the currentBlockString so it's empty rather than null
        currentBlockString = "";
        currentText = "0";
    }

    /**
     * Updates the text view (preview text) with the current value
     *
     * @param value Value of the button pressed can be digit or math symbol (+,- etc)
     */
    @Override
    public void updatePreview(char value) {
        // Note: Processing the minus must always become before symbols, symbols are the last stage
        //          this is because the minus symbol is unique in that it both serves as subtraction
        //          and an indicator that a number is negative. (below 0, -0 not included)
        //       It may also be best to keep both decimal and symbol processing at or near the end
        //          and have any more specific bit of processing before hand.
        //       Of course one could process each and every symbol manually however, most by the same
        //          formatting rules so it's not completely necessary.


        currentText = preview.getPrimary().getText().toString();
        lastChar = currentText.charAt(currentText.length() - 1);

        // In order to avoid a null pointer exception, we can only do this when there are two or
        // more elements in the string
        if(currentText.length() > 1) {
            secondLastChar = currentText.charAt(currentText.length() - 2);
        }

        // If the calculation was recently evaluated we need to do certain things based on the next input
        if(isEvaluated) {
            if(Character.isDigit(value)) {
                resetPreview();
            }

            isEvaluated = false;
        }

        // If the entire string just contains zero, the default start point, set the current text to
        // an empty string
        if(currentText.equals("0")) {
            currentText = "";
            lastChar = '\u0000';

            switch (value) {
                case '-':
                    processMinus();
                    break;
                case '.':
                    processDecimal();
                    break;
                default:
                    if(Character.isDigit(value)) {
                        processDigit(value);
                    }

                    break;
            }
        } else {
            switch (value) {
                case '=':
                    processEquals();
                    break;
                case '-':
                    processMinus();
                    break;
                case '.':
                    processDecimal();
                    break;
                default:
                    if(Character.isDigit(value)) {
                        processDigit(value);
                    } else {
                        processSymbol(value);
                    }

                    break;
            }
        }

        Log.i(TAG, "Second last char: " + secondLastChar);
        Log.i(TAG, "Last char: " + lastChar);
        Log.i(TAG, "Current char: " + value);
        Log.i(TAG, "Current block string value: " + currentBlockString);
        Log.i(TAG, "Current text value: " + currentText);
        Log.e(TAG, DASH_SEPARATOR);
    }

    /**
     * Processes the equals input, will calculate the final sum given and sets the preview text
     */
    private void processEquals() {
        updateCurrentBlockString(currentBlockString, true, false);

        Log.i(TAG, "::Blocks::" + System.getProperty("line.separator")
                + manager.toString() + "::Blocks::");

        // Get sum from block manager
        double sum = 0.0;
        // Try catch was used for debugging, can take out safely (hopefully)
        try {
            sum = manager.getBlockEvaluator().calculateTotal();

            // Format it so it's presentable
            String sumString = formatNumber(sum);
            // Reset the preview
            resetPreview();
            // Set current text and reset the preview text total to null
            setText(sumString);
            preview.getSecondary().setText("");
            // Update the current block
            currentBlockString = sumString;

            // Set the isEvaluated boolean for logic that deals with further input after = has been pressed
            isEvaluated = true;

            preview.scrollText(View.FOCUS_LEFT);
        } catch (ClassCastException e) {
            Log.e(TAG, "Blocks: " + manager.toString());

            String exception = Log.getStackTraceString(e);

            Log.e(TAG, exception);
        } catch (InvalidMathOperatorException e) {
            Log.e(TAG, "Exception occurred when calculating total, invalid math operator found");

            String exception = Log.getStackTraceString(e);

            Log.e(TAG, exception);

            // TODO - show popup explaining that an error has occurred - when internet is fixed
            //DialogFragment exceptionDialog = new ExceptionDialogFragment();
            //exceptionDialog.show(MathSheetApp.getAppContext(), "exception");

            resetPreview();
        }
    }

    /**
     * Processes a digit passed
     *
     * @param digit Number value passed to add to the block string and such
     */
    private void processDigit(final char digit) {
        // If it's a digit, set text
        setText(currentText + digit);

        if(!Character.isDigit(lastChar) && lastChar != '.' && lastChar != '-' && lastChar != '\u0000') {
            // If following a symbol except minus or decimal start again otherwise we have to
            // add it to the current block string
            updateCurrentBlockString(String.valueOf(digit), true, false);
        } else if(lastChar == '-') {
            // If second last character isn't a number or is the null character, include minus
            if(!Character.isDigit(secondLastChar) || secondLastChar == '#') {
                updateCurrentBlockString(String.valueOf(digit), false, false);
            } else {
                // Otherwise minus is being used as an operator in this instance
                updateCurrentBlockString(String.valueOf(digit), true, false);
            }
        } else {
            updateCurrentBlockString(String.valueOf(digit), false, false);
        }
    }

    /**
     * Processes a decimal, makes sure that there's only one decimal as well what to do if the user
     * pressed the decimal without actually adding a number or zero
     */
    private void processDecimal() {
        final char DECIMAL = '.';

        // Check to see if the current block doesn't contain a decimal, if it doesn't proceed
        if(!currentBlockString.contains(".")) {
            // If the case is that it's at the beginning automatically add a zero if the decimal
            // is pressed
            if(currentText.equals("") || currentText.equals("0")) {
                setText(currentText + 0 + DECIMAL);

                updateCurrentBlockString(0 + String.valueOf(DECIMAL), true, false);
            } else if(Character.isDigit(lastChar)) {
                setText(currentText + DECIMAL);

                updateCurrentBlockString(String.valueOf(DECIMAL), false, false);
            } else {
                // To handle any case where the previous value isn't a number, much like the beginning
                setText(currentText + 0 + DECIMAL);

                // Also include for the case where we want a negative decimal and the user just presses
                // decimal to automatically add the zero
                if(lastChar == '-') {
                    updateCurrentBlockString(0 + String.valueOf(DECIMAL), false, false);
                } else {
                    updateCurrentBlockString(0 + String.valueOf(DECIMAL), true, false);
                }
            }
        }
    }

    /**
     * Processes a minus symbol, minus is unique as it's also used to represent negative numbers, as
     * such, it means that a minus can appear twice in a row
     */
    private void processMinus() {
        final char MINUS = '-';

        // For the first entry
        if(currentText.equals("") || currentText.equals("0")) {
            setText(currentText + MINUS);

            updateCurrentBlockString(String.valueOf(MINUS), false, false);
        }
        // If the last character is a number, we're specifically looking to take away from it
        if(Character.isDigit(lastChar)) {
            setText(currentText + MINUS);

            updateCurrentBlockString(String.valueOf(MINUS), true, false);
        }
        // Otherwise we are starting a new, negative number however, we can only ever have a
        // symbol followed by a minus, nothing else
        if(secondLastChar != '#' && !Character.isDigit(lastChar) && Character.isDigit(secondLastChar)) {
            setText(currentText + MINUS);

            updateCurrentBlockString(String.valueOf(MINUS), true, false);
        }
    }

    /**
     * Processes symbols, pretty much everything except digits, decimal and minus, so plus, mult,
     * divide, percentage etc
     */
    private void processSymbol(final char symbol) {
        // TODO - TEMP/TEST
        /*if(symbol == MathOperator.LEFT_BRACKET.getSymbol() || symbol == MathOperator.RIGHT_BRACKET.getSymbol()) {
            setText(currentText + symbol);

            updateCurrentBlockString(String.valueOf(symbol), true, false);

            // TODO - refactor so no return statement
            return;
        }*/
        // TODO - END TEMP/TEXT

        // TODO - if right bracket is added straight after a number, add a multiply.
        // TODO - Do the same if number is added directly after right bracket, otherwise, use don't add a multiply.


        // First check to make sure if we're not entering the same symbol, if we are, ignore it
        if(symbol != lastChar) {
            // If the last character is a minus
            if(Character.isDigit(lastChar)) {
                setText(currentText + symbol);

                updateCurrentBlockString(String.valueOf(symbol), true, false);
            }
            // If the last character isn't number
            if(!Character.isDigit(lastChar)) {
                // And if the last character isn't a minus with the second last character being a digit,
                // carry on, otherwise, do nothing and expect a number to be entered as this will
                // signify the start of a negative number
                if(Character.isDigit(secondLastChar)) {
                    String subString = currentText.substring(0, currentText.length() - 1);

                    setText(subString + symbol);

                    updateCurrentBlockString(String.valueOf(symbol), true, true);
                }
            }
        }
    }

    /**
     * Sets the text of the previews and automatically scrolls them to the end
     *
     * @param updatedText the text that we want the preview texts to display
     */
    private void setText(String updatedText) {
        Log.i(TAG, "Setting text to: " + updatedText);

        // Update text
        preview.getPrimary().setText(updatedText);
        currentText = updatedText;

        // If there's more than one block
        if(manager.getBlocks().size() != 0) {
            Log.i(TAG, "Showing total");

            double sum = 0.0;

            // Unnecessary try catch, was used for debugging, can take out safely (hopefully)
            try {
                sum = manager.getBlockEvaluator().calculateCurrentTotal();
            } catch (Exception e) {
                Log.e(TAG, manager.toString());

                StringBuilder stackTrace = new StringBuilder();

                for(StackTraceElement elem : e.getStackTrace()) {
                    stackTrace.append(elem.toString());
                    stackTrace.append(System.getProperty("line.separator"));
                }

                Log.e("AndroidRuntime: ", stackTrace.toString());
            }

            preview.getSecondary().setText(formatNumber(sum));
        }

        preview.scrollText(View.FOCUS_RIGHT);
    }

    /**
     * Updates the currentBlockString as well as modifying the block manager to have the latest data
     *
     * @param value The value to be added to the block string
     * @param finishedState Whether or not the call to this should add to the block manager
     * @param replacePrevious Whether or not the call to this should replace the last object in the
     *                        block manager
     */
    private void updateCurrentBlockString(String value, boolean finishedState, boolean replacePrevious) {
        if(finishedState) {
            if(replacePrevious) {
                currentBlockString = "";

                try {
                    Double number = Double.parseDouble(currentBlockString);
                    manager.createAndAddBlock(number);
                } catch (NumberFormatException e) {
                    Log.w(TAG, "Failed parsing a number, adding character!");

                    if(currentBlockString.length() > 0) {
                        manager.createAndAddBlock(MathOperator.getEnumFromCharacter(currentBlockString.charAt(0)));
                    } else {
                        Log.e(TAG, "Failed to add character, currentBlockString is empty");
                    }

                    Log.i(TAG, DASH_SEPARATOR);
                }

                currentBlockString = value;
            } else {
                try {
                    Double number = Double.parseDouble(currentBlockString);
                    manager.createAndAddBlock(number);
                } catch(NumberFormatException e) {
                    Log.w(TAG, "Failed parsing a number, adding character!");

                    if(currentBlockString.length() > 0) {
                        // TODO - TEST
                        if(lastChar == '(') {
                            manager.createAndAddBlock(MathOperator.MULTIPLY);
                        }
                        // TODO - END TEST

                        manager.createAndAddBlock(MathOperator.getEnumFromCharacter(currentBlockString.charAt(0)));
                    } else {
                        Log.e(TAG, "Failed to add character, currentBlockString is empty");
                    }


                    Log.i(TAG, DASH_SEPARATOR);
                }

                currentBlockString = value;
            }

            if(!manager.isEmpty()) {
                Log.i(TAG, "Number of blocks: " + manager.getBlocks().size());
                Log.i(TAG, "First on deque: " + manager.getFirstBlock());
                Log.i(TAG, "Last on deque: " + manager.getFinalBlock());
            } else {
                Log.i(TAG, "Block manager is empty");
            }
            Log.e(TAG, DASH_SEPARATOR);
        } else {
            currentBlockString += value;
        }
    }

    /**
     * Resets the preview, block manager and current block string
     */
    @Override
    public void resetPreview() {
        Log.i(TAG, "Resetting preview");
        Log.e(TAG, HASH_SEPARATOR);

        // Reset the texts to display "0", the default state
        preview.resetPreview();

        currentBlockString = "";
        currentText = "0";

        // Reset the block manager, deleting all blocks
        manager.reset();

        // Reset last and second last char parameters to the null value (aka: "/0")
        lastChar = '\u0000';
        secondLastChar = '\u0000';

        isEvaluated = false;
    }

    /**
     * Deletes the last input from preview text and block in the block manager
     */
    @Override
    public void deleteLastInput() {
        Log.i(TAG, "Undoing last input");

        // If the currentText is greater than 0, then it's fine to go ahead and delete previous element
        // of both the currentBlockString and the currentText
        if(currentText.length() > 0 && !(currentText.startsWith("0") && currentText.length() == 1)) {
            if(currentBlockString.length() > 0) {
                // Get length of currentBlockString and use it in order to get substring
                int stringSizeBlock = currentBlockString.length();
                String subStringCurrentBlock = currentBlockString.substring(0, stringSizeBlock - 1);

                // Set currentBlockString to the new block string which has the last element deleted
                currentBlockString = subStringCurrentBlock;

                Log.i(TAG, "CurrentBlockString: " + currentBlockString);
                Log.i(TAG, "CurrentBlockString length: " + stringSizeBlock + ", sub string: " + subStringCurrentBlock);

                // Remove the last element from current text
                removeLastTextElement();

                // If currentBlockString is empty we want to set it as the next thing
                if(currentBlockString.isEmpty() && !manager.isEmpty()) {
                    Log.e(TAG, "CurrentBlockString length is 0 or less");

                    // Get the block that holds the data
                    Block currentBlock = manager.getFinalBlock();

                    // If the block holds a math operator we want the currentBlockString to hold it's
                    // character value likewise for a number
                    if(currentBlock.getValue() instanceof MathOperator) {
                        Log.i(TAG, "Current block is a MathOperator");

                        MathOperator operator = (MathOperator)currentBlock.getValue();

                        currentBlockString = String.valueOf(operator.getSymbol());
                    } else if(currentBlock.getValue() instanceof Double) {
                        // currentBlockString = String.valueOf(currentBlock.getValue());
                        currentBlockString = formatNumber((Double)currentBlock.getValue());

                        Log.i(TAG, "Current block is a number");
                    }

                    Log.i(TAG, "CurrentBlockString is now: " + currentBlockString);
                    Log.i(TAG, "Is blockManager empty: " + manager.isEmpty());

                    // Then we remove the element from the block manager
                    manager.pop();
                }
            }
        } else {
            Log.i(TAG, "At beginning, nothing to undo");
        }

        Log.e(TAG, DASH_SEPARATOR);
    }

    /**
     * Removes the last element from the current text and then sets the preview
     * text or resets it if there's nothing left
     */
    private void removeLastTextElement() {
        int stringSizeText = currentText.length();
        String subStringCurrentText = currentText.substring(0, stringSizeText - 1);

        Log.i(TAG, "CurrentText: " + currentText);
        Log.i(TAG, "CurrentText length: " + stringSizeText + ", sub string: " + subStringCurrentText);

        // Set currentText to the new string which has the last element deleted
        if(subStringCurrentText.isEmpty()) {
            resetPreview();
        } else {
            setText(subStringCurrentText);
        }
    }

    /**
     * Formats the number so that if it is a whole number, it returns without any decimal and trailing
     * zeros
     *
     * @param number the number to be formatted
     * @return formatted string of the number
     */
    private String formatNumber(double number) {
        if(number == (long) number) {
            // Explicit locale to UK as default locale is apparently prone to bugs
            return String.format(Locale.UK, "%d", (long)number);
        } else {
            return String.format(Locale.UK, "%s", number);
        }
    }
}
