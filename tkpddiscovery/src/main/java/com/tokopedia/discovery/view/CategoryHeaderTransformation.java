package com.tokopedia.discovery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by alifa on 3/22/17.
 */

public class CategoryHeaderTransformation extends BitmapTransformation {

    public static double CATEGORY_HEADER_CROP = 0.19;

    public CategoryHeaderTransformation(Context context) {
        super();
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return bitmapChanger(toTransform, outWidth, outHeight);
    }

    private static Bitmap bitmapChanger(Bitmap bitmap, int desiredWidth, int desiredHeight) {
        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();

        float scaleX = desiredWidth / originalWidth;
        float scaleY = desiredHeight / originalHeight;
        float scale = Math.max(scaleX, scaleY);

        int crop = (int) (CATEGORY_HEADER_CROP * originalWidth);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, crop, 0, (int) originalWidth-crop, (int) originalHeight, matrix, true);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}