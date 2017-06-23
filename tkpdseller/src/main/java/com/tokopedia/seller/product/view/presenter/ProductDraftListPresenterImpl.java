package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.seller.product.domain.interactor.productdraft.DeleteSingleDraftProductUseCase;
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
    private DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase;

    public ProductDraftListPresenterImpl (FetchAllDraftProductUseCase fetchAllDraftProductUseCase,
                                          DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase){
        this.fetchAllDraftProductUseCase = fetchAllDraftProductUseCase;
        this.deleteSingleDraftProductUseCase = deleteSingleDraftProductUseCase;
    }

    @Override
    public void fetchAllDraftData() {
        fetchAllDraftProductUseCase.execute(FetchAllDraftProductUseCase.createRequestParams(),
                getSubscriber());
    }

    @Override
    public void deleteProductDraft(long draftId) {
        deleteSingleDraftProductUseCase.execute(DeleteSingleDraftProductUseCase.createRequestParams(draftId),
                getDeleteSubscriber());
    }

    private Subscriber<List<UploadProductInputDomainModel>> getSubscriber(){
        return new Subscriber<List<UploadProductInputDomainModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {
                if(isViewAttached()) {
                    getView().onLoadSearchError(t);
                }
            }

            @Override
            public void onNext(List<UploadProductInputDomainModel> uploadProductInputDomainModels) {
                if (uploadProductInputDomainModels == null || uploadProductInputDomainModels.size() == 0 ) {
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

    public Subscriber<Boolean> getDeleteSubscriber() {
        return new Subscriber<Boolean>() {
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
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        fetchAllDraftProductUseCase.unsubscribe();
        deleteSingleDraftProductUseCase.unsubscribe();
    }
}