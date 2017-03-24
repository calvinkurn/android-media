package com.tokopedia.seller.shop.setting.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.seller.shop.setting.constant.ShopSettingNetworkConstant;
import com.tokopedia.seller.shop.setting.data.mapper.ShopOpenGenerateHostMapper;
import com.tokopedia.seller.shop.setting.data.mapper.ShopOpenUploadImageMapper;
import com.tokopedia.seller.shop.setting.data.model.GenerateHostModel;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.setting.data.source.cloud.apiservice.GenerateHostApi;
import com.tokopedia.seller.shop.setting.data.source.cloud.apiservice.ShopImageUploadApi;
import com.tokopedia.seller.shop.utils.UploadPhotoShopTask;

import java.io.File;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopSettingInfoDataSourceCloud {

    private final GenerateHostApi generateHostApi;
    private final Context context;
    private final ShopOpenGenerateHostMapper shopOpenGenerateHostMapper;
    private final ShopOpenUploadImageMapper shopOpenUploadImageMapper;

    @Inject
    public ShopSettingInfoDataSourceCloud(GenerateHostApi generateHostApi, @ActivityContext Context context,
                                          ShopOpenGenerateHostMapper shopOpenGenerateHostMapper,
                                          ShopOpenUploadImageMapper shopOpenUploadImageMapper) {
        this.generateHostApi = generateHostApi;
        this.context = context;
        this.shopOpenGenerateHostMapper = shopOpenGenerateHostMapper;
        this.shopOpenUploadImageMapper = shopOpenUploadImageMapper;
    }

    public Observable<Boolean> saveShopSetting() {
        return null;
    }

    public Observable<GenerateHostModel.GenerateHost> generateHost() {
        return generateHostApi.generateHost(AuthUtil.generateParamsNetwork(context, getParamsGenerateHost()))
                .flatMap(shopOpenGenerateHostMapper);
    }

    public Observable<UploadShopImageModel.Data> uploadImage(GenerateHostModel.GenerateHost generateHost, String pathFileImage) {
        String urlUploadImage = ShopSettingNetworkConstant.getUploadImageUrl(generateHost.getUploadHost());
        return RetrofitUtils.createRetrofit(urlUploadImage)
                .create(ShopImageUploadApi.class)
                .uploadImage(getParamsUploadImage(urlUploadImage, pathFileImage, generateHost.getServerId()))
                .map(shopOpenUploadImageMapper);
    }

    public TKPDMapParam<String,String> getParamsGenerateHost() {
        TKPDMapParam<String, String> paramsGenerateHost = new TKPDMapParam<>();
        paramsGenerateHost.put(ShopSettingNetworkConstant.SERVER_LANGUAGE, ShopSettingNetworkConstant.GOLANG_VALUE);
        return paramsGenerateHost;
    }

    public Map<String,RequestBody> getParamsUploadImage(String urlUploadImage, String pathFile, String serverIdUpload) {
        Map<String, RequestBody> paramsUploadImage = new TKPDMapParam<>();

        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, context,
                urlUploadImage).setIdentity().compileAllParam().finish();

        File file = new File(pathFile);

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), ShopSettingNetworkConstant.GOLANG_VALUE);
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), ShopSettingNetworkConstant.RESOLUTION_DEFAULT_VALUE);
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), serverIdUpload);

        paramsUploadImage.put(NetworkCalculator.USER_ID,userId);
        paramsUploadImage.put(NetworkCalculator.DEVICE_ID, deviceId);
        paramsUploadImage.put(NetworkCalculator.HASH, hash);
        paramsUploadImage.put(NetworkCalculator.DEVICE_TIME, deviceTime);
        paramsUploadImage.put(ShopSettingNetworkConstant.LOGO_FILENAME_IMAGE_JPG, fileToUpload);
        paramsUploadImage.put(ShopSettingNetworkConstant.SERVER_LANGUAGE, newAdd);
        paramsUploadImage.put(ShopSettingNetworkConstant.RESOLUTION, resolution);
        paramsUploadImage.put(ShopSettingNetworkConstant.SERVER_ID, serverId);

        return paramsUploadImage;
    }
}
