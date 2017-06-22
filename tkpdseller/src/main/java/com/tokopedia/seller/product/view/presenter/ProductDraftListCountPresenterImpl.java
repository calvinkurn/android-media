package com.tokopedia.seller.product.view.presenter;

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

    public ProductDraftListCountPresenterImpl(FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase){
        this.fetchAllDraftProductCountUseCase = fetchAllDraftProductCountUseCase;
    }

    @Override
    public void fetchAllDraftCount() {
        fetchAllDraftProductCountUseCase.execute(FetchAllDraftProductCountUseCase.createRequestParams(),
                getSubscriber());
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
    }

}
