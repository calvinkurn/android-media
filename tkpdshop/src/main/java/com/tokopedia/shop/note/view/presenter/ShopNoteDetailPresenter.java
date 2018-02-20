package com.tokopedia.shop.note.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.note.domain.interactor.GetShopNoteDetailUseCase;
import com.tokopedia.shop.note.view.listener.ShopNoteDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class ShopNoteDetailPresenter extends BaseDaggerPresenter<ShopNoteDetailView> {

    private final GetShopNoteDetailUseCase getShopNoteDetailUseCase;

    @Inject
    public ShopNoteDetailPresenter(GetShopNoteDetailUseCase getShopNoteDetailUseCase) {
        this.getShopNoteDetailUseCase = getShopNoteDetailUseCase;
    }

    public void getShopNoteList(String shopId) {
        getShopNoteDetailUseCase.execute(GetShopNoteDetailUseCase.createRequestParam(shopId), new Subscriber<ShopNoteDetail>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    getView().onErrorGetShopNoteList(e);
                }
            }

            @Override
            public void onNext(ShopNoteDetail shopNoteDetail) {
                getView().onSuccessGetShopNoteList(shopNoteDetail);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopNoteDetailUseCase.unsubscribe();
    }
}