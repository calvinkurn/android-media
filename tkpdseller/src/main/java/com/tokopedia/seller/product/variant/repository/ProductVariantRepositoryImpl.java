package com.tokopedia.seller.product.variant.repository;

import com.tokopedia.seller.product.category.di.scope.CategoryPickerScope;
import com.tokopedia.seller.product.variant.data.model.ProductVariantModel;
import com.tokopedia.seller.product.variant.data.source.ProductVariantDataSource;

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

    public Observable<List<ProductVariantModel>> fetchProductVariant(long categoryId) {
        return productVariantDataSource.fetchProductVariant(categoryId);
    }
}

