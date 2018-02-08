package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.product.edit.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.mapper.AddProductDomainMapper;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */


public class ProcessAddProductValidation implements Func1<AddProductValidationDomainModel, Observable<AddProductDomainModel>> {

    private final UploadProductInputDomainModel uploadProductInputDomainModel;
    private final ImageProductUploadRepository imageProductUploadRepository;
    private final UploadProductRepository uploadProductRepository;

    public ProcessAddProductValidation(UploadProductInputDomainModel uploadProductInputDomainModel, ImageProductUploadRepository imageProductUploadRepository, UploadProductRepository uploadProductRepository) {
        this.uploadProductInputDomainModel = uploadProductInputDomainModel;
        this.imageProductUploadRepository = imageProductUploadRepository;
        this.uploadProductRepository = uploadProductRepository;
    }
    @Override
    public Observable<AddProductDomainModel> call(AddProductValidationDomainModel addProductValidationDomainModel) {
        String postKey = addProductValidationDomainModel.getPostKey();
        if (StringUtils.isNotBlank(postKey)) {
            uploadProductInputDomainModel.setPostKey(postKey);
            return Observable.just(uploadProductInputDomainModel)
                    .flatMap(new AddProductPicture(imageProductUploadRepository))
                    .map(new ProcessAddProductPicture(uploadProductInputDomainModel, postKey))
                    .flatMap(new AddProductSubmit(uploadProductRepository));
        } else {
            return Observable.just(AddProductDomainMapper.mapValidationToSubmit(addProductValidationDomainModel));
        }
    }

}