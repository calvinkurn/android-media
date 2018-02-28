package com.tokopedia.seller.product.edit.domain;

import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ProductRepository {

    Observable<ProductViewModel> addProductSubmit(ProductViewModel productViewModel);

    Observable<ProductViewModel> editProductSubmit(ProductViewModel productViewModel);

    Observable<ProductViewModel> getProductDetail(String productId);

}
