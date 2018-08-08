package com.tokopedia.seller.product.variant.repository;

import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprdold.ProductVariantByPrdModel;

import java.util.List;

import rx.Observable;

/**
 * Created by hendry on 8/14/2017.
 */

public interface ProductVariantRepository {
    Observable<List<ProductVariantByCatModel>> fetchProductVariantByCat(long categoryId);
    Observable<ProductVariantByPrdModel> fetchProductVariantByPrd(long productId);
}
