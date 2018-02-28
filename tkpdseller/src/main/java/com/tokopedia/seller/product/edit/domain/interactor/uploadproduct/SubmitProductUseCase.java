package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import android.text.TextUtils;

import com.tokopedia.seller.product.edit.domain.ProductRepository;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class SubmitProductUseCase extends UseCase<ProductViewModel> {

    private static final String PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";

    private final ProductRepository productRepository;

    @Inject
    public SubmitProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Observable<ProductViewModel> createObservable(RequestParams requestParams) {
        ProductViewModel productViewModel = (ProductViewModel) requestParams.getObject(PRODUCT_VIEW_MODEL);
        if (TextUtils.isEmpty(productViewModel.getProductId())) {
            return productRepository.addProductSubmit(productViewModel);
        } else {
            return productRepository.editProductSubmit(productViewModel);
        }
    }

    public static RequestParams createParams(ProductViewModel productViewModel) {
        RequestParams params = RequestParams.create();
        params.putObject(PRODUCT_VIEW_MODEL, productViewModel);
        return params;
    }
}
