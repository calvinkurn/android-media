package com.tokopedia.seller.product.variant.data.source;

import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.variant.data.cloud.ProductVariantCloud;
import com.tokopedia.seller.product.variant.data.model.ProductVariantModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author hendry on 4/7/17.
 */

public class ProductVariantDataSource {

    private ProductVariantCloud productVariantCloud;

    @Inject
    public ProductVariantDataSource(ProductVariantCloud productVariantCloud) {
        this.productVariantCloud = productVariantCloud;
    }

    public Observable<List<ProductVariantModel>> fetchProductVariant(long categoryId) {
        return productVariantCloud.fetchProductVariant(categoryId)
                .map(new SimpleDataResponseMapper<List<ProductVariantModel>>());
    }
}