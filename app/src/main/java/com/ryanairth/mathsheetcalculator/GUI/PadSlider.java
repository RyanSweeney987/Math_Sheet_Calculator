package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ryanairth.mathsheetcalculator.R;

import java.util.ArrayList;
import java.util.List;

import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

//import com.ryanairth.mathsheetcalculator.R;

/**
 * Created by Ryan Airth (Sweeney) on 27/02/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class PadSlider extends LinearLayout {
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private List<View> pagerViews;

    public PadSlider(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    /**
     * Adds the view to pagerViews list
     *
     * @param view The view to be added to the pagerViews list
     */
    public void addPagerView(View view) {
        pagerViews.add(view);
        pagerAdapter.notifyDataSetChanged();
    }

    /**
     * Initializes all the main stuff for the class
     *
     * @param context required to inflate the pad slider layout as well as passing on to the
     *                ScreenSlidePagerAdapter object
     */
    private void init(Context context) {
        setId(R.id.calculator_pad_slider);

        pagerViews = new ArrayList<>();

        // Inflate the view pager from the layout as opposed to instantiating it
        viewPager = (ViewPager)inflate(context, R.layout.pad_slider_layout ,null);
        pagerAdapter = new ScreenSlidePagerAdapter(context);

        viewPager.setAdapter(pagerAdapter);

        super.addView(viewPager);
    }

    /**
     * Custom pager adapter for screen sliding
     */
    private class ScreenSlidePagerAdapter extends PagerAdapter {
        private Context context;

        public ScreenSlidePagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            ViewGroup layout = (ViewGroup) pagerViews.get(position);

            collection.addView(layout);

            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return pagerViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /*@Override
        public CharSequence getPageTitle(int position) {
            PagerEnum pagerEnum = PagerEnum.values()[position];
            return context.getString(pagerEnum.getTitleId());
        }*/
    }
}
