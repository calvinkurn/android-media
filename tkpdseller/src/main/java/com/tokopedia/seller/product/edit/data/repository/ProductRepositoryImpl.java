package com.tokopedia.seller.product.edit.data.repository;

import com.tokopedia.seller.product.edit.data.mapper.AddProductSubmitMapper;
import com.tokopedia.seller.product.edit.data.source.ProductDataSource;
import com.tokopedia.seller.product.edit.domain.ProductRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class ProductRepositoryImpl implements ProductRepository {
    private final ProductDataSource productDataSource;

    public ProductRepositoryImpl(ProductDataSource productDataSource) {
        this.productDataSource = productDataSource;
    }

    @Override
    public Observable<AddProductDomainModel> addProductSubmit(ProductViewModel productViewModel) {
        return productDataSource.addProductSubmit(productViewModel)
                .map(new AddProductSubmitMapper());
    }

    @Override
    public Observable<AddProductDomainModel> editProduct(ProductViewModel productViewModel) {
        return productDataSource.editProduct(productViewModel)
                .map(new AddProductSubmitMapper());
    }

    @Override
    public Observable<ProductViewModel> getProductDetail(String productId) {
        return productDataSource.getProductDetail(productId);
    }
}
