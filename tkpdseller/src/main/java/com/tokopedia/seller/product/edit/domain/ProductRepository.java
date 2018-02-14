package com.tokopedia.seller.product.edit.domain;

import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.edit.domain.model.EditImageProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ProductRepository {
    Observable<AddProductDomainModel> addProductSubmit(ProductViewModel productViewModel);

    Observable<AddProductDomainModel> editProduct(ProductViewModel productViewModel);

    Observable<ProductViewModel> getProductDetail(String productId);

}
