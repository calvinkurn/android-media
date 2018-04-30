package com.tokopedia.transaction.bcaoneklik.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class CreditCardViewLayout extends FrameLayout {

    private static final int CREDIT_CARD_WIDTH = 270;
    private static final int CREDIT_CARD_HEIGHT = 160;

    public CreditCardViewLayout(@NonNull Context context) {
        super(context);
    }

    public CreditCardViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CreditCardViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, width * CREDIT_CARD_HEIGHT / CREDIT_CARD_WIDTH);

        Log.d("CreditCardView width", MeasureSpec.toString(widthMeasureSpec));
        Log.d("CreditCardView height", MeasureSpec.toString(heightMeasureSpec));
    }

}
