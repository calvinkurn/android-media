package com.tokopedia.seller.product.view.presenter;

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

public class ProductDraftListPresenterImpl extends ProductDraftListPresenter {
    private FetchAllDraftProductUseCase fetchAllDraftProductUseCase;

    public ProductDraftListPresenterImpl (FetchAllDraftProductUseCase fetchAllDraftProductUseCase){
        this.fetchAllDraftProductUseCase = fetchAllDraftProductUseCase;
    }

    @Override
    public void fetchAllDraftData() {
        fetchAllDraftProductUseCase.execute(FetchAllDraftProductUseCase.createRequestParams(),
                getSubscriber());
    }

    private Subscriber<List<UploadProductInputDomainModel>> getSubscriber(){
        return new Subscriber<List<UploadProductInputDomainModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().onLoadSearchError();
                }
            }

            @Override
            public void onNext(List<UploadProductInputDomainModel> uploadProductInputDomainModels) {
                if (uploadProductInputDomainModels == null || uploadProductInputDomainModels.size() <= 0 ) {
                    getView().onSearchLoaded(new ArrayList<ProductDraftViewModel>(), 0);
                } else {
                    // map to View Model
                    List<ProductDraftViewModel> viewModelList = new ArrayList<>();
                    for (int i=0, sizei = uploadProductInputDomainModels.size(); i<sizei; i++) {
                        UploadProductInputDomainModel domainModel = uploadProductInputDomainModels.get(i);
                        viewModelList.add(ProductDraftListMapper.mapDomainToView(domainModel));
                    }
                    getView().onSearchLoaded(viewModelList, viewModelList.size());
                }

            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        fetchAllDraftProductUseCase.unsubscribe();
    }
}