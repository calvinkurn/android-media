package com.tokopedia.seller.product.variant.repository;

import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;

import java.util.List;

import rx.Observable;

/**
 * Created by hendry on 8/14/2017.
 */

public interface ProductVariantRepository {
    Observable<List<ProductVariantByCatModel>> fetchProductVariant(long categoryId);
}
