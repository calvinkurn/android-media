package com.tokopedia.seller.product.edit.domain.mapper;

import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductDomainMapper {
    public static AddProductDomainModel mapValidationToSubmit(AddProductValidationDomainModel validationDomainModel) {
        AddProductDomainModel domainModel = new AddProductDomainModel();
        domainModel.setProductId(validationDomainModel.getProductId());
        domainModel.setProductName(validationDomainModel.getProductName());
        domainModel.setProductUrl(validationDomainModel.getProductUrl());
        domainModel.setProductDesc(validationDomainModel.getProductDesc());
        domainModel.setProductDest(validationDomainModel.getProductDest());
        domainModel.setProductEtalase(validationDomainModel.getProductEtalase());
        return domainModel;
    }

    public static AddProductPictureInputDomainModel mapUploadToPicture(UploadProductInputDomainModel uploadProductInputDomainModel) {
        AddProductPictureInputDomainModel domainModel = new AddProductPictureInputDomainModel();
        domainModel.setProductPhotos(uploadProductInputDomainModel.getProductPhotos());
        domainModel.setServerId(uploadProductInputDomainModel.getServerId());
        domainModel.setHostUrl(uploadProductInputDomainModel.getHostUrl());
        return domainModel;
    }

    public static AddProductSubmitInputDomainModel mapUploadToSubmit(AddProductPictureDomainModel addProductPictureDomainModel, UploadProductInputDomainModel uploadProductInputDomainModel, String postKey) {
        AddProductSubmitInputDomainModel domainModel = new AddProductSubmitInputDomainModel();
        domainModel.setPostKey(postKey);
        domainModel.setFileUploadedTo(addProductPictureDomainModel.getFileUploaded());
        domainModel.setProductEtalaseId(uploadProductInputDomainModel.getProductEtalaseId());
        domainModel.setProductUploadTo(uploadProductInputDomainModel.getProductUploadTo());
        return domainModel;
    }
}
