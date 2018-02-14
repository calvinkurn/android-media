package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.seller.product.edit.data.source.cloud.ProductCloud;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ProductUploadResultModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class ProductDataSource {
    private final ProductCloud productCloud;

    @Inject
    public ProductDataSource(ProductCloud productCloud) {
        this.productCloud = productCloud;
    }

    public Observable<ProductUploadResultModel> addProductSubmit(ProductViewModel serviceModel) {
        return productCloud.addProductSubmit(serviceModel);
    }

    public Observable<ProductUploadResultModel> editProduct(ProductViewModel productViewModel) {
        return productCloud.editProduct(productViewModel);
    }

    public Observable<ProductViewModel> getProductDetail(String productId) {
        return productCloud.getProductDetail(productId);
    }
}
