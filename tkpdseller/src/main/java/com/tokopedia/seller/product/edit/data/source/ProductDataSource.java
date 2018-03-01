package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.seller.product.edit.data.source.cloud.ProductCloud;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ProductUploadResultModel;
import com.tokopedia.seller.product.edit.domain.mapper.ProductUploadMapper;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class ProductDataSource {

    private final ProductCloud productCloud;
    private final ProductUploadMapper productUploadMapper;

    @Inject
    public ProductDataSource(ProductCloud productCloud, ProductUploadMapper productUploadMapper) {
        this.productCloud = productCloud;
        this.productUploadMapper = productUploadMapper;
    }

    public Observable<Boolean> addProductSubmit(ProductViewModel productViewModel) {
        return productCloud.addProductSubmit(productUploadMapper.removeUnusedParam(productViewModel));
    }

    public Observable<Boolean> editProduct(ProductViewModel productViewModel) {
        return productCloud.editProduct(productViewModel.getProductId(), productUploadMapper.removeUnusedParam(productViewModel));
    }

    public Observable<ProductViewModel> getProductDetail(String productId) {
        return productCloud.getProductDetail(productId);
    }
}
