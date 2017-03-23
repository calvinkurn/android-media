package com.tokopedia.seller.shop.open.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;

import com.stepstone.stepper.internal.widget.StepTab;
import com.stepstone.stepper.internal.widget.TabsContainer;
import com.tokopedia.seller.R;

import java.util.List;

/**
 * Created by Nathaniel on 3/22/2017.
 */

public class TkpdTabsContainer extends TabsContainer {

    private List<CharSequence> mStepTitles;
    private LinearLayout mTabsInnerContainer;

    public TkpdTabsContainer(Context context) {
        super(context);
        initializeView();
    }

    public TkpdTabsContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public TkpdTabsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    private void initializeView() {
        mTabsInnerContainer = (LinearLayout) findViewById(com.stepstone.stepper.R.id.ms_stepTabsInnerContainer);
    }

    @Override
    public void setSteps(List<CharSequence> stepTitles) {
        super.setSteps(stepTitles);
        TkpdStepTab tab = (TkpdStepTab) mTabsInnerContainer.getChildAt(0);
        tab.toggleLeftDividerVisibility(false);
    }
}
