package com.tokopedia.seller.manageitem.domain.repository;



import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductViewModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ProductRepository {

    Observable<Integer> addProductSubmit(ProductViewModel productViewModel);

    Observable<Integer> editProductSubmit(ProductViewModel productViewModel);

    Observable<ProductViewModel> getProductDetail(String productId);

}
