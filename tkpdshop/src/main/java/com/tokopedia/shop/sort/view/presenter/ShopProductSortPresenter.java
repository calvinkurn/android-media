package com.tokopedia.shop.sort.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort;
import com.tokopedia.shop.sort.domain.interactor.GetShopProductSortUseCase;
import com.tokopedia.shop.sort.view.listener.ShopProductSortListView;
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductSortPresenter extends BaseDaggerPresenter<ShopProductSortListView> {

    private final GetShopProductSortUseCase getShopProductFilterUseCase;
    private final ShopProductSortMapper shopProductFilterMapper;
    private final GetShopInfoUseCase getShopInfoUseCase;
    private final UserSession userSession;

    @Inject
    public ShopProductSortPresenter(GetShopProductSortUseCase getShopProductFilterUseCase, ShopProductSortMapper shopProductMapper, GetShopInfoUseCase getShopInfoUseCase, UserSession userSession) {
        this.getShopProductFilterUseCase = getShopProductFilterUseCase;
        this.shopProductFilterMapper = shopProductMapper;
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.userSession = userSession;
    }

    public void getShopFilterList() {
        getShopProductFilterUseCase.execute(new Subscriber<List<ShopProductSort>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<ShopProductSort> dataValue) {
                getView().renderList(shopProductFilterMapper.convertSort(dataValue));
            }
        });
    }

    public void getShopInfo(String shopId) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().onSuccessGetShopInfo(shopInfo);
            }
        });
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopInfoUseCase.unsubscribe();
        getShopProductFilterUseCase.unsubscribe();
    }
}