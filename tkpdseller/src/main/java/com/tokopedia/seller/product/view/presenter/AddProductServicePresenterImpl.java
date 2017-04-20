package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.AddProductUseCase;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/20/17.
 */

public class AddProductServicePresenterImpl extends AddProductServicePresenter {
    private final AddProductUseCase addProductUseCase;

    public AddProductServicePresenterImpl(AddProductUseCase addProductUseCase) {
        this.addProductUseCase = addProductUseCase;
    }

    @Override
    public void addProduct(long productDraftId) {
        RequestParams requestParams = AddProductUseCase.generateUploadProductParam(productDraftId);
        addProductUseCase.execute(requestParams, new AddProductSubscriber());
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
        }
    }
}
