package com.tokopedia.seller.product.draft.view.presenter;

import com.tokopedia.seller.manageitem.data.db.ProductDraftViewModel;
import com.tokopedia.seller.manageitem.domain.usecase.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductLegacyUseCase;
import com.tokopedia.seller.product.common.utils.ProductDraftErrorHandler;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.DeleteSingleDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductLegacyUseCase;
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
    private FetchAllDraftProductLegacyUseCase fetchAllDraftProductLegacyUseCase;
    private ClearAllDraftProductLegacyUseCase clearAllDraftProductLegacyUseCase;

    public ProductDraftListPresenterImpl(FetchAllDraftProductUseCase fetchAllDraftProductUseCase,
                                         FetchAllDraftProductLegacyUseCase fetchAllDraftProductLegacyUseCase,
                                         DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase,
                                         UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase,
                                         ClearAllDraftProductUseCase clearAllDraftProductUseCase,
                                         ClearAllDraftProductLegacyUseCase clearAllDraftProductLegacyUseCase) {
        this.fetchAllDraftProductUseCase = fetchAllDraftProductUseCase;
        this.fetchAllDraftProductLegacyUseCase = fetchAllDraftProductLegacyUseCase;
        this.deleteSingleDraftProductUseCase = deleteSingleDraftProductUseCase;
        this.updateUploadingDraftProductUseCase = updateUploadingDraftProductUseCase;
        this.clearAllDraftProductUseCase = clearAllDraftProductUseCase;
        this.clearAllDraftProductLegacyUseCase = clearAllDraftProductLegacyUseCase;
    }

    @Override
    public void fetchAllDraftData() {
        fetchAllDraftProductUseCase.execute(
                FetchAllDraftProductUseCase.createRequestParams(),
                getSubscriber()
        );
    }

    @Override
    public void fetchAllDraftDataWithUpdateUploading() {
        updateUploadingDraftProductUseCase.execute(UpdateUploadingDraftProductUseCase.createRequestParamsUpdateAll(false),
                getUpdateUploadingSubscriber());
    }

    @Override
    public void deleteProductDraft(long draftProductId) {
        //TODO milhamj
//        if (GlobalConfig.isSellerApp()) {
            deleteSingleDraftProductUseCase.execute(DeleteSingleDraftProductUseCase.Companion.createRequestParams(draftProductId),
                    getDeleteSubscriber());
//        } else {
//            deleteSingleDraftProductLegacyUseCase.execute(DeleteSingleDraftProductLegacyUseCase.createRequestParams(draftProductId),
//                    getDeleteSubscriber());
//        }

    }

    @Override
    public void clearAllDraftData() {
        //TODO milhamj
//        if (GlobalConfig.isSellerApp()) {
            clearAllDraftProductUseCase.execute(RequestParams.EMPTY, getClearAllDraftSubscriber());
//        } else {
//            clearAllDraftProductLegacyUseCase.execute(RequestParams.EMPTY, getClearAllDraftSubscriber());
//        }
    }

    private Subscriber<Boolean> getClearAllDraftSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {
                getView().onErrorDeleteAllDraft();
                ProductDraftErrorHandler.INSTANCE.logExceptionToCrashlytics(t);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().onSuccessDeleteAllDraft();
            }
        };
    }

    private Subscriber<List<ProductDraftViewModel>> getSubscriber() {
        return new Subscriber<List<ProductDraftViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {
                if (isViewAttached()) {
                    getView().onLoadSearchError(t);
                }
                ProductDraftErrorHandler.INSTANCE.logExceptionToCrashlytics(t);
            }

            @Override
            public void onNext(List<ProductDraftViewModel> productViewModels) {
                if (productViewModels == null || productViewModels.size() == 0) {
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
        fetchAllDraftProductLegacyUseCase.unsubscribe();
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
            public void onError(Throwable t) {
                fetchAllDraftData();
                ProductDraftErrorHandler.INSTANCE.logExceptionToCrashlytics(t);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                fetchAllDraftData();
            }
        };
    }
}