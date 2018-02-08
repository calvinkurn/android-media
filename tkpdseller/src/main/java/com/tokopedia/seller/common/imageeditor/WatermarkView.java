package com.tokopedia.seller.common.imageeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Hendry on 10/3/2017.
 */

public class WatermarkView extends View {

    public static final float TEXT_SIZE_MIN = 8; // dp
    public static final float TEXT_SIZE_MAX = 32; // dp

    public static final float PADDING_DEFAULT = 12; // dp
    public static final float WIDTH_MIN_FONT = 150; // dp
    public static final float WIDTH_MAX_FONT = 600; // dp
    public static final int TEXT_LENGTH_THRESHOLD = 18;
    public static final float TEXT_LENGTH_MULTIPLIER = 0.7f;
    public static final String TEMP_FILE_NAME = "temp.tmp";
    public static final float PADDING_TEXT_RATIO = 2f / 3;

    private CharSequence textString;
    private int xText = 0;
    private int yText = 0;
    private TextPaint mTextPaint = new TextPaint();
    private float mTextSize = TEXT_SIZE_MIN;
    private RectF windowRect = new RectF();
    private float paddingDefault = PADDING_DEFAULT;

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
        if (!this.windowRect.equals(windowRect) &&
                !TextUtils.isEmpty(textString)) {
            float left = windowRect.left;
            float right = windowRect.right;
            float top = windowRect.top;
            float bottom = windowRect.bottom;

            this.windowRect = new RectF(left, top, right, bottom);
            int width = (int) (right - left);
            setTextSize(getCalcTextSize(width, textString.toString()));
            paddingDefault = mTextSize * PADDING_TEXT_RATIO;
            setTextCoord((int) left, (int) bottom);
        }
    }

    public void setText(String text) {
        this.textString = MethodChecker.fromHtml(text);
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
        this.xText = x + (int) (paddingDefault * density);
        this.yText = y - (int) (paddingDefault * density);
        invalidate();
    }

    public void drawText(Canvas canvas) {
        canvas.drawText(textString,0, textString.length(), xText, yText, mTextPaint);
    }

    public Bitmap drawTo(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        if (TextUtils.isEmpty(textString)) {
            return bitmap;
        }
        Bitmap mutableBitmap;
        try {
            mutableBitmap = convertToMutable(bitmap);
        } catch (Exception e) {
            return bitmap;
        }
        int newWidth = mutableBitmap.getWidth();
        int oldWidth = (int) (windowRect.right - windowRect.left);
        float ratio = (float) newWidth / oldWidth;

        Canvas canvas = new Canvas(mutableBitmap);
        TextPaint watermarkTextPaint = new TextPaint();
        setDefaultTextPaint(watermarkTextPaint);
        watermarkTextPaint.setTextSize((int) (mTextSize * ratio * density + 0.5f));

        int padding = (int) (paddingDefault * ratio * density);
        int xText = padding;
        int yText = mutableBitmap.getHeight() - padding;

        if (!TextUtils.isEmpty(textString)) {
            canvas.drawText(textString, 0, textString.length(), xText, yText, watermarkTextPaint);
        }
        bitmap.recycle();

        return mutableBitmap;
    }

    /**
     * Converts a immutable bitmap to a mutable bitmap. This operation doesn't allocates
     * more memory that there is already allocated.
     *
     * @param imgIn - Source image. It will be released, and should not be used more
     * @return a copy of imgIn, but muttable.
     */
    public static Bitmap convertToMutable(Bitmap imgIn) throws Exception {
        //this is the file going to use temporally to save the bytes.
        // This file will not be a image, it will store the raw image data.
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + TEMP_FILE_NAME);

        //Open an RandomAccessFile
        //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        //into AndroidManifest.xml file
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

        // get the width and height of the source bitmap.
        int width = imgIn.getWidth();
        int height = imgIn.getHeight();
        Bitmap.Config type = imgIn.getConfig();

        //Copy the byte to the file
        //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
        imgIn.copyPixelsToBuffer(map);
        //recycle the source bitmap, this will be no longer used.
        imgIn.recycle();
        System.gc();// try to force the bytes from the imgIn to be released

        //Create a new bitmap to load the bitmap again. Probably the memory will be available.
        imgIn = Bitmap.createBitmap(width, height, type);
        map.position(0);
        //load it back from temporary
        imgIn.copyPixelsFromBuffer(map);
        //close the temporary file and channel , then delete that also
        channel.close();
        randomAccessFile.close();

        // delete the temp file
        file.delete();
        return imgIn;
    }
}
