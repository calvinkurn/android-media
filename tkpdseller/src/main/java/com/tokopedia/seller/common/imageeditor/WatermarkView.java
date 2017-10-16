package com.tokopedia.seller.common.imageeditor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by User on 10/3/2017.
 */

public class WatermarkView extends View {

    public static final float TEXT_SIZE_DEFAULT = 14;

    private String textString;
    private int xText = 0;
    private int yText = 0;
    private TextPaint mTextPaint = new TextPaint();
    private float mTextSize = TEXT_SIZE_DEFAULT;

    float density;

    public WatermarkView(Context context) {
        this(context, null);
        initView(context);
    }

    public WatermarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WatermarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        density = context.getResources().getDisplayMetrics().density;

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (TextUtils.isEmpty(textString)) {
            return;
        }

        drawText(canvas);
    }

    public void setTextSize(float textSize) {
        this.mTextSize =textSize;
    }

    public void setText(String text) {
        this.textString = text;
        invalidate();
    }

    public void setTextCoord(int x, int y) {
        this.xText = x;
        this.yText = y;
        invalidate();
    }

    public void drawText(Canvas canvas) {
        canvas.drawText(textString, xText, yText, mTextPaint);
    }
}
