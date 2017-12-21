package com.tokopedia.seller.shop.setting.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.base.domain.model.ImageUploadDomainModel;
import com.tokopedia.seller.product.edit.domain.GenerateHostRepository;
import com.tokopedia.seller.product.edit.domain.model.GenerateHostDomainModel;
import com.tokopedia.seller.shop.setting.constant.ShopSettingNetworkConstant;
import com.tokopedia.seller.shop.setting.data.model.GenerateHostModel;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopSettingSaveInfoUseCase extends UseCase<Boolean> {
    public static final String PATH_FILE_IMAGE = "PATH_FILE_IMAGE";
    public static final String SHOP_DESCRIPTION = "SHOP_DESCRIPTION";
    public static final String TAG_LINE = "TAG_LINE";
    public static final int STEP_INFO_1 = 1;

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
        return uploadImageUseCase.getExecuteObservableAsync(uploadImageUseCase.createRequestParams(ShopSettingNetworkConstant.UPLOAD_SHOP_IMAGE_PATH,
                requestParams.getString(PATH_FILE_IMAGE, "")))
                .flatMap(new Func1<ImageUploadDomainModel<UploadShopImageModel>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(ImageUploadDomainModel<UploadShopImageModel> dataImageUploadDomainModel) {
                        return shopSettingSaveInfoRepository.saveShopSetting(dataImageUploadDomainModel.getDataResultImageUpload().getData().getUpload().getSrc(),
                                dataImageUploadDomainModel.getServerId(), "",
                                requestParams.getString(SHOP_DESCRIPTION, ""), requestParams.getString(TAG_LINE, ""), STEP_INFO_1);
                    }
                });
    }
}
