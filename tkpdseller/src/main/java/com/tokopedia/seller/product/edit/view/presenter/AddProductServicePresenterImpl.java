package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.seller.product.draft.data.source.db.model.DraftNotFoundException;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.edit.data.exception.UploadProductException;
import com.tokopedia.seller.product.edit.domain.interactor.uploadproduct.UploadProductUseCase;
import com.tokopedia.seller.product.edit.domain.listener.AddProductNotificationListener;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/20/17.
 */

public class AddProductServicePresenterImpl extends AddProductServicePresenter implements AddProductNotificationListener {
    private final UploadProductUseCase uploadProductUseCase;
    private final UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase;

    public AddProductServicePresenterImpl(UploadProductUseCase uploadProductUseCase,
                                          UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase) {
        this.uploadProductUseCase = uploadProductUseCase;
        this.updateUploadingDraftProductUseCase = updateUploadingDraftProductUseCase;
        uploadProductUseCase.setListener(this);
    }

    @Override
    public void uploadProduct(final long productDraftId, final boolean isAdd) {
        checkViewAttached();
        uploadProductUseCase.execute(UploadProductUseCase.generateUploadProductParam(productDraftId), new Subscriber<AddProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable uploadThrowable) {
                Throwable e = uploadThrowable;
                if (!isViewAttached()) {
                    return;
                }
                if (uploadThrowable instanceof UploadProductException) {
                    e = ((UploadProductException) uploadThrowable).getThrowable();
                }
                if (!(e instanceof DraftNotFoundException)) {
                    updateDraftUploadingStatus(productDraftId);
                }
                getView().onFailedAddProduct();
                getView().notificationFailed(e, productDraftId, isAdd ? ProductStatus.ADD : ProductStatus.EDIT);
                getView().sendFailedBroadcast(e);
            }

            @Override
            public void onNext(AddProductDomainModel addProductDomainModel) {
                checkViewAttached();
                getView().onSuccessAddProduct();
                getView().notificationComplete(productDraftId);
                getView().sendSuccessBroadcast(addProductDomainModel);
            }
        });
    }

    private void updateDraftUploadingStatus(final long productDraftId) {
        updateUploadingDraftProductUseCase.execute(UpdateUploadingDraftProductUseCase.createRequestParams(String.valueOf(productDraftId), false), new Subscriber<Boolean>() {
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

    @Override
    public void createNotification(long productDraftId, String productName) {
        checkViewAttached();
        getView().createNotification(productDraftId, productName);
    }

    @Override
    public void notificationUpdate(long productDraftId) {
        checkViewAttached();
        getView().notificationUpdate(productDraftId);
    }
}
