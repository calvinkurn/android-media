package com.tokopedia.shop.page.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.shop.R;

/**
 * Created by hendry on 7/10/2017.
 */

public class ShopPageSubDetailView extends FrameLayout {

    private FrameLayout frameLayout;
    private TextView titleTextView;

    private String titleText;

    public ShopPageSubDetailView(Context context) {
        super(context);
        init();
    }

    public ShopPageSubDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShopPageSubDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ShopPageSubDetailView);
        try {
            titleText = styledAttributes.getString(R.styleable.ShopPageSubDetailView_spsd_title);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_shop_page_sub_detail_view, this);
        titleTextView = view.findViewById(R.id.text_view_title);
        frameLayout = view.findViewById(R.id.frame_layout_content);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTextView.setText(titleText);
        invalidate();
        requestLayout();
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.linear_layout_container) {
            // Carry on adding the View...
            super.addView(child, index, params);
        } else {
            // remove the link between child and previous parent before add (if any)
            if (child.getParent() != null) {
                ViewGroup viewParent = (ViewGroup) child.getParent();
                viewParent.removeView(child);
            }
            frameLayout.addView(child, params);
        }
    }
}