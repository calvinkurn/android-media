package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.setting.domain.interactor.CheckDomainNameUseCase;
import com.tokopedia.seller.shop.setting.domain.interactor.CheckShopNameUseCase;
import com.tokopedia.seller.shop.setting.domain.interactor.FetchDistrictDataUseCase;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public class ShopOpenDomainPresenterImpl extends ShopOpenDomainPresenter {

    private final CheckDomainNameUseCase checkDomainNameUseCase;
    private final CheckShopNameUseCase checkShopNameUseCase;

    public ShopOpenDomainPresenterImpl(ShopOpenDomainView view,
                                       CheckDomainNameUseCase checkDomainNameUseCase,
                                       CheckShopNameUseCase checkShopNameUseCase) {
        super(view);
        this.checkDomainNameUseCase = checkDomainNameUseCase;
        this.checkShopNameUseCase = checkShopNameUseCase;
    }

    @Override
    protected void unsubscribeOnDestroy() {
        checkDomainNameUseCase.unsubscribe();
        checkShopNameUseCase.unsubscribe();
    }
}
