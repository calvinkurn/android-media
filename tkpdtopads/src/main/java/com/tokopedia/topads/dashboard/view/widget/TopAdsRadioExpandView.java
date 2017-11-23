package com.tokopedia.topads.dashboard.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.tokopedia.topads.R;

/**
 * Created by zulfikarrahman on 2/22/17.
 */

public class TopAdsRadioExpandView extends LinearLayout {

    private TopAdsCustomRadioButton radioButton;
    private View childView;
    private String titleText;
    private boolean radioChecked;
    private int radioId = NO_ID;

    public TopAdsRadioExpandView(Context context) {
        super(context);
        init();
    }

    public TopAdsRadioExpandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TopAdsRadioExpandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public TopAdsRadioExpandView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    public int getOrientation() {
        return VERTICAL;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childs = getChildCount();
        if (childs > 0) {
            childView = getChildAt(1);
        }
        radioButton.setText(titleText);
        radioButton.setChecked(radioChecked);
        setVisibleChildView(radioChecked);
        radioButton.addOnCheckedChangeListener(onCheckedChangeRadio());
        invalidate();
        requestLayout();
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeRadio() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setVisibleChildView(b);
            }
        };
    }

    private void setVisibleChildView(boolean b) {
        if (childView != null) {
            if (b) {
                childView.setVisibility(VISIBLE);

            } else {
                childView.setVisibility(GONE);
            }
        }
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TopAdsRadioExpand);
        try {
            titleText = styledAttributes.getString(R.styleable.TopAdsRadioExpand_title_radio);
            radioChecked = styledAttributes.getBoolean(R.styleable.TopAdsRadioExpand_checked, false);
            radioId = styledAttributes.getResourceId(R.styleable.TopAdsRadioExpand_radio_id, NO_ID);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_radio_expand_view, this);
        radioButton = (TopAdsCustomRadioButton) view.findViewById(R.id.radio_button);
        radioButton.setId(radioId);
        setOrientation(VERTICAL);
    }

    public void setChecked(boolean b) {
        radioButton.setChecked(b);
    }

}
