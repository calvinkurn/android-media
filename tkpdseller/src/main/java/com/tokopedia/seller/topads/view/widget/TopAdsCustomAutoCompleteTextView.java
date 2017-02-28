package com.tokopedia.seller.topads.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;

/**
 * Created by zulfikarrahman on 2/23/17.
 */

public class TopAdsCustomAutoCompleteTextView extends AutoCompleteTextView {

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

    public TopAdsCustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public TopAdsCustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, popupTheme);
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
        Rect bounds;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            actionX = (int) event.getX();
            actionY = (int) event.getY();

            if (drawableRight != null) {

                bounds = null;
                bounds = drawableRight.getBounds();

                int x, y;
                int extraTapArea = 13;

                /**
                 * IF USER CLICKS JUST OUT SIDE THE RECTANGLE OF THE DRAWABLE
                 * THAN ADD X AND SUBTRACT THE Y WITH SOME VALUE SO THAT AFTER
                 * CALCULATING X AND Y CO-ORDINATE LIES INTO THE DRAWBABLE
                 * BOUND. - this process help to increase the tappable area of
                 * the rectangle.
                 */
                x = (int) (actionX + extraTapArea);
                y = (int) (actionY - extraTapArea);

                /**Since this is right drawable subtract the value of x from the width
                 * of view. so that width - tappedarea will result in x co-ordinate in drawable bound.
                 */
                x = getWidth() - x;

                 /*x can be negative if user taps at x co-ordinate just near the width.
                 * e.g views width = 300 and user taps 290. Then as per previous calculation
                 * 290 + 13 = 303. So subtract X from getWidth() will result in negative value.
                 * So to avoid this add the value previous added when x goes negative.
                 */

                if(x <= 0){
                    x += extraTapArea;
                }

                 /* If result after calculating for extra tappable area is negative.
                 * assign the original value so that after subtracting
                 * extratapping area value doesn't go into negative value.
                 */

                if (y <= 0)
                    y = actionY;

                /**If drawble bounds contains the x and y points then move ahead.*/
                if (bounds.contains(x, y)) {
                    resetView();
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
                return super.onTouchEvent(event);
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
            setCompoundDrawables(null, null, drawableRight, null);
        }else{
            setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
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
    }

}
