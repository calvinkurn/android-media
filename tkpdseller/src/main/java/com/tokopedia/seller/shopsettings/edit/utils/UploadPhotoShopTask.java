package com.tokopedia.seller.shopsettings.edit.utils;

import android.os.AsyncTask;

import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.myproduct.model.GenerateHostModel;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.myproduct.utils.MetadataUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.Pair;

import java.io.File;
import java.io.FileOutputStream;

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
//        File photo=new File(Environment.getExternalStorageDirectory(), "image.jpg");// old
        File photo = FileUtils.writeImageToTkpdPath(jpeg[0]);

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
        //[END] save to db for images

        return photo.getAbsolutePath();
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
