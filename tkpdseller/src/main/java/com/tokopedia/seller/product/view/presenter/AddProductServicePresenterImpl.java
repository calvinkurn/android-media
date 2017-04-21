package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.AddProductUseCase;
import com.tokopedia.seller.product.domain.listener.AddProductNotificationListener;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/20/17.
 */

public class AddProductServicePresenterImpl extends AddProductServicePresenter implements AddProductNotificationListener {
    private final AddProductUseCase addProductUseCase;

    public AddProductServicePresenterImpl(AddProductUseCase addProductUseCase) {
        this.addProductUseCase = addProductUseCase;
        addProductUseCase.setListener(this);
    }

    @Override
    public void addProduct(long productDraftId) {
        checkViewAttached();
        RequestParams requestParams = AddProductUseCase.generateUploadProductParam(productDraftId);
        addProductUseCase.execute(requestParams, new AddProductSubscriber());
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
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
        }

        @Override
        public void onNext(AddProductDomainModel addProductDomainModel) {
            checkViewAttached();
            getView().onSuccessAddProduct();
            getView().notificationComplete();
        }
    }
}
