package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.FetchCategoryFromSelectedUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchEditProductFormUseCase;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.domain.model.EditProductFormDomainModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditPresenter extends ProductAddPresenterImpl {

    private final FetchEditProductFormUseCase fetchEditProductFormUseCase;

    @Inject
    public ProductEditPresenter(SaveDraftProductUseCase saveDraftProductUseCase, ProductScoringUseCase productScoringUseCase, FetchEditProductFormUseCase fetchEditProductFormUseCase) {
        super(saveDraftProductUseCase, productScoringUseCase);
        this.fetchEditProductFormUseCase = fetchEditProductFormUseCase;
    }

    public void fetchEditProductData(String productId) {
        RequestParams params = FetchEditProductFormUseCase.createParams(productId);
        fetchEditProductFormUseCase.execute(params, new FetchEditProductFormSubscriber());
    }

    private class FetchEditProductFormSubscriber extends Subscriber<EditProductFormDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(EditProductFormDomainModel editProductFormDomainModel) {

        }
    }
}
