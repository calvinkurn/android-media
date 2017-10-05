package com.tokopedia.seller.myproduct.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tkpd.library.utils.ImageHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by m.normansyah on 03/12/2015.
 * start support for multi fragment. 8/4/2016
 */
public class AddProductFragment {

    public static byte[] compressImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.Options checksize = new BitmapFactory.Options();
        checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
        checksize.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, checksize);
        options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
        Bitmap tempPic = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        Bitmap tempPicToUpload = null;
        if (tempPic != null) {
            try {
                tempPic = new ImageHandler().RotatedBitmap(tempPic, path);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (tempPic.getWidth() > 2048 || tempPic.getHeight() > 2048) {
                tempPicToUpload = new ImageHandler().ResizeBitmap(tempPic, 2048);
            } else {
                tempPicToUpload = tempPic;
            }
            tempPicToUpload.compress(Bitmap.CompressFormat.JPEG, 70, bao);
            return bao.toByteArray();
        }
        return null;
    }
}

