package com.tokopedia.seller.product.draft.view.presenter;

import com.tokopedia.product.manage.item.main.draft.data.model.ProductDraftViewModel;
import com.tokopedia.product.manage.item.main.draft.domain.DeleteSingleDraftProductUseCase;
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by hendry on 6/20/2017.
 */

public class ProductDraftListPresenterImpl extends ProductDraftListPresenter {
    private FetchAllDraftProductUseCase fetchAllDraftProductUseCase;
    private DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase;
    private UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase;
    private ClearAllDraftProductUseCase clearAllDraftProductUseCase;

    public ProductDraftListPresenterImpl (FetchAllDraftProductUseCase fetchAllDraftProductUseCase,
                                          DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase,
                                          UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase,
                                          ClearAllDraftProductUseCase clearAllDraftProductUseCase){
        this.fetchAllDraftProductUseCase = fetchAllDraftProductUseCase;
        this.deleteSingleDraftProductUseCase = deleteSingleDraftProductUseCase;
        this.updateUploadingDraftProductUseCase = updateUploadingDraftProductUseCase;
        this.clearAllDraftProductUseCase = clearAllDraftProductUseCase;
    }

    @Override
    public void fetchAllDraftData() {
        fetchAllDraftProductUseCase.execute(FetchAllDraftProductUseCase.createRequestParams(),
                getSubscriber());
    }

    @Override
    public void fetchAllDraftDataWithUpdateUploading() {
        updateUploadingDraftProductUseCase.execute(UpdateUploadingDraftProductUseCase.createRequestParamsUpdateAll(false),
                getUpdateUploadingSubscriber());
    }

    @Override
    public void deleteProductDraft(long draftProductId) {
        deleteSingleDraftProductUseCase.execute(DeleteSingleDraftProductUseCase.createRequestParams(draftProductId),
                getDeleteSubscriber());
    }

    @Override
    public void clearAllDraftData() {
        clearAllDraftProductUseCase.execute(RequestParams.EMPTY,getClearAllDraftSubscriber());
    }

    private Subscriber<Boolean> getClearAllDraftSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorDeleteAllDraft();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().onSuccessDeleteAllDraft();
            }
        };
    }

    private Subscriber<List<ProductDraftViewModel>> getSubscriber(){
        return new Subscriber<List<ProductDraftViewModel>>() {
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
            public void onNext(List<ProductDraftViewModel> productViewModels) {
                if (productViewModels == null || productViewModels.size() == 0 ) {
                    getView().onSearchLoaded(new ArrayList<ProductDraftViewModel>(), 0);
                } else {
                    getView().onSearchLoaded(productViewModels, productViewModels.size());
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
        updateUploadingDraftProductUseCase.unsubscribe();
    }

    public Subscriber<Boolean> getUpdateUploadingSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                fetchAllDraftData();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                fetchAllDraftData();
            }
        };
    }
}