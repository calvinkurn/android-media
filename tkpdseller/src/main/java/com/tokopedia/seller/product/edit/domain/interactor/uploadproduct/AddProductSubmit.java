package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import android.text.TextUtils;

import com.tokopedia.seller.product.edit.domain.ProductRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */


public class AddProductSubmit implements Func1<ProductViewModel, Observable<AddProductDomainModel>> {

    private final ProductRepository productRepository;

    public AddProductSubmit(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Observable<AddProductDomainModel> call(ProductViewModel productViewModel) {
        if(TextUtils.isEmpty(productViewModel.getProductId())) {
            return productRepository.addProductSubmit(productViewModel);
        }else{
            return productRepository.editProductSubmit(productViewModel);
        }
    }

}