package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.productdraft.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.domain.interactor.productdraft.FetchAllDraftProductCountUseCase;
import com.tokopedia.seller.product.domain.interactor.productdraft.FetchAllDraftProductUseCase;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.mapper.ProductDraftListMapper;
import com.tokopedia.seller.product.view.model.ProductDraftViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by hendry on 6/20/2017.
 */

public class ProductDraftListCountPresenterImpl extends ProductDraftListCountPresenter {
    private FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase;
    private ClearAllDraftProductUseCase clearAllDraftProductUseCase;

    public ProductDraftListCountPresenterImpl(FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase,
                                              ClearAllDraftProductUseCase clearAllDraftProductUseCase){
        this.fetchAllDraftProductCountUseCase = fetchAllDraftProductCountUseCase;
        this.clearAllDraftProductUseCase = clearAllDraftProductUseCase;
    }

    @Override
    public void fetchAllDraftCount() {
        fetchAllDraftProductCountUseCase.execute(FetchAllDraftProductCountUseCase.createRequestParams(),
                getSubscriber());
    }

    @Override
    public void clearAllDraft() {
        clearAllDraftProductUseCase.execute(RequestParams.EMPTY, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                // no op
            }

            @Override
            public void onError(Throwable e) {
                // no op
            }

            @Override
            public void onNext(Boolean aBoolean) {
                // no op
            }
        });
    }

    private Subscriber<Long> getSubscriber(){
        return new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().onDraftCountLoadError();
                }
            }

            @Override
            public void onNext(Long rowCount) {
                getView().onDraftCountLoaded(rowCount);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        fetchAllDraftProductCountUseCase.unsubscribe();
        clearAllDraftProductUseCase.unsubscribe();
    }

}
