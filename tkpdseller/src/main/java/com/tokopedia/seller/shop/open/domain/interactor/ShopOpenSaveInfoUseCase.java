package com.tokopedia.seller.shop.open.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.base.domain.model.ImageUploadDomainModel;
import com.tokopedia.seller.shop.open.domain.ShopOpenSaveInfoRepository;
import com.tokopedia.seller.shop.setting.constant.ShopSettingNetworkConstant;
import com.tokopedia.seller.shop.open.data.model.UploadShopImageModel;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopOpenSaveInfoUseCase extends UseCase<Boolean> {
    public static final String PATH_FILE_IMAGE = "PATH_FILE_IMAGE";
    public static final String SHOP_DESCRIPTION = "SHOP_DESCRIPTION";
    public static final String TAG_LINE = "tag_line";
    public static final String STEP_INFO_1 = "1";
    public static final String LOGO = "logo";
    public static final String SERVER_ID = "server_id";
    public static final String PHOTO_OBJ = "photo_obj";
    public static final String SHORT_DESC = "short_desc";
    public static final String TAG_LINE_REQUEST_CLOUD = "tag_line";
    public static final String STEP = "step";
    public static final String URL_IMAGE_CLOUD = "URL_IMAGE_CLOUD";

    private ShopOpenSaveInfoRepository shopOpenSaveInfoRepository;
    private UploadImageUseCase<UploadShopImageModel> uploadImageUseCase;

    @Inject
    public ShopOpenSaveInfoUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   ShopOpenSaveInfoRepository shopOpenSaveInfoRepository,
                                   UploadImageUseCase<UploadShopImageModel> uploadImageUseCase) {
        super(threadExecutor, postExecutionThread);
        this.shopOpenSaveInfoRepository = shopOpenSaveInfoRepository;
        this.uploadImageUseCase = uploadImageUseCase;
    }

    public static RequestParams createRequestParams(String uriPathImage, String description, String shopSlogan,
                                                    String urlImageCloud, String serverId,
                                                    String picObj) {
        RequestParams params = RequestParams.create();
        params.putString(PATH_FILE_IMAGE, uriPathImage);
        params.putString(SHOP_DESCRIPTION, description);
        params.putString(TAG_LINE_REQUEST_CLOUD, shopSlogan);
        params.putString(URL_IMAGE_CLOUD, urlImageCloud);
        params.putString(SERVER_ID, serverId);
        params.putString(PHOTO_OBJ, picObj);
        return params;
    }

    @Override
    public Observable<Boolean> createObservable(final RequestParams requestParams) {
        if (!TextUtils.isEmpty(requestParams.getString(PATH_FILE_IMAGE, ""))) {
            return uploadImageUseCase.getExecuteObservable(uploadImageUseCase.createRequestParams(ShopSettingNetworkConstant.UPLOAD_SHOP_IMAGE_PATH,
                    requestParams.getString(PATH_FILE_IMAGE, "")))
                    .flatMap(new Func1<ImageUploadDomainModel<UploadShopImageModel>, Observable<Boolean>>() {
                        @Override
                        public Observable<Boolean> call(ImageUploadDomainModel<UploadShopImageModel> dataImageUploadDomainModel) {
                            return shopOpenSaveInfoRepository.saveShopSetting(getImageRequest(
                                    dataImageUploadDomainModel.getDataResultImageUpload().getData().getUpload().getSrc(),
                                    dataImageUploadDomainModel.getServerId(), "",
                                    requestParams.getString(SHOP_DESCRIPTION, ""), requestParams.getString(TAG_LINE_REQUEST_CLOUD, "")));
                        }
                    });
        } else {
            return shopOpenSaveInfoRepository.saveShopSetting(getImageRequest(requestParams.getString(URL_IMAGE_CLOUD, "")
                    , requestParams.getString(SERVER_ID, ""), requestParams.getString(PHOTO_OBJ, ""),
                    requestParams.getString(SHOP_DESCRIPTION, ""), requestParams.getString(TAG_LINE_REQUEST_CLOUD, "")));
        }
    }

    private HashMap<String, String> getImageRequest(String imageSrc, String serverId, String picObj, String shopDesc, String tagLine) {
        HashMap<String, String> params = new HashMap<>();
        params.put(LOGO, imageSrc);
        params.put(SERVER_ID, serverId);
        params.put(PHOTO_OBJ, picObj);
        params.put(SHORT_DESC, shopDesc);
        params.put(TAG_LINE_REQUEST_CLOUD, tagLine);
        params.put(STEP, STEP_INFO_1);
        return params;
    }
}
