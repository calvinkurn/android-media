package com.tokopedia.topads.dashboard.view.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tokopedia.topads.R;

/**
 * Created by zulfikarrahman on 2/23/17.
 */

public class TopAdsCustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    private Drawable drawableRight;

    int actionX, actionY;

    public TopAdsCustomAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    public TopAdsCustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TopAdsCustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        if (right != null) {
            drawableRight = right;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    protected void onFinishInflate() {
        setVisibleDrawableRight(false);
        super.onFinishInflate();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if (drawableRight != null) {
                if (event.getRawX() >= (getRight() - drawableRight.getBounds().width())) {
                    resetView();
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void init(AttributeSet attrs) {
        init();
//        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TopAdsRadioExpand);
//        try {
//        } finally {
//            styledAttributes.recycle();
//        }
    }


    private void init() {
//        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher);
//        setCompoundDrawables(null, null, drawable, null);
    }


    @Override
    protected void finalize() throws Throwable {
        drawableRight = null;
        super.finalize();
    }

    public void setVisibleDrawableRight(boolean isVisible){
        if(isVisible){
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_green, 0);
        }else{
            setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_down_wpadding,0);
        }
        invalidate();
        requestLayout();
    }

    protected void resetView() {
        setEnabled(true);
        setVisibleDrawableRight(false);
        setText("");
    }

    public void lockView(){
        setEnabled(false);
        setVisibleDrawableRight(true);
        dismissDropDown();
    }

    public void performFiltering(CharSequence text){
        super.performFiltering(text, 0);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    public void showDropDownFilter(){
        if(isEnabled()) {
            performFiltering(getText());
            showDropDown();
        }
    }
}

