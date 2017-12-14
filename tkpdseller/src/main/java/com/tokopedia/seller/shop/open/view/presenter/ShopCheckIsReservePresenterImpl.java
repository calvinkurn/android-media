package com.tokopedia.seller.shop.open.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shop.open.domain.interactor.CheckDomainNameUseCase;
import com.tokopedia.seller.shop.open.domain.interactor.CheckShopNameUseCase;
import com.tokopedia.seller.shop.open.view.listener.ShopCheckDomainView;

import javax.inject.Inject;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public class ShopCheckIsReservePresenterImpl extends BaseDaggerPresenter<ShopCheckDomainView>
        implements ShopCheckIsReservePresenter {

    private final CheckDomainNameUseCase checkDomainNameUseCase;
    private final CheckShopNameUseCase checkShopNameUseCase;

    @Inject
    public ShopCheckIsReservePresenterImpl(CheckDomainNameUseCase checkDomainNameUseCase,
                                           CheckShopNameUseCase checkShopNameUseCase) {
        this.checkDomainNameUseCase = checkDomainNameUseCase;
        this.checkShopNameUseCase = checkShopNameUseCase;
    }

    @Override
    public void isReservingDomain() {

    }

    @Override
    public void detachView() {
        super.detachView();
        checkDomainNameUseCase.unsubscribe();
        checkShopNameUseCase.unsubscribe();
    }
}
