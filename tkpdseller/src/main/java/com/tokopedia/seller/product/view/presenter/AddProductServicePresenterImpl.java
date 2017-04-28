package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.exception.ResponseErrorListStringException;
import com.tokopedia.seller.product.data.exception.UploadProductException;
import com.tokopedia.seller.product.domain.interactor.UploadProductUseCase;
import com.tokopedia.seller.product.domain.listener.AddProductNotificationListener;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.utils.ViewUtils;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/20/17.
 */

public class AddProductServicePresenterImpl extends AddProductServicePresenter implements AddProductNotificationListener {
    private final UploadProductUseCase uploadProductUseCase;

    public AddProductServicePresenterImpl(UploadProductUseCase uploadProductUseCase) {
        this.uploadProductUseCase = uploadProductUseCase;
        uploadProductUseCase.setListener(this);
    }

    @Override
    public void uploadProduct(long productDraftId) {
        checkViewAttached();
        RequestParams requestParams = UploadProductUseCase.generateUploadProductParam(productDraftId);
        uploadProductUseCase.execute(requestParams, new AddProductSubscriber());
    }

    @Override
    public void createNotification(String productName) {
        checkViewAttached();
        getView().createNotification(productName);
    }

    @Override
    public void notificationUpdate(int stepNotification) {
        checkViewAttached();
        getView().notificationUpdate(stepNotification);
    }

    private class AddProductSubscriber extends Subscriber<AddProductDomainModel> {

        public AddProductSubscriber() {

        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable uploadThrowable) {
            Throwable e = uploadThrowable;
            String productDraftId = "";
            @ProductStatus
            int productStatus = ProductStatus.ADD;
            if (uploadThrowable instanceof UploadProductException){
                e = ((UploadProductException) uploadThrowable).getThrowable();
                productDraftId = ((UploadProductException) uploadThrowable).getProductDraftId();
                productStatus = ((UploadProductException) uploadThrowable).getProductStatus();
            }

            checkViewAttached();
            getView().onFailedAddProduct();
            getView().notificationFailed(e, productDraftId, productStatus);
            getView().sendFailedBroadcast(e);
        }

        @Override
        public void onNext(AddProductDomainModel addProductDomainModel) {
            checkViewAttached();
            getView().onSuccessAddProduct();
            getView().notificationComplete();
            getView().sendSuccessBroadcast(addProductDomainModel);
        }
    }
}
