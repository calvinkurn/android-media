package com.tokopedia.seller.product.variant.data.source;

import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.seller.product.variant.data.cloud.ProductVariantCloud;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;

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

    public Observable<List<ProductVariantByCatModel>> fetchProductVariantByCat(long categoryId) {
        return productVariantCloud.fetchProductVariantByCat(categoryId)
                .map(new DataResponseMapper<List<ProductVariantByCatModel>>());
    }

    public Observable<ProductVariantByPrdModel> fetchProductVariantByPrd(long productId) {
        return productVariantCloud.fetchProductVariantByPrd(productId)
                .map(new DataResponseMapper<ProductVariantByPrdModel>());
    }
}