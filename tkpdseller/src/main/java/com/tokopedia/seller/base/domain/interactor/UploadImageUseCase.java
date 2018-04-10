package com.tokopedia.seller.base.domain.interactor;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.base.domain.UploadImageRepository;
import com.tokopedia.seller.base.domain.model.ImageUploadDomainModel;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.edit.domain.GenerateHostRepository;
import com.tokopedia.seller.product.edit.domain.model.GenerateHostDomainModel;
import com.tokopedia.seller.shop.setting.constant.ShopSettingNetworkConstant;

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

    public static final String PARAM_PATH_UPLOAD = "PATH_UPLOAD";
    public static final String HTTPS = "https://";
    public static final String PARAM_BODY = "PARAM_BODY";

    private static final String PARAM_SERVER_ID = "server_id";
    private static final String PARAM_SERVER_LANGUAGE = "new_add";
    public static final String PARAM_RESOLUTION = "resolution";
    private static final String DEFAULT_GOLANG_VALUE = "2";
    private static final String DEFAULT_RESOLUTION_VALUE = "300";
    private static final String ATTACHMENT_TYPE_KEY_VALUE = "fileToUpload\"; filename=\"image.jpg";

    private UploadImageRepository uploadImageRepository;
    private GenerateHostRepository generateHostRepository;
    private Gson gson;
    private NetworkCalculator networkCalculator;
    private Class<T> imageUploadResultModel;

    public UploadImageUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
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
                        return uploadImageRepository
                                .uploadImage(
                                        getParamsUploadImage(
                                                String.valueOf(generateHostDomainModel.getServerId()),
                                                (Map<String, RequestBody>) requestParams.getObject(PARAM_BODY)
                                        ),
                                        generateUploadUrl(requestParams.getString(PARAM_PATH_UPLOAD, ""), generateHostDomainModel.getUrl()))
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

    private Map<String, RequestBody> getParamsUploadImage(String serverIdUpload, Map<String, RequestBody> maps) {
        RequestBody serverId = RequestBody.create(MediaType.parse("text/plain"), serverIdUpload);
        maps.put(PARAM_SERVER_ID, serverId);
        return maps;
    }

    private String generateUploadUrl(String pathUpload, String urlUpload) {
        return HTTPS + urlUpload + pathUpload;
    }

    public RequestParams createRequestParams(String pathUpload, String pathFile, String keyLabelUploadImage, String productId) {
        Map<String, RequestBody> paramsUploadImage = new TKPDMapParam<>();

        File file = new File(pathFile);

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), DEFAULT_GOLANG_VALUE);
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), DEFAULT_RESOLUTION_VALUE);
        RequestBody productIdBody = RequestBody.create(MediaType.parse("text/plain"), productId);

        paramsUploadImage.put(NetworkCalculator.USER_ID, userId);
        paramsUploadImage.put(NetworkCalculator.DEVICE_ID, deviceId);
        paramsUploadImage.put(NetworkCalculator.HASH, hash);
        paramsUploadImage.put(NetworkCalculator.DEVICE_TIME, deviceTime);
        paramsUploadImage.put(keyLabelUploadImage, fileToUpload);
        paramsUploadImage.put(PARAM_SERVER_LANGUAGE, newAdd);
        paramsUploadImage.put(PARAM_RESOLUTION, resolution);
        if (productId != null && !productId.isEmpty()) {
            paramsUploadImage.put(ProductNetworkConstant.PRODUCT_ID, productIdBody);
        }
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PATH_UPLOAD, pathUpload);
        requestParams.putObject(PARAM_BODY, paramsUploadImage);
        return requestParams;
    }


    public RequestParams createAttachmentsRequestParams(String pathFile) {
        Map<String, RequestBody> paramsUploadImage = new TKPDMapParam<>();
        File file = new File(pathFile);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.USER_ID));
        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.HASH));
        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody webservice = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody osType = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), networkCalculator.getUserId());
        paramsUploadImage.put(NetworkCalculator.USER_ID, userId);
        paramsUploadImage.put(NetworkCalculator.DEVICE_ID, deviceId);
        paramsUploadImage.put(NetworkCalculator.HASH, hash);
        paramsUploadImage.put(NetworkCalculator.DEVICE_TIME, deviceTime);
        paramsUploadImage.put(ATTACHMENT_TYPE_KEY_VALUE, fileToUpload);
        paramsUploadImage.put("web_service", webservice);
        paramsUploadImage.put("os_type", osType);
        paramsUploadImage.put("id", id);

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PATH_UPLOAD, "/upload/attachment");
        requestParams.putObject(PARAM_BODY, paramsUploadImage);
        return requestParams;
    }
}
