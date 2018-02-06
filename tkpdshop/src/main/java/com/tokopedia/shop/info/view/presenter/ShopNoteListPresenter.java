package com.tokopedia.shop.info.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.info.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.info.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.info.domain.interactor.GetShopNoteListUseCase;
import com.tokopedia.shop.info.view.listener.ShopNoteListView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopNoteListPresenter extends BaseDaggerPresenter<ShopNoteListView> {

    private final GetShopNoteListUseCase getShopNoteListUseCase;

    @Inject
    public ShopNoteListPresenter(GetShopNoteListUseCase getShopNoteListUseCase) {
        this.getShopNoteListUseCase = getShopNoteListUseCase;
    }

    public void getShopNoteList(String shopId) {
        getShopNoteListUseCase.execute(GetShopNoteListUseCase.createRequestParam(shopId), new Subscriber<List<ShopNote>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopNoteList(e);
                }
            }

            @Override
            public void onNext(List<ShopNote> shopNoteList) {
                getView().onSuccessGetShopNoteList(shopNoteList);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopNoteListUseCase.unsubscribe();
    }
}