package com.tokopedia.seller.shop.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.myproduct.model.GenerateHostModel;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.myproduct.utils.MetadataUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by m.normansyah on 18/12/2015.
 * still using old network handler, just to make sure retrofit 2 upload work
 *
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class UploadPhotoShopTask extends AsyncTask<byte[], String, String> {
    public static final String LOGO = "logo";
    public static final String NEW_ADD = "new_add";
    public static final String RESOLUTION = "resolution";
    public static final String SERVER_ID = "server_id";
    public static final String RESOLUTION_DEFAULT_VALUE = "300";
    public static final String GOLANG = "2";
    public static final String TAG = "MNORMANSYAH";
    public static final String messageTAG = UploadPhotoShopTask.class.getSimpleName() + " : ";
    String path;
    GenerateHostModel.GenerateHost generateHost;
    String token;
    String userId;
    String deviceId;

    public UploadPhotoShopTask(String userId, String deviceId, GenerateHostModel.GenerateHost generateHost, String path, String token) {
        this(generateHost, path, token);
        this.userId = userId;
        this.deviceId = deviceId;
    }

    @Deprecated
    public UploadPhotoShopTask(GenerateHostModel.GenerateHost generateHost, String path, String token) {
        this.path = path;
        this.generateHost = generateHost;
        this.token = token;
    }

    @Override
    protected String doInBackground(byte[]... jpeg) {
        Log.d(TAG, messageTAG + FileUtils.getPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath(), "image", "jpg"));
//        File photo=new File(Environment.getExternalStorageDirectory(), "image.jpg");// old
        File photo = writeImageToTkpdPath(jpeg[0]);

        //[START] save to db for images
        Pair<Integer, Integer> resolution = null;
        try {
            resolution = MetadataUtil.getWidthFromImage(photo.getAbsolutePath());
        } catch (MetadataUtil.UnSupportedimageFormatException e) {
            e.printStackTrace();
        }
        int width = resolution.getModel1();
        int height = resolution.getModel2();
        PictureDB pictureDB = new PictureDB(path, width, height);
        pictureDB.save();
        Log.d(TAG, messageTAG + " hasil save ke db : " + pictureDB.Id);
        //[END] save to db for images

        return photo.getAbsolutePath();
    }

    public static File writeImageToTkpdPath(InputStream source) {
        OutputStream outStream = null;
        File dest = null;
        try {

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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dest;
    }

    public static File writeImageToTkpdPath(File source) {
        InputStream inStream = null;
        OutputStream outStream = null;
        File dest = null;
        try {

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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dest;
    }

    public static File writeImageToTkpdPath(byte[] buffer) {
        File directory = new File(FileUtils.getFolderPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath()));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File photo = new File(directory.getAbsolutePath() + "/image.jpg");

        if (photo.exists()) {
            photo.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(photo.getPath());

            fos.write(buffer);
            fos.close();
        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
            return null;
        }
        return photo;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        File file = new File(s);// fileLoc[i]

        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, userId, deviceId,
                "https://" + generateHost.getUploadHost() + "/web-service/v4/action/upload-image/upload_shop_image.pl", false)
                .setIdentity()
                .addParam(UploadPhotoShopTask.NEW_ADD, UploadPhotoShopTask.GOLANG)
                .addParam(UploadPhotoShopTask.RESOLUTION, UploadPhotoShopTask.RESOLUTION_DEFAULT_VALUE)
                .addParam(UploadPhotoShopTask.SERVER_ID, generateHost.getServerId())
                .compileAllParam()
                .finish();

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NEW_ADD));
        RequestBody productId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(RESOLUTION));
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(SERVER_ID));

    }
}
