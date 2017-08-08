package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.mapper.AddProductInputMapper;
import com.tokopedia.seller.product.data.mapper.AddProductSubmitMapper;
import com.tokopedia.seller.product.data.mapper.AddProductValidationInputMapper;
import com.tokopedia.seller.product.data.mapper.AddProductValidationMapper;
import com.tokopedia.seller.product.data.mapper.DeleteProductPictureMapper;
import com.tokopedia.seller.product.data.mapper.EditProductImageMapper;
import com.tokopedia.seller.product.data.mapper.EditProductInputMapper;
import com.tokopedia.seller.product.data.mapper.EditProductMapper;
import com.tokopedia.seller.product.data.source.UploadProductDataSource;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.edit.domain.model.EditImageProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class UploadProductRepositoryImpl implements UploadProductRepository {
    private final UploadProductDataSource uploadProductDataSource;
    private final AddProductValidationInputMapper addProductValidationInputMapper;
    private final EditProductInputMapper editProductInputMapper;

    public UploadProductRepositoryImpl(UploadProductDataSource uploadProductDataSource, AddProductValidationInputMapper addProductValidationInputMapper, EditProductInputMapper editProductInputMapper) {
        this.uploadProductDataSource = uploadProductDataSource;
        this.addProductValidationInputMapper = addProductValidationInputMapper;
        this.editProductInputMapper = editProductInputMapper;
    }

    @Override
    public Observable<AddProductValidationDomainModel> addProductValidation(UploadProductInputDomainModel domainModel) {
        AddProductValidationInputServiceModel serviceModel = new AddProductValidationInputServiceModel();
        addProductValidationInputMapper.map(serviceModel, domainModel);
        return uploadProductDataSource.addProductValidation(serviceModel)
                .map(new AddProductValidationMapper());
    }

    @Override
    public Observable<AddProductDomainModel> addProductSubmit(AddProductSubmitInputDomainModel domainModel) {
        AddProductSubmitInputServiceModel serviceModel = AddProductInputMapper.mapSubmit(domainModel);
        return uploadProductDataSource.addProductSubmit(serviceModel)
                .map(new AddProductSubmitMapper());
    }

    @Override
    public Observable<Boolean> editProduct(UploadProductInputDomainModel uploadProductInputDomainModel) {
        EditProductInputServiceModel serviceModel = new EditProductInputServiceModel();
        editProductInputMapper.map(serviceModel, uploadProductInputDomainModel);
        return uploadProductDataSource.editProduct(serviceModel)
                .map(new EditProductMapper());
    }

    @Override
    public Observable<EditImageProductDomainModel> editImageProduct(String picObj) {
        return uploadProductDataSource.editProductImage(picObj)
                .map(new EditProductImageMapper());
    }

    @Override
    public Observable<ImageProductInputDomainModel> deleteProductPicture(String picId, String productId) {
        return uploadProductDataSource.deleteProductPicture(picId, productId)
                .map(new DeleteProductPictureMapper());
    }
}
