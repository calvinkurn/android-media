package com.tokopedia.seller.shop.open.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.shop.open.domain.ShopOpenSaveInfoRepository;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.seller.shop.open.data.model.UploadShopImageModel;

import javax.inject.Inject;

import rx.Observable;

/**
 *
 * https://phab.tokopedia.com/w/api/tome/#open-shop-api
 *
 * Reserve Shop Description And Details ( Step 3 )
 *
 * this link is important for references.
 */

public class ShopOpenSaveCourierUseCase extends UseCase<Boolean> {

    private static final String PARAM_COURIER_ID_LIST = "courier_id_list";

    private final ShopOpenSaveInfoRepository shopOpenSaveInfoRepository;

    @Inject
    public ShopOpenSaveCourierUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      ShopOpenSaveInfoRepository shopOpenSaveInfoRepository,
                                      UploadImageUseCase<UploadShopImageModel> uploadImageUseCase) {
        super(threadExecutor, postExecutionThread);
        this.shopOpenSaveInfoRepository = shopOpenSaveInfoRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return shopOpenSaveInfoRepository.saveShopSettingStep3(
                (CourierServiceIdWrapper) requestParams.getObject(PARAM_COURIER_ID_LIST));
    }

    /**
     * @return RequestParam object
     */
    public static RequestParams createRequestParams(CourierServiceIdWrapper courierServiceIdWrapper) {
        RequestParams params = RequestParams.create();
        params.putObject(PARAM_COURIER_ID_LIST, courierServiceIdWrapper);
        return params;
    }
}
