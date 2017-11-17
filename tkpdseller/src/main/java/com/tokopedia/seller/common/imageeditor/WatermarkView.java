package com.tokopedia.seller.common.imageeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
    public static final int TEXT_LENGTH_THRESHOLD = 18;
    public static final float TEXT_LENGTH_MULTIPLIER = 0.8f;

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
        setDefaultTextPaint(mTextPaint);
    }

    private void setDefaultTextPaint(TextPaint textPaint) {
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.watermark_text_color));
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(mTextSize * density);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.LEFT);
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
            setTextSize(getCalcTextSize(width, textString));
            setTextCoord((int) left, (int) bottom);
        }
    }

    public void setText(String text) {
        this.textString = text;
        invalidate();
    }

    private float getCalcTextSize(int actualWidth, String textString) {
        float textMin = TEXT_SIZE_MIN;
        float textMax = TEXT_SIZE_MAX;
        if (!TextUtils.isEmpty(textString)) {
            if (textString.length() >= TEXT_LENGTH_THRESHOLD) {
                textMin = TEXT_LENGTH_MULTIPLIER * TEXT_SIZE_MIN;
                textMax = TEXT_LENGTH_MULTIPLIER * TEXT_SIZE_MAX;
            }
        }
        if (actualWidth >= WIDTH_MAX_FONT) {
            return textMax;
        } else if (actualWidth <= WIDTH_MIN_FONT) {
            return (textMin);
        } else {
            return textMin + (actualWidth - WIDTH_MIN_FONT) * (textMax - textMin) / (WIDTH_MAX_FONT - WIDTH_MIN_FONT);
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

    public Bitmap drawTo(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int newWidth = mutableBitmap.getWidth();
        int oldWidth = (int) (windowRect.right - windowRect.left);
        float ratio = (float) newWidth / oldWidth;

        Canvas canvas = new Canvas(mutableBitmap);
        TextPaint watermarkTextPaint = new TextPaint();
        setDefaultTextPaint(watermarkTextPaint);
        watermarkTextPaint.setTextSize((int) (mTextSize * ratio * density + 0.5f));

        int padding = (int) (PADDING_DEFAULT * ratio * density);
        int xText = padding;
        int yText = mutableBitmap.getHeight() - padding;

        canvas.drawText(textString, xText, yText, watermarkTextPaint);

        bitmap.recycle();

        return mutableBitmap;
    }
}
