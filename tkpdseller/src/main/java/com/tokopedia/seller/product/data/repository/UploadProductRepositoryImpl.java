package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.mapper.AddProductInputMapper;
import com.tokopedia.seller.product.data.mapper.AddProductPictureMapper;
import com.tokopedia.seller.product.data.mapper.AddProductSubmitMapper;
import com.tokopedia.seller.product.data.mapper.AddProductValidationMapper;
import com.tokopedia.seller.product.data.mapper.EditProductMapper;
import com.tokopedia.seller.product.data.source.UploadProductDataSource;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.domain.UploadProductRepository;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.domain.model.EditProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

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
    public Observable<AddProductValidationDomainModel> addProductValidation(UploadProductInputDomainModel domainModel) {
        AddProductValidationInputServiceModel serviceModel = AddProductInputMapper.mapValidation(domainModel);
        return uploadProductDataSource.addProductValidation(serviceModel)
                .map(new AddProductValidationMapper());
    }

    @Override
    public Observable<AddProductPictureDomainModel> addProductPicture(AddProductPictureInputDomainModel domainModel) {
        AddProductPictureInputServiceModel serviceModel = AddProductInputMapper.mapPicture(domainModel);
        return uploadProductDataSource.addProductPicture(serviceModel)
                .map(new AddProductPictureMapper());
    }

    @Override
    public Observable<AddProductDomainModel> addProductSubmit(AddProductSubmitInputDomainModel domainModel) {
        AddProductSubmitInputServiceModel serviceModel = AddProductInputMapper.mapSubmit(domainModel);
        return uploadProductDataSource.addProductSubmit(serviceModel)
                .map(new AddProductSubmitMapper());
    }

    @Override
    public Observable<EditProductDomainModel> editProduct(UploadProductInputDomainModel uploadProductInputDomainModel) {
        EditProductInputServiceModel serviceModel = AddProductInputMapper.mapEdit(uploadProductInputDomainModel);
        return uploadProductDataSource.editProduct(serviceModel)
                .map(new EditProductMapper());
    }
}
