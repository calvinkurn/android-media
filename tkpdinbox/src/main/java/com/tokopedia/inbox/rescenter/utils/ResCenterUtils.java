package com.tokopedia.inbox.rescenter.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.myproduct.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hangnadi on 2/10/16.
 */
@SuppressWarnings("unused")
public class ResCenterUtils {

    private static final int STATE_VISIBLE = 1;
    private static final String TAG = ResCenterUtils.class.getSimpleName();

    public static void setMenuItemVisibility(MenuItem menuItem, Integer showState) {
        if (showState == STATE_VISIBLE) {
            menuItem.setVisible(true);
        } else {
            menuItem.setVisible(false);
        }
    }

    public static void showSnackbar(@NonNull Activity activity, String message, int duration) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, duration).show();
    }

    public static void showSnackbarAsAction(@NonNull Activity activity, String message, String actionName, View.OnClickListener listener) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
                .setAction(actionName, listener)
                .show();
    }

    public static Bitmap getBitmapFromFile(String fileLoc) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = getInSampleSize(fileLoc);

        Bitmap uploadImage = BitmapFactory.decodeFile(fileLoc, options);
        uploadImage = ImageHandler.RotatedBitmap(uploadImage, fileLoc);
        return uploadImage;
    }

    private static int getInSampleSize(String fileLoc) {
        BitmapFactory.Options checksize = new BitmapFactory.Options();
        checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
        checksize.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileLoc, checksize);
        return ImageHandler.calculateInSampleSize(checksize);
    }

    public static File writeImageToTkpdPath(InputStream source){
        OutputStream outStream = null;
        File dest = null;
        try{

            File directory = new File(FileUtils.getFolderPathForUploadRandom());
            if(!directory.exists()){
                directory.mkdirs();
            }
            dest=new File(directory.getAbsolutePath()+"/image.jpg");

            outStream = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = source.read(buffer)) > 0){

                outStream.write(buffer, 0, length);

            }

            source.close();
            outStream.close();

            Log.d(TAG, "File is copied successful!");

        }catch(IOException e){
            e.printStackTrace();
        }

        return dest;
    }

    public static File writeImageToTkpdPath(File source){
        InputStream inStream = null;
        OutputStream outStream = null;
        File dest = null;
        try{

            File directory = new File(FileUtils.getFolderPathForUploadRandom());
            if(!directory.exists()){
                directory.mkdirs();
            }
            dest=new File(directory.getAbsolutePath()+"/image.jpg");

            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0){

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

            Log.d(TAG, "File is copied successful!");

        } catch(IOException e) {
            e.printStackTrace();
        }

        return dest;
    }

    @NonNull
    public static File writeImageToTkpdPath(byte[] buffer) {
        File directory = new File(FileUtils.getFolderPathForUploadRandom());
        if(!directory.exists()){
            directory.mkdirs();
        }
        File photo=new File(directory.getAbsolutePath()+"/image.jpg");

        if (photo.exists()) {
            photo.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(photo.getPath());

            fos.write(buffer);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
            return null;
        }
        return photo;
    }
}
