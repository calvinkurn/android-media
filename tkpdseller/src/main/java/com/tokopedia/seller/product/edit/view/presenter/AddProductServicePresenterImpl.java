package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.seller.product.draft.data.source.db.model.DraftNotFoundException;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.edit.data.exception.UploadProductException;
import com.tokopedia.seller.product.edit.domain.interactor.uploadproduct.UploadDraftProductUseCase;
import com.tokopedia.seller.product.edit.domain.listener.NotificationCountListener;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/20/17.
 */

public class AddProductServicePresenterImpl extends AddProductServicePresenter {
    private final UploadDraftProductUseCase uploadProductUseCase;
    private final UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase;

    public AddProductServicePresenterImpl(UploadDraftProductUseCase uploadProductUseCase,
                                          UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase) {
        this.uploadProductUseCase = uploadProductUseCase;
        this.updateUploadingDraftProductUseCase = updateUploadingDraftProductUseCase;
    }

    @Override
    public void uploadProduct(final long draftProductId, final boolean isAdd, NotificationCountListener notificationCountListener) {
        uploadProductUseCase.execute(UploadDraftProductUseCase.createParams(draftProductId, notificationCountListener), new Subscriber<ProductViewModel>() {
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
                    updateDraftUploadingStatus(draftProductId);
                }
                getView().onFailedAddProduct();
                getView().notificationFailed(e, draftProductId, isAdd ? ProductStatus.ADD : ProductStatus.EDIT);
                getView().sendFailedBroadcast(e);
            }

            @Override
            public void onNext(ProductViewModel productViewModel) {
                getView().onSuccessAddProduct();
                getView().notificationComplete(draftProductId);
                getView().sendSuccessBroadcast(productViewModel);
            }
        });
    }

    private void updateDraftUploadingStatus(final long draftProductId) {
        updateUploadingDraftProductUseCase.execute(UpdateUploadingDraftProductUseCase.createRequestParams(draftProductId, false), new Subscriber<Boolean>() {
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
}
