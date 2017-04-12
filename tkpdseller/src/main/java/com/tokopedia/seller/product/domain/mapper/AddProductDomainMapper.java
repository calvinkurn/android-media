package com.tokopedia.seller.product.domain.mapper;

import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductDomainMapper {
    public static AddProductDomainModel mapValidationToSubmit(AddProductValidationDomainModel addProductValidationDomainModel) {
        return null;
    }

    public static AddProductPictureInputDomainModel mapUploadToPicture(UploadProductInputDomainModel uploadProductInputDomainModel) {
        AddProductPictureInputDomainModel domainModel = new AddProductPictureInputDomainModel();
        domainModel.setDuplicate(uploadProductInputDomainModel.getDuplicate());
        domainModel.setProductPhoto(uploadProductInputDomainModel.getProductPhoto());
        domainModel.setProductPhotoDefault(uploadProductInputDomainModel.getProductPhotoDefault());
        domainModel.setProductPhotoDesc(uploadProductInputDomainModel.getProductPhotoDescription());
        domainModel.setServerId(uploadProductInputDomainModel.getServerId());
        return domainModel;
    }

    public static AddProductSubmitInputDomainModel mapUploadToSubmit(AddProductPictureDomainModel addProductPictureDomainModel, UploadProductInputDomainModel uploadProductInputDomainModel, String postKey) {
        AddProductSubmitInputDomainModel domainModel = new AddProductSubmitInputDomainModel();
        domainModel.setPostKey(postKey);
        domainModel.setClickName(uploadProductInputDomainModel.getClickName());
        domainModel.setFileUploadedTo(addProductPictureDomainModel.getFileUploaded());
        domainModel.setProductEtalaseId(uploadProductInputDomainModel.getProductEtalaseId());
        domainModel.setProductEtalaseName(uploadProductInputDomainModel.getProductEtalaseName());
        domainModel.setProductUploadTo(uploadProductInputDomainModel.getProductUploadTo());
        return domainModel;
    }
}
