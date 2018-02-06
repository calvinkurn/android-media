package com.tokopedia.inbox.inboxchat.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.util.MethodChecker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


/**
 * Created by Steven on 2/1/2018.
 */
public class ImageUploadHandlerChat {

    public static final int CODE_UPLOAD_IMAGE = 789;
    public static final String FILELOC = "fileloc";
    private static final String TAG = ImageUploadHandlerChat.class.getSimpleName();

    public static ImageUploadHandlerChat createInstance(Fragment fragment) {
        ImageUploadHandlerChat uploadimage = new ImageUploadHandlerChat();
        uploadimage.fragment = fragment;
        uploadimage.context = fragment.getActivity();
        return uploadimage;
    }

    public class Model {
        public String cameraFileLoc;
    }

    public static final int REQUEST_CODE = 7777;

    private Fragment fragment;
    private Context context;
    private Model model = new Model();

    public String actionCamera2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        startActivity(intent, REQUEST_CODE);
        return model.cameraFileLoc;
    }

    private void startActivity(Intent intent, int code) {
        fragment.startActivityForResult(intent, code);
    }

    private Uri getOutputMediaFileUri() {
        return MethodChecker.getUri(context, getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        String imageCode = uniqueCode();
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + "Tokopedia" + File.separator);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + imageCode + ".jpg");
        model.cameraFileLoc = Environment.getExternalStorageDirectory() + File.separator
                + "Tokopedia" + File.separator + "IMG_" + imageCode + ".jpg";
        return mediaFile;
    }

    private String uniqueCode() {
        String IDunique = UUID.randomUUID().toString();
        String id = IDunique.replaceAll("-", "");
        String code = id.substring(0, 16);
        return code;
    }

    public String getCameraFileloc() {
        return model.cameraFileLoc;
    }

    public void setImageBitmap(String fileloc) {
        this.model.cameraFileLoc = fileloc;
    }

    public static File writeImageToTkpdPath(InputStream source) throws IOException {
        OutputStream outStream = null;
        File dest = null;
        File directory = new File(FileUtils.getFolderPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath()));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        dest = new File(directory.getAbsolutePath() + "/image.jpg");

        outStream = new FileOutputStream(dest);

        byte[] buffer = new byte[1024];

        int length;
        //copy the file content in bytes
        while ((length = source.read(buffer)) > 0) {

            outStream.write(buffer, 0, length);

        }

        source.close();
        outStream.close();

        Log.d(TAG, "File is copied successful!");

        return dest;
    }

    public static File writeImageToTkpdPath(File source) throws IOException {
        InputStream inStream = null;
        OutputStream outStream = null;
        File dest = null;
        File directory = new File(FileUtils.getFolderPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath()));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        dest = new File(directory.getAbsolutePath() + "/image.jpg");

        inStream = new FileInputStream(source);
        outStream = new FileOutputStream(dest);

        byte[] buffer = new byte[1024];

        int length;
        //copy the file content in bytes
        while ((length = inStream.read(buffer)) > 0) {

            outStream.write(buffer, 0, length);

        }

        inStream.close();
        outStream.close();

        Log.d(TAG, "File is copied successful!");

        return dest;
    }

    public static File writeImageToTkpdPath(byte[] buffer) throws IOException {
        File directory = new File(FileUtils.getFolderPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath()));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File photo = new File(directory.getAbsolutePath() + "/image.jpg");

        if (photo.exists()) {
            photo.delete();
        }

        FileOutputStream fos = new FileOutputStream(photo.getPath());

        fos.write(buffer);
        fos.close();

        return photo;
    }

    public static byte[] compressImage(String path) throws IOException {
        Log.d(TAG, "lokasi yang mau diupload " + path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.Options checksize = new BitmapFactory.Options();
        checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
        checksize.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, checksize);
        options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
        Bitmap tempPic = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = null;

        tempPic = ImageHandler.RotatedBitmap(tempPic, path);

        if (tempPic.getWidth() > 2048 || tempPic.getHeight() > 2048) {
            bitmap = ImageHandler.ResizeBitmap(tempPic, 2048);
        }
        else {
            bitmap = tempPic;
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }

    public static boolean checkSizeOverLimit(int fileSizeInBytes, int limit){

        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;

        return (fileSizeInMB > limit);
    }

}
