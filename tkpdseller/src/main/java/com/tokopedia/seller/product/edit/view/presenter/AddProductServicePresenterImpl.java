package com.tokopedia.seller.product.edit.view.presenter;

import android.text.TextUtils;

import com.tokopedia.seller.product.draft.data.source.db.model.DraftNotFoundException;
import com.tokopedia.seller.product.edit.data.exception.UploadProductException;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;
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
    private UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase;

    public AddProductServicePresenterImpl(UploadProductUseCase uploadProductUseCase,
                                          UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase) {
        this.uploadProductUseCase = uploadProductUseCase;
        this.updateUploadingDraftProductUseCase = updateUploadingDraftProductUseCase;
        uploadProductUseCase.setListener(this);
    }

    @Override
    public void uploadProduct(long productDraftId, boolean isAdd) {
        checkViewAttached();
        uploadProductUseCase.execute(UploadProductUseCase.generateUploadProductParam(productDraftId),
                new AddProductSubscriber(String.valueOf(productDraftId), isAdd));
    }

    @Override
    public void createNotification(String productDraftId, String productName) {
        checkViewAttached();
        getView().createNotification(productDraftId, productName);
    }

    @Override
    public void notificationUpdate(String productDraftId) {
        checkViewAttached();
        getView().notificationUpdate(productDraftId);
    }

    private class AddProductSubscriber extends Subscriber<AddProductDomainModel> {

        private String productDraftId;
        private boolean isAdd;

        public AddProductSubscriber(String productDraftId, boolean isAdd) {
            this.productDraftId = productDraftId;
            this.isAdd = isAdd;
        }

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
                updateUploadingDraftProductUseCase.execute(
                        UpdateUploadingDraftProductUseCase.createRequestParams(
                                this.productDraftId, false), new Subscriber<Boolean>() {
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
            getView().onFailedAddProduct();
            getView().notificationFailed(e, this.productDraftId, isAdd ? ProductStatus.ADD : ProductStatus.EDIT);
            getView().sendFailedBroadcast(e);
        }

        @Override
        public void onNext(AddProductDomainModel addProductDomainModel) {
            checkViewAttached();
            getView().onSuccessAddProduct();
            getView().notificationComplete(productDraftId);
            getView().sendSuccessBroadcast(addProductDomainModel);
        }
    }
}
