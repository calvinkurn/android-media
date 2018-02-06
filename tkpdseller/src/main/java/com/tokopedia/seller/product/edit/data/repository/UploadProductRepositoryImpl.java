package com.tokopedia.seller.product.edit.data.repository;

import com.tokopedia.seller.product.edit.data.mapper.AddProductInputMapper;
import com.tokopedia.seller.product.edit.data.mapper.AddProductSubmitMapper;
import com.tokopedia.seller.product.edit.data.mapper.AddProductValidationInputMapper;
import com.tokopedia.seller.product.edit.data.mapper.AddProductValidationMapper;
import com.tokopedia.seller.product.edit.data.mapper.DeleteProductPictureMapper;
import com.tokopedia.seller.product.edit.data.mapper.EditProductImageMapper;
import com.tokopedia.seller.product.edit.data.mapper.EditProductInputMapper;
import com.tokopedia.seller.product.edit.data.mapper.EditProductMapper;
import com.tokopedia.seller.product.edit.data.source.UploadProductDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.edit.domain.model.EditImageProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class UploadProductRepositoryImpl implements UploadProductRepository {
    private final UploadProductDataSource uploadProductDataSource;

    public UploadProductRepositoryImpl(UploadProductDataSource uploadProductDataSource) {
        this.uploadProductDataSource = uploadProductDataSource;
    }

    @Override
    public Observable<AddProductDomainModel> addProductSubmit(ProductViewModel productViewModel) {
        return uploadProductDataSource.addProductSubmit(productViewModel)
                .map(new AddProductSubmitMapper());
    }

    @Override
    public Observable<AddProductDomainModel> editProduct(ProductViewModel productViewModel) {
        return uploadProductDataSource.editProduct(productViewModel)
                .map(new AddProductSubmitMapper());
    }
}
