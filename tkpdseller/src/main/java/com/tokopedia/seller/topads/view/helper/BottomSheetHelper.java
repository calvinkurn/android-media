package com.tokopedia.seller.topads.view.helper;

import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

/**
 * @author normansyahputa on 2/13/17.
 */

public class BottomSheetHelper {
    private final View peakView;
    private int actionBarSize;
    private BottomSheetBehavior mBottomSheetBehavior;
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback;
    private int height;

    public BottomSheetHelper(BottomSheetBehavior mBottomSheetBehavior,
                             View viewById, int actionBarSize) {
        this.mBottomSheetBehavior = mBottomSheetBehavior;

        //Let's peek it, programmatically
        peakView = viewById;
        this.actionBarSize = actionBarSize;
    }

    public void showBottomSheet() {
        mBottomSheetBehavior.setPeekHeight((int) (height + (0.50 * peakView.getHeight()) + actionBarSize));
        peakView.requestLayout();
    }

    public void dismissBottomSheet() {
        mBottomSheetBehavior.setPeekHeight(0);
        peakView.requestLayout();
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback bottomSheetCallback) {
        this.bottomSheetCallback = bottomSheetCallback;

        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
        }
    }

    public void collapse() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void expanded() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public int getState() {
        return mBottomSheetBehavior.getState();
    }
}
