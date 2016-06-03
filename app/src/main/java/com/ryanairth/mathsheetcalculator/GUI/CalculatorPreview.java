package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ryanairth.mathsheetcalculator.Exceptions.InvalidMathOperatorException;
import com.ryanairth.mathsheetcalculator.Math.Block;
import com.ryanairth.mathsheetcalculator.Math.BlockManager;
import com.ryanairth.mathsheetcalculator.Math.MathOperator;
import com.ryanairth.mathsheetcalculator.R;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

/**
 * Created by Ryan Airth (Sweeney) on 16/01/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class CalculatorPreview extends RelativeLayout {
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
     *  Getter method for the previewTextMain {@link TextView}
     *
     *  @see TextView
     *
     * @return previewTextMain, a TextView
     */
    public TextView getPreviewTextMain() {
        return previewTextMain;
    }

    /**
     *  Getter method for the previewTextTotal {@link TextView}
     *
     *  @see TextView
     *
     * @return previewTextTotal, a TextView
     */
    public TextView getPreviewTextTotal() {
        return previewTextTotal;
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
    }

    /**
     * Scrolls both texts to the end in a given direction, uses View.FOCUS_LEFT/RIGHT
     *
     * @param direction direction to scroll the text in
     */
    public void scrollText(int direction) {
        switch (direction) {
            case FOCUS_LEFT:
                scrollViewMain.post(new HorizontalAutoScroller(scrollViewMain, scrollViewMain.getChildAt(0).getLeft()));
                scrollViewTotal.post(new HorizontalAutoScroller(scrollViewTotal, scrollViewTotal.getChildAt(0).getLeft()));
                break;
            case FOCUS_RIGHT:
                scrollViewMain.post(new HorizontalAutoScroller(scrollViewMain, scrollViewMain.getChildAt(0).getRight()));
                scrollViewTotal.post(new HorizontalAutoScroller(scrollViewTotal, scrollViewTotal.getChildAt(0).getRight()));
                break;
        }
    }

    /**
     * Resets the preview texts
     */
    public void resetPreview() {
        previewTextMain.setText("0");
        previewTextTotal.setText("0");
    }

    /**
     * For use with the View.post(Runnable) method.
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
