package com.tokopedia.seller.manageitem.domain.usecase;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.manageitem.common.constant.ProductNetworkConstant;
import com.tokopedia.seller.manageitem.common.constant.ShopSettingNetworkConstant;
import com.tokopedia.seller.manageitem.data.model.GenerateHostDomainModel;
import com.tokopedia.seller.manageitem.data.model.ImageUploadDomainModel;
import com.tokopedia.seller.manageitem.domain.repository.GenerateHostRepository;
import com.tokopedia.seller.manageitem.domain.repository.UploadImageRepository;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public class UploadImageUseCase<T> extends UseCase<ImageUploadDomainModel<T>> {

    public static final String PATH_FILE = "PATH_FILE";
    public static final String PATH_UPLOAD = "PATH_UPLOAD";
    public static final String HTTPS = "https://";
    public static final String KEY_LABEL_UPLOAD_IMAGE = "KEY_LABEL_UPLOAD_IMAGE";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    private UploadImageRepository uploadImageRepository;
    private GenerateHostRepository generateHostRepository;
    private Gson gson;
    private NetworkCalculator networkCalculator;
    private Class<T> imageUploadResultModel;

    public UploadImageUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                              UploadImageRepository uploadImageRepository,
                              GenerateHostRepository generateHostRepository,
                              Gson gson,
                              NetworkCalculator networkCalculator,
                              Class<T> imageUploadResultModel) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
        this.generateHostRepository = generateHostRepository;
        this.gson = gson;
        this.networkCalculator = networkCalculator;
        this.imageUploadResultModel = imageUploadResultModel;
    }

    @Override
    public Observable<ImageUploadDomainModel<T>> createObservable(final RequestParams requestParams) {
        return generateHostRepository.generateHost()
                .flatMap(new Func1<GenerateHostDomainModel, Observable<ImageUploadDomainModel<T>>>() {
                    @Override
                    public Observable<ImageUploadDomainModel<T>> call(final GenerateHostDomainModel generateHostDomainModel) {
                        return uploadImageRepository.uploadImage(getParamsUploadImage(generateHostDomainModel.getUrl(),
                                requestParams.getString(PATH_FILE, ""), String.valueOf(generateHostDomainModel.getServerId()), requestParams.getString(PRODUCT_ID, null),
                                requestParams.getString(KEY_LABEL_UPLOAD_IMAGE, "")),
                                generateUploadUrl(requestParams.getString(PATH_UPLOAD, ""), generateHostDomainModel.getUrl()))
                                .map(new Func1<String, ImageUploadDomainModel<T>>() {
                                    @Override
                                    public ImageUploadDomainModel<T> call(String s) {
                                        ImageUploadDomainModel<T> imageUploadDomainModel = new ImageUploadDomainModel<T>(imageUploadResultModel);
                                        T dataResultImageUpload = gson.fromJson(s, imageUploadDomainModel.getType());
                                        imageUploadDomainModel.setDataResultImageUpload(dataResultImageUpload);
                                        imageUploadDomainModel.setServerId(String.valueOf(generateHostDomainModel.getServerId()));
                                        imageUploadDomainModel.setUrl(generateHostDomainModel.getUrl());
                                        return imageUploadDomainModel;
                                    }
                                });
                    }
                });
    }

    private String generateUploadUrl(String pathUpload, String urlUpload) {
        return HTTPS + urlUpload + pathUpload;
    }

    public RequestParams createRequestParams(String pathUpload, String pathFile, String keyLabelUploadImage, String productId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PATH_UPLOAD, pathUpload);
        requestParams.putString(PATH_FILE, pathFile);
        requestParams.putString(KEY_LABEL_UPLOAD_IMAGE, keyLabelUploadImage);
        requestParams.putString(PRODUCT_ID, productId);
        return requestParams;
    }

    public Map<String, RequestBody> getParamsUploadImage(String urlUploadImage, String pathFile, String serverIdUpload, String productId, String paramUploadImage) {
        Map<String, RequestBody> paramsUploadImage = new TKPDMapParam<>();

        File file = new File(pathFile);

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), ShopSettingNetworkConstant.GOLANG_VALUE);
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), ShopSettingNetworkConstant.RESOLUTION_DEFAULT_VALUE);
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), serverIdUpload);
        RequestBody productIdBody = RequestBody.create(MediaType.parse("text/plain"), productId);

        paramsUploadImage.put(NetworkCalculator.USER_ID, userId);
        paramsUploadImage.put(NetworkCalculator.DEVICE_ID, deviceId);
        paramsUploadImage.put(NetworkCalculator.HASH, hash);
        paramsUploadImage.put(NetworkCalculator.DEVICE_TIME, deviceTime);
        paramsUploadImage.put(paramUploadImage, fileToUpload);
        paramsUploadImage.put(ShopSettingNetworkConstant.SERVER_LANGUAGE, newAdd);
        paramsUploadImage.put(ShopSettingNetworkConstant.RESOLUTION, resolution);
        paramsUploadImage.put(ShopSettingNetworkConstant.SERVER_ID, serverId);
        if(productId !=null && !productId.isEmpty()){
            paramsUploadImage.put(ProductNetworkConstant.PRODUCT_ID, productIdBody);
        }

        return paramsUploadImage;
    }

}
