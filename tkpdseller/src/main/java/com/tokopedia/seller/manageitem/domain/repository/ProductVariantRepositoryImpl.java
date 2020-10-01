package com.tokopedia.seller.manageitem.domain.repository;

import com.tokopedia.core.common.category.di.scope.CategoryPickerScope;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantByCatModel;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantByPrdModel;
import com.tokopedia.seller.manageitem.data.source.ProductVariantDataSource;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */
@CategoryPickerScope
public class ProductVariantRepositoryImpl implements ProductVariantRepository {
    private final ProductVariantDataSource productVariantDataSource;

    @Inject
    public ProductVariantRepositoryImpl(ProductVariantDataSource productVariantDataSource) {
        this.productVariantDataSource = productVariantDataSource;
    }

    public Observable<List<ProductVariantByCatModel>> fetchProductVariantByCat(long categoryId) {
        return productVariantDataSource.fetchProductVariantByCat(categoryId);
    }

    @Override
    public Observable<ProductVariantByPrdModel> fetchProductVariantByPrd(long productId) {
        return productVariantDataSource.fetchProductVariantByPrd(productId);
    }
}

