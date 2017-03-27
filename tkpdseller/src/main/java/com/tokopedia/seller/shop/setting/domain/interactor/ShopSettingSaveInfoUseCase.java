package com.tokopedia.seller.shop.setting.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.setting.data.model.GenerateHostModel;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopSettingSaveInfoUseCase extends UseCase<Boolean> {
    public static final String PATH_FILE_IMAGE = "PATH_FILE_IMAGE";
    public static final String SHOP_DESCRIPTION = "SHOP_DESCRIPTION";
    public static final String TAG_LINE = "TAG_LINE";

    private ShopSettingSaveInfoRepository shopSettingSaveInfoRepository;

    public ShopSettingSaveInfoUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      ShopSettingSaveInfoRepository shopSettingSaveInfoRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopSettingSaveInfoRepository = shopSettingSaveInfoRepository;
    }

    @Override
    public Observable<Boolean> createObservable(final RequestParams requestParams) {
        return shopSettingSaveInfoRepository.generateHost()
                .flatMap(new Func1<GenerateHostModel.GenerateHost, Observable<UploadShopImageModel.Data>>() {
                    @Override
                    public Observable<UploadShopImageModel.Data> call(GenerateHostModel.GenerateHost generateHost) {
                        return shopSettingSaveInfoRepository.uploadImage(generateHost, requestParams.getString(PATH_FILE_IMAGE, ""));
                    }
                })
                .flatMap(new Func1<UploadShopImageModel.Data, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(UploadShopImageModel.Data data) {
                        return shopSettingSaveInfoRepository.saveShopSetting(data.getUpload().getSrc(),
                                requestParams.getString(SHOP_DESCRIPTION, ""), requestParams.getString(TAG_LINE, ""));
                    }
                });
    }

    public static RequestParams createRequestParams(String pathFileImage, String shopDescription,
                                                    String tagLine) {
        RequestParams params = RequestParams.create();
        params.putString(PATH_FILE_IMAGE, pathFileImage);
        params.putString(SHOP_DESCRIPTION, shopDescription);
        params.putString(TAG_LINE, tagLine);
        return params;
    }
}
