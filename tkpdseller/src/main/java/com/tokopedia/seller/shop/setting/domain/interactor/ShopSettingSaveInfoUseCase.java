package com.tokopedia.seller.shop.setting.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.base.domain.model.ImageUploadDomainModel;
import com.tokopedia.seller.shop.setting.constant.ShopSettingNetworkConstant;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopSettingSaveInfoUseCase extends UseCase<Boolean> {
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

    private ShopSettingSaveInfoRepository shopSettingSaveInfoRepository;
    private UploadImageUseCase<UploadShopImageModel> uploadImageUseCase;

    @Inject
    public ShopSettingSaveInfoUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      ShopSettingSaveInfoRepository shopSettingSaveInfoRepository,
                                      UploadImageUseCase<UploadShopImageModel> uploadImageUseCase) {
        super(threadExecutor, postExecutionThread);
        this.shopSettingSaveInfoRepository = shopSettingSaveInfoRepository;
        this.uploadImageUseCase = uploadImageUseCase;
    }

    public static RequestParams createRequestParams(String pathFileImage, String shopDescription,
                                                    String tagLine) {
        RequestParams params = RequestParams.create();
        params.putString(PATH_FILE_IMAGE, pathFileImage);
        params.putString(SHOP_DESCRIPTION, shopDescription);
        params.putString(TAG_LINE, tagLine);
        return params;
    }

    @Override
    public Observable<Boolean> createObservable(final RequestParams requestParams) {
        if(!TextUtils.isEmpty(requestParams.getString(PATH_FILE_IMAGE,""))) {
            return uploadImageUseCase.getExecuteObservable(uploadImageUseCase.createRequestParams(ShopSettingNetworkConstant.UPLOAD_SHOP_IMAGE_PATH,
                    requestParams.getString(PATH_FILE_IMAGE, "")))
                    .flatMap(new Func1<ImageUploadDomainModel<UploadShopImageModel>, Observable<Boolean>>() {
                        @Override
                        public Observable<Boolean> call(ImageUploadDomainModel<UploadShopImageModel> dataImageUploadDomainModel) {
                            return shopSettingSaveInfoRepository.saveShopSetting(getImageRequest(
                                    dataImageUploadDomainModel.getDataResultImageUpload().getData().getUpload().getSrc(),
                                    dataImageUploadDomainModel.getServerId(), "",
                                    requestParams.getString(SHOP_DESCRIPTION, ""), requestParams.getString(TAG_LINE, "")));
                        }
                    });
        }else{
            return shopSettingSaveInfoRepository.saveShopSetting(getImageRequest("", "", "", requestParams.getString(SHOP_DESCRIPTION, ""), requestParams.getString(TAG_LINE, "")));
        }
    }

    private HashMap<String, String> getImageRequest(String imageSrc, String serverId, String picObj, String shopDesc, String tagLine){
        HashMap<String, String> params = new HashMap<>();
        if(!TextUtils.isEmpty(imageSrc)) {
            params.put(LOGO, imageSrc);
            params.put(SERVER_ID, serverId);
            params.put(PHOTO_OBJ, picObj);
        }
        params.put(SHORT_DESC, shopDesc);
        params.put(TAG_LINE, tagLine);
        params.put(STEP, STEP_INFO_1);
        return params;
    }
}
