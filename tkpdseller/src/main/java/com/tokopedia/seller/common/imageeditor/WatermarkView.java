package com.tokopedia.seller.common.imageeditor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.seller.R;

/**
 * Created by Hendry on 10/3/2017.
 */

public class WatermarkView extends View {

    public static final float TEXT_SIZE_MIN = 14; // dp
    public static final float TEXT_SIZE_MAX = 28; // dp

    public static final float PADDING_DEFAULT = 12; // dp
    public static final float WIDTH_MIN_FONT = 300; // dp
    public static final float WIDTH_MAX_FONT = 600; // dp

    private String textString;
    private int xText = 0;
    private int yText = 0;
    private TextPaint mTextPaint = new TextPaint();
    private float mTextSize = TEXT_SIZE_MIN;
    private RectF windowRect = new RectF();

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

        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.watermark_text_color));
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(mTextSize * density);
        mTextPaint.setStyle(Paint.Style.STROKE);
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
        this.mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize * density);
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setWindowRect(@NonNull RectF windowRect) {
        if (!this.windowRect.equals(windowRect)) {
            float left = windowRect.left;
            float right = windowRect.right;
            float top = windowRect.top;
            float bottom = windowRect.bottom;

            this.windowRect = new RectF(left, top, right, bottom);
            int width = (int) (right - left);
            setTextSize(getCalcTextSize(width));
            setTextCoord((int) left, (int) bottom);
        }
    }

    public void setText(String text) {
        this.textString = text;
        invalidate();
    }

    private float getCalcTextSize(int actualWidth) {
        if (actualWidth >= WIDTH_MAX_FONT) {
            return TEXT_SIZE_MAX;
        } else if (actualWidth <= WIDTH_MIN_FONT) {
            return (TEXT_SIZE_MIN);
        } else {
            return TEXT_SIZE_MIN + (actualWidth - WIDTH_MIN_FONT) * (TEXT_SIZE_MAX - TEXT_SIZE_MIN) / (WIDTH_MAX_FONT - WIDTH_MIN_FONT);
        }
    }

    public void setTextCoord(int x, int y) {
        this.xText = x + (int) (PADDING_DEFAULT * density);
        this.yText = y - (int) (PADDING_DEFAULT * density);
        invalidate();
    }

    public void drawText(Canvas canvas) {
        canvas.drawText(textString, xText, yText, mTextPaint);
    }
}
