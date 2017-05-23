package com.tokopedia.seller.lib.williamchart.tooltip;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 5/18/17.
 */

class TooltipPointerView extends View {

    private Paint mPaint;
    private int color;

    public TooltipPointerView(Context context) {
        super(context);
    }

    public TooltipPointerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TooltipPointerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TooltipPointerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TooltipPointerAttrs);
        try {
            color = styledAttributes.getColor(R.styleable.TooltipPointerAttrs_tooltip_pointer_color, 0);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path p = new Path();
        p.moveTo(0, 0);
        p.lineTo(getWidth() / 2, getHeight());
        p.lineTo(getWidth(), 0);
        p.lineTo(0, 0);
        canvas.drawPath(p, mPaint);
    }
}
