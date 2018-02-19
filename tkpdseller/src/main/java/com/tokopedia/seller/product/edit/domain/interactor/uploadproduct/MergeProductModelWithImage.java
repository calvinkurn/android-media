package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */


public class MergeProductModelWithImage implements Func1<List<ProductPictureViewModel>, ProductViewModel> {

    private final ProductViewModel productViewModel;

    public MergeProductModelWithImage(ProductViewModel productViewModel) {
        this.productViewModel = productViewModel;
    }
    @Override
    public ProductViewModel call(List<ProductPictureViewModel> productPictureViewModels) {
        productViewModel.setProductPictureViewModelList(productPictureViewModels);
        return productViewModel;
    }

}