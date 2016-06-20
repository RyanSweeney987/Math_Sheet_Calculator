package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.ryanairth.mathsheetcalculator.R;
import com.ryanairth.mathsheetcalculator.Util.PreviewUpdateDispatcher;
import com.ryanairth.mathsheetcalculator.Util.PreviewUpdateListener;

import java.util.List;

/**
 * Created by Ryan Airth (Sweeney) on 27/02/2016.
 * Copyright information found in accompanying License.txt file.
 */
<<<<<<< HEAD
public class CalculatorNumpad extends LinearLayout {
    // TODO - better link to xml file

=======
public class CalculatorNumpad extends LinearLayout implements PreviewUpdateDispatcher{
>>>>>>> Calc_Preview
    /**
     * Grid layout to put the numbers and such in, for some reason this is really needed or odd gaps
     * appear between the buttons
     */
    private GridLayout gridLayout;
    /**
     * Buttons that are numbers
     */
    private Button one, two, three, four, five, six, seven, eight, nine, zero;
    /**
     * Buttons that are symbols or provide some other user, such as reset or delete
     */
    private Button equals, plus, minus, multiply, divide, decimal, ce, percentage, del, other2;
    /**
     * Receiver for all the preview update events
     */
    private PreviewUpdateListener listener;

    public CalculatorNumpad(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    /**
     * Set the {@link PreviewUpdateListener} that receives all the update events
     *
     * @see PreviewUpdateListener
     *
     * @param listener listener
     */
    @Override
    public void setPreviewUpdateListener(PreviewUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * Initializes all the main stuff for the class
     *
     * @param context The context required to inflate the numpad layout which this class will get
     *                details from
     */
    private void init(Context context) {
        setGravity(Gravity.CENTER);
        setId(R.id.calculator_number_pad);

        gridLayout = (GridLayout) inflate(context, R.layout.numpad_calc_layout, null);

        addView(gridLayout);

        one = (Button) gridLayout.findViewById(R.id.numpad_one);
        two = (Button) gridLayout.findViewById(R.id.numpad_two);
        three = (Button) gridLayout.findViewById(R.id.numpad_three);
        four = (Button) gridLayout.findViewById(R.id.numpad_four);
        five = (Button) gridLayout.findViewById(R.id.numpad_five);
        six = (Button) gridLayout.findViewById(R.id.numpad_six);
        seven = (Button) gridLayout.findViewById(R.id.numpad_seven);
        eight = (Button) gridLayout.findViewById(R.id.numpad_eight);
        nine = (Button) gridLayout.findViewById(R.id.numpad_nine);
        zero = (Button) gridLayout.findViewById(R.id.numpad_zero);

        equals = (Button) gridLayout.findViewById(R.id.numpad_equals);
        plus = (Button) gridLayout.findViewById(R.id.numpad_plus);
        minus = (Button) gridLayout.findViewById(R.id.numpad_minus);
        multiply = (Button) gridLayout.findViewById(R.id.numpad_multiply);
        divide = (Button) gridLayout.findViewById(R.id.numpad_divide);
        decimal = (Button) gridLayout.findViewById(R.id.numpad_decimal);
        percentage = (Button) gridLayout.findViewById(R.id.numpad_percent);
        ce = (Button) gridLayout.findViewById(R.id.numpad_ce);
        del = (Button) gridLayout.findViewById(R.id.numpad_del);

        setUpNumberButtons();
        setUpMathButtons();
    }

    /**
     * Method to separate things up to make things much cleaner as opposed to having a god method
     * All buttons implement a custom OnClickListener that also sends the number data to the
     * {@link CalculatorPreview}
     */
    private void setUpNumberButtons() {
        one.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_one).charAt(0));
            }
        });

        two.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_two).charAt(0));
            }
        });

        three.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_three).charAt(0));
            }
        });

        four.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_four).charAt(0));
            }
        });

        five.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_five).charAt(0));
            }
        });

        six.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_six).charAt(0));
            }
        });

        seven.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_seven).charAt(0));
            }
        });

        eight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_eight).charAt(0));
            }
        });

        nine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_nine).charAt(0));
            }
        });

        zero.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_zero).charAt(0));
            }
        });
    }

    /**
     * Method to separate things up to make things much cleaner as opposed to having a god method
     * All buttons implement a custom OnClickListener that also sends the symbol or extra data to the
     * {@link CalculatorPreview}
     */
    private void setUpMathButtons() {
        equals.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_equals).charAt(0));
            }
        });

        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_plus).charAt(0));
            }
        });

        minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_minus).charAt(0));
            }
        });

        multiply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_multiply).charAt(0));
            }
        });

        divide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_divide).charAt(0));
            }
        });

        decimal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_decimal).charAt(0));
            }
        });

        percentage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updatePreview(getResources().getString(R.string.numpad_percent).charAt(0));
            }
        });

        ce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.resetPreview();
            }
        });

        del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteLastInput();
            }
        });
    }
}
