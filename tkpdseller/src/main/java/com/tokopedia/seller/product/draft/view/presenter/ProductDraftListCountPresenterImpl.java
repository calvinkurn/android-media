package com.tokopedia.seller.product.draft.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductCountUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;

import rx.Subscriber;

/**
 * Created by hendry on 6/20/2017.
 */

public class ProductDraftListCountPresenterImpl extends ProductDraftListCountPresenter {
    private FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase;
    private ClearAllDraftProductUseCase clearAllDraftProductUseCase;
    private UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase;

    public ProductDraftListCountPresenterImpl(FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase,
                                              ClearAllDraftProductUseCase clearAllDraftProductUseCase,
                                              UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase){
        this.fetchAllDraftProductCountUseCase = fetchAllDraftProductCountUseCase;
        this.clearAllDraftProductUseCase = clearAllDraftProductUseCase;
        this.updateUploadingDraftProductUseCase = updateUploadingDraftProductUseCase;
    }

    @Override
    public void fetchAllDraftCount() {
        fetchAllDraftProductCountUseCase.execute(FetchAllDraftProductCountUseCase.createRequestParams(),
                getSubscriber());
    }

    @Override
    public void fetchAllDraftCountWithUpdateUploading() {
        updateUploadingDraftProductUseCase.execute(UpdateUploadingDraftProductUseCase.createRequestParamsUpdateAll(false),
                getUploadingSubscriber());
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
        updateUploadingDraftProductUseCase.unsubscribe();
    }

    private Subscriber<Boolean> getUploadingSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                fetchAllDraftCount();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                fetchAllDraftCount();
            }
        };
    }
}
