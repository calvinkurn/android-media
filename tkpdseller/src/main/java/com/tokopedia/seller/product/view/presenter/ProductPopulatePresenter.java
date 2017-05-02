package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.domain.interactor.categorypicker.FetchCategoryDisplayUseCase;
import com.tokopedia.seller.product.view.listener.ProductAddView;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/28/17.
 */

public abstract class ProductPopulatePresenter<V extends ProductPopulateView> extends ProductAddPresenterImpl<V> {
    private final FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase;

    public ProductPopulatePresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                    FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                    GetCategoryRecommUseCase getCategoryRecommUseCase,
                                    ProductScoringUseCase productScoringUseCase,
                                    AddProductShopInfoUseCase addProductShopInfoUseCase,
                                    FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase) {
        super(saveDraftProductUseCase, fetchCatalogDataUseCase, getCategoryRecommUseCase, productScoringUseCase, addProductShopInfoUseCase);
        this.fetchCategoryDisplayUseCase = fetchCategoryDisplayUseCase;
    }

    public void fetchCategoryDisplay(long productDepartmentId) {
        RequestParams requestParam = FetchCategoryDisplayUseCase.generateParam(productDepartmentId);
        fetchCategoryDisplayUseCase.execute(requestParam, new FetchCategoryDisplaySubscriber());
    }

    private class FetchCategoryDisplaySubscriber extends Subscriber<List<String>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
        }

        @Override
        public void onNext(List<String> strings) {
            checkViewAttached();
            getView().populateCategory(strings);
        }
    }
}
