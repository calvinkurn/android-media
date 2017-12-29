package com.tokopedia.seller.shop.open.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.shop.open.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.open.domain.ShopSettingSaveInfoRepository;

import javax.inject.Inject;

import rx.Observable;


public class ShopOpenCreateUseCase extends UseCase<Boolean> {

    private final ShopSettingSaveInfoRepository shopSettingSaveInfoRepository;

    @Inject
    public ShopOpenCreateUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 ShopSettingSaveInfoRepository shopSettingSaveInfoRepository,
                                 UploadImageUseCase<UploadShopImageModel> uploadImageUseCase) {
        super(threadExecutor, postExecutionThread);
        this.shopSettingSaveInfoRepository = shopSettingSaveInfoRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return shopSettingSaveInfoRepository.createShop();
    }
}
