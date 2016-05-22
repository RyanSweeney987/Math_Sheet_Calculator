package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ryanairth.mathsheetcalculator.Math.BlockManager;
import com.ryanairth.mathsheetcalculator.Math.MathOperator;
import com.ryanairth.mathsheetcalculator.R;

import java.util.ArrayList;
import java.util.List;

import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

/**
 * Created by Ryan Airth (Sweeney) on 16/01/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class CalculatorPreview extends RelativeLayout {
    // TODO - make so that if user enters a number after hitting '=', they start off with the previous value
    final String DASH_SEPARATOR = "------------------------------------------------------";

    /*
        Text view for the numbers and symbols
     */
    private TextView previewTextMain;
    private TextView previewTextTotal;
    /*
        HorizontalScrollViews that have the text views inside them so people can scroll
        Not completely necessary to have these here but might come in handy
     */
    private HorizontalScrollView scrollViewMain;
    private HorizontalScrollView scrollViewTotal;
    /*
        Block manager that manages all of the numbers and symbols used for the maths
     */
    private BlockManager blockManager;
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

    /**
     * Box used to preview the numbers the user has entered
     *
     * @param context the context provided to do contexty stuff
     * @param attrs attributes passed but won't really be needed to be directly accessed for this class, probably
     */
    public CalculatorPreview(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    /**
     * Initializes all the main stuff for the class
     *
     * @param context The context required to create layouts and such
     */
    private void init(Context context) {
        setId(R.id.calculator_preview);

        // Get the layout created to make things easier
        RelativeLayout layout = (RelativeLayout) inflate(context, R.layout.preview_text_box, null);

        final List<View> layoutViews = new ArrayList<>();
        final int layoutChildCount = layout.getChildCount();

        // Add the views to the array from layout
        for(int i = 0; i < layoutChildCount; i++) {
            layoutViews.add(layout.getChildAt(i));
        }

        // Free the views in the array from their parent
        layout.removeAllViews();

        // Add the views in the array to this layout
        for(View elem : layoutViews) {
            addView(elem);
        }

        // Acquire the main text horizontal scroll view
        scrollViewMain = (HorizontalScrollView) findViewById(R.id.preview_hscrollv_main);

        // Acquire the main text from inside the horizontal scroll view
        previewTextMain = (TextView) findViewById(R.id.preview_main_text);
        previewTextMain.setText("0");

        Log.i(TAG, "PreviewTextMain layout null: " + previewTextMain.getLayout());

        // Acquire the total text horizontal scroll view
        scrollViewTotal = (HorizontalScrollView) findViewById(R.id.preview_hscrollv_total);

        // Acquire the total text from inside the horizontal scroll view
        previewTextTotal = (TextView) findViewById(R.id.preview_total_text);
        previewTextTotal.setText("0");

        // Create the block manager
        blockManager = new BlockManager();

        // Initialize the currentBlockString so it's empty rather than null
        currentBlockString = "";
    }

    /**
     * Updates the text view (preview text) with the current value
     *
     * @param value Value of the button pressed can be digit or math symbol (+,- etc)
     */
    public void updatePreview(char value) {
        currentText = previewTextMain.getText().toString();
        lastChar = currentText.charAt(currentText.length() - 1);

        // In order to avoid a null pointer exception, we can only do this when there are two or
        // more elements in the string
        if(currentText.length() > 1) {
            secondLastChar = currentText.charAt(currentText.length() - 2);
        }

        // If the entire string just contains zero, the default start point, set the current text to
        // an empty string
        if(currentText.equals("0")) {
            switch (value) {
                case '-':
                    resetCurrentText("Current text = '0', resetting to allow new value");

                    processMinus();
                    break;
                case '.':
                    resetCurrentText("Current text = '0', resetting to allow new value");

                    processDecimal();
                    break;
                default:
                    if(Character.isDigit(value)) {
                        resetCurrentText("Current text = '0', resetting to allow new value");

                        processDigit(value);
                    }
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
        Log.i(TAG, DASH_SEPARATOR);
    }

    /**
     * Pretty much a temporary helper method
     *
     * @param logString what to display before making changes
     */
    private void resetCurrentText(String logString) {
        Log.i(TAG, logString);

        currentText = "";

        Log.i(TAG, "Current text value: " + currentText);
    }

    /**
     * Processes the equals input, will calculate the final sum given and sets the preview text
     */
    private void processEquals() {
        updateCurrentBlockString(currentBlockString, true, false);

        Log.i(TAG, "::Blocks::" + System.getProperty("line.separator")
                + blockManager.toString() + "::Blocks::");

        // Get sum from block manager
        double sum = 0.0;
        try {
            sum = blockManager.getBlockEvaluator().calculateTotal();
        } catch (ClassCastException e) {
            Log.e(TAG, "Blocks: " + blockManager.toString());
            e.printStackTrace();
        }

        // Format it so it's presentable
        String sumString = formatNumber(sum);
        // Reset the preview
        resetPreview();
        // Set current text and reset the preview text total to null
        setTextAndScroll(sumString);
        previewTextTotal.setText("");
        // Update the current block
        currentBlockString = sumString;
    }

    /**
     * Processes a digit passed
     *
     * @param digit Number value passed to add to the block string and such
     */
    private void processDigit(final char digit) {
        // If it's a digit, set text
        setTextAndScroll(currentText + digit);

        if(!Character.isDigit(lastChar) && lastChar != '.' && lastChar != '-') {
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
                setTextAndScroll(currentText + 0 + DECIMAL);

                updateCurrentBlockString(0 + String.valueOf(DECIMAL), true, false);
            } else if(Character.isDigit(lastChar)) {
                setTextAndScroll(currentText + DECIMAL);

                updateCurrentBlockString(String.valueOf(DECIMAL), false, false);
            } else {
                // To handle any case where the previous value isn't a number, much like the beginning
                setTextAndScroll(currentText + 0 + DECIMAL);

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
            setTextAndScroll(currentText + MINUS);

            updateCurrentBlockString(String.valueOf(MINUS), true, false);
        }
        // If the last character is a number, we're specifically looking to take away from it
        if(Character.isDigit(lastChar)) {
            setTextAndScroll(currentText + MINUS);

            updateCurrentBlockString(String.valueOf(MINUS), true, false);
        }
        // Otherwise we are starting a new, negative number however, we can only ever have a
        // symbol followed by a minus, nothing else
        if(secondLastChar != '#' && !Character.isDigit(lastChar) && Character.isDigit(secondLastChar)) {
            setTextAndScroll(currentText + MINUS);

            updateCurrentBlockString(String.valueOf(MINUS), true, false);
        }
    }

    /**
     * Processes symbols, pretty much everything except digits, decimal and minus, so plus, mult, divide
     * , percentage etc
     */
    private void processSymbol(final char symbol) {
        // If the last character is a minus
        if(Character.isDigit(lastChar)) {
            /*if(blockManager.isEmpty()) {
                updateCurrentBlockString(String.valueOf(currentText), true, false);
            }*/

            setTextAndScroll(currentText + symbol);

            updateCurrentBlockString(String.valueOf(symbol), true, false);
        }
        // If the last character isn't number
        if(!Character.isDigit(lastChar)) {
            // And if the last character isn't a minus with the second last character being a digit,
            // carry on, otherwise, do nothing and expect a number to be entered as this will
            // signify the start of a negative number
            if(Character.isDigit(secondLastChar)) {
                String subString = currentText.substring(0, currentText.length() - 1);

                setTextAndScroll(subString + symbol);

                updateCurrentBlockString(String.valueOf(symbol), true, true);
            }
        }
    }

    /**
     * Sets the text of the previews and automatically scrolls them to the end
     *
     * @param updatedText the text that we want the preview texts to display
     */
    private void setTextAndScroll(String updatedText) {
        // Update text
        previewTextMain.setText(updatedText);

        // TODO - show current total
        if(blockManager.getBlocks().size() != 0) {
            Log.i(TAG, "Showing total");

            double sum = 0.0;

            try {
                sum = blockManager.getBlockEvaluator().calculateCurrentTotal();
            } catch (Exception e) {
                Log.e(TAG, blockManager.toString());

                StringBuilder stackTrace = new StringBuilder();

                for(StackTraceElement elem : e.getStackTrace()) {
                    stackTrace.append(elem.toString());
                    stackTrace.append(System.getProperty("line.separator"));
                }

                Log.e("AndroidRuntime: ", stackTrace.toString());
            }


            previewTextTotal.setText(formatNumber(sum));
        }

        // Scroll to end
        scrollViewMain.post(new HorizontalAutoScroller(scrollViewMain, scrollViewMain.getChildAt(0).getRight()));
        scrollViewTotal.post(new HorizontalAutoScroller(scrollViewTotal, scrollViewTotal.getChildAt(0).getRight()));
    }

    /**
     * Helper method that updates the currentBlockString as well as modifying the block manager
     * to have the latest data
     *
     * @param value The value to be added to the block string
     * @param finishedState Whether or not the call to this should add to the block manager
     * @param replacePrevious Whether or not the call to this should replace the last object in the
     *                        block manager
     */
    private void updateCurrentBlockString(String value, boolean finishedState, boolean replacePrevious) {
        if(finishedState) {
            if(replacePrevious) {
                // Remove the top element from the block manager
                blockManager.pop();

                try {
                    Double number = Double.parseDouble(currentBlockString);
                    blockManager.createAndAddBlock(number);
                } catch (NumberFormatException e) {
                    Log.w(TAG, "Failed parsing a number, adding character!");

                    if(currentBlockString.length() > 0) {
                        blockManager.createAndAddBlock(MathOperator.getEnumFromCharacter(currentBlockString.charAt(0)));
                    } else {
                        Log.e(TAG, "Failed to add character, currentBlockString is empty");
                    }

                    Log.i(TAG, DASH_SEPARATOR);
                }

                currentBlockString = value;
            } else {
                try {
                    Double number = Double.parseDouble(currentBlockString);
                    blockManager.createAndAddBlock(number);
                } catch(NumberFormatException e) {
                    Log.w(TAG, "Failed parsing a number, adding character!");

                    if(currentBlockString.length() > 0) {
                        blockManager.createAndAddBlock(MathOperator.getEnumFromCharacter(currentBlockString.charAt(0)));
                    } else {
                        Log.e(TAG, "Failed to add character, currentBlockString is empty");
                    }


                    Log.i(TAG, DASH_SEPARATOR);
                }

                currentBlockString = value;
            }

            Log.i(TAG, "Number of blocks: " + blockManager.getBlocks().size());
            Log.i(TAG, "First on deque: " + blockManager.getFirstBlock());
            Log.i(TAG, "Last on deque: " + blockManager.getFinalBlock());
            Log.i(TAG, DASH_SEPARATOR);
        } else {
            currentBlockString += value;
        }
    }

    /**
     * Resets the preview, block manager and current block string
     */
    public void resetPreview() {
        Log.i(TAG, "Resetting preview");

        // Reset the texts to display "0", the default state
        previewTextMain.setText("0");
        previewTextTotal.setText("0");

        // Reset the currentBlockString to an empty string
        currentBlockString = "";

        // Reset the block manager, deleting all blocks
        blockManager.reset();

        // Reset last and second last char parameters to the null value (aka: "/0")
        lastChar = 0;
        secondLastChar = 0;
    }

    /**
     * Deletes the last input from preview text and block in the block manager
     */
    public void deleteLastInput() {
        Log.i(TAG, "Undoing last input");


        //TODO - detect whether or not we need to remove the last block from the block manager

    }

    /**
     * Formats the number so that if it is a whole number, it returns without any decimal and trailing
     * zeros
     *
     * @param number the number to be formatted
     * @return formatted string of the number
     */
    public String formatNumber(double number) {
        if(number == (long) number) {
            return String.format("%d", (long)number);
        } else {
            return String.format("%s", number);
        }
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int desiredWidth = 100;
        final int desiredHeight = 100;

        int width;
        int height;

        if(widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if(widthMode == MeasureSpec.AT_MOST) {
            width = Math.max(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if(heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if(heightMode == MeasureSpec.AT_MOST) {
            height = Math.max(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }*/

    /**
     * Helper class for use with the View.post(Runnable) method.
     * Scrolls the scroller along the X axis
     * Names of variables and purpose are self explanatory.
     *
     * @see #post(Runnable)
     */
    private class HorizontalAutoScroller implements Runnable {
        private View viewToScroll;
        private int xPos;

        public HorizontalAutoScroller(View viewToScroll, int xPos) {
            this.viewToScroll = viewToScroll;
            this.xPos = xPos;
        }

        @Override
        public void run() {
            viewToScroll.scrollTo(xPos, 0);
        }
    }
 }
