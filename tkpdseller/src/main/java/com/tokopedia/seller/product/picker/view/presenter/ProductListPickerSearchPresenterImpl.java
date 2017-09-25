package com.tokopedia.seller.product.picker.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.domain.interactor.GetProductListSellingUseCase;
import com.tokopedia.seller.product.picker.view.mapper.GetProductListPickerMapperView;
import com.tokopedia.seller.product.picker.view.listener.ProductListPickerSearchView;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerSearchPresenterImpl extends BaseDaggerPresenter<ProductListPickerSearchView>  implements ProductListPickerSearchPresenter {

    private final GetProductListSellingUseCase getProductListSellingUseCase;
    private final GetProductListPickerMapperView getProductListPickerMapperView;

    public ProductListPickerSearchPresenterImpl(GetProductListSellingUseCase getProductListSellingUseCase, GetProductListPickerMapperView getProductListPickerMapperView) {
        this.getProductListSellingUseCase = getProductListSellingUseCase;
        this.getProductListPickerMapperView = getProductListPickerMapperView;
    }

    @Override
    public void getProductList(int page, String keywordFilter) {
        getProductListSellingUseCase.execute(GetProductListSellingUseCase.createRequestParams(page, keywordFilter), getSubscriberGetProductList());
    }

    private Subscriber<ProductListSellerModel> getSubscriberGetProductList() {
        return new Subscriber<ProductListSellerModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorGetProductList(e);
            }

            @Override
            public void onNext(ProductListSellerModel productListSellerModel) {
                getView().onSuccessGetProductList(getProductListPickerMapperView.transform(productListSellerModel));
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        getProductListSellingUseCase.unsubscribe();
    }
}
