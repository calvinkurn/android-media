package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductInputMapper {
    public static AddProductValidationInputServiceModel mapValidation(UploadProductInputDomainModel domainModel) {

        AddProductValidationInputServiceModel serviceModel = new AddProductValidationInputServiceModel();
        serviceModel.setClickName(domainModel.getClickName());
        serviceModel.setDuplicate(domainModel.getDuplicate());
        serviceModel.setProductPriceOne(domainModel.getProductPriceOne());
        serviceModel.setProductPriceTwo(domainModel.getProductPriceTwo());
        serviceModel.setProductPriceThree(domainModel.getProductPriceThree());
        serviceModel.setProductPriceFour(domainModel.getProductPriceFour());
        serviceModel.setProductPriceFive(domainModel.getProductPriceFive());
        serviceModel.setProductCatalogId(domainModel.getProductCatalogId());
        serviceModel.setProductCondition(domainModel.getProductCondition());
        serviceModel.setProductDepartmentId(domainModel.getProductDepartmentId());
        serviceModel.setProductDescription(domainModel.getProductDescription());
        serviceModel.setProductEtalaseId(domainModel.getProductEtalaseId());
        serviceModel.setProductEtalaseName(domainModel.getProductEtalaseName());
        serviceModel.setProductMinOrder(domainModel.getProductMinOrder());
        serviceModel.setProductMustInsurance(domainModel.getProductMustInsurance());
        serviceModel.setProductName(domainModel.getProductName());
        serviceModel.setProductPhoto(domainModel.getProductPhoto());
        serviceModel.setProductPhotoDefault(domainModel.getProductPhotoDefault());
        serviceModel.setProductPhotoDescription(domainModel.getProductPhotoDescription());
        serviceModel.setProductPrice(domainModel.getProductPrice());
        serviceModel.setProductPriceCurrency(domainModel.getProductPriceCurrency());
        serviceModel.setProductReturnable(domainModel.getProductReturnable());
        serviceModel.setProductUploadTo(domainModel.getProductUploadTo());
        serviceModel.setProductWeight(domainModel.getProductWeight());
        serviceModel.setProductWeightUnit(domainModel.getProductWeightUnit());
        serviceModel.setQuantityMaxOne(domainModel.getQuantityMaxOne());
        serviceModel.setQuantityMaxTwo(domainModel.getQuantityMaxTwo());
        serviceModel.setQuantityMaxThree(domainModel.getQuantityMaxThree());
        serviceModel.setQuantityMaxFour(domainModel.getQuantityMaxFour());
        serviceModel.setQuantityMaxFive(domainModel.getQuantityMaxFive());
        serviceModel.setQuantityMinOne(domainModel.getQuantityMinOne());
        serviceModel.setQuantityMinTwo(domainModel.getQuantityMinTwo());
        serviceModel.setQuantityMinThree(domainModel.getQuantityMinThree());
        serviceModel.setQuantityMinFour(domainModel.getQuantityMinFour());
        serviceModel.setQuantityMinFive(domainModel.getQuantityMinFive());
        serviceModel.setPoProcessType(domainModel.getPoProcessType());
        serviceModel.setPoProcessValue(domainModel.getPoProcessValue());
        serviceModel.setServerId(domainModel.getServerId());

        return serviceModel;
    }

    public static AddProductPictureInputServiceModel mapPicture(AddProductPictureInputDomainModel domainModel) {
        AddProductPictureInputServiceModel serviceModel = new AddProductPictureInputServiceModel();
        serviceModel.setDuplicate(domainModel.getDuplicate());
        serviceModel.setProductPhoto(domainModel.getProductPhoto());
        serviceModel.setProductPhotoDefault(domainModel.getProductPhotoDefault());
        serviceModel.setProductPhotoDesc(domainModel.getProductPhotoDesc());
        serviceModel.setServerId(domainModel.getServerId());
        return serviceModel;
    }

    public static AddProductSubmitInputServiceModel mapSubmit(AddProductSubmitInputDomainModel domainModel) {
        AddProductSubmitInputServiceModel serviceModel = new AddProductSubmitInputServiceModel();
        serviceModel.setClickName(domainModel.getClickName());
        serviceModel.setPostKey(domainModel.getPostKey());
        serviceModel.setFileUploaded(domainModel.getFileUploadedTo());
        serviceModel.setProductEtalaseName(domainModel.getProductEtalaseName());
        serviceModel.setProductEtalseId(domainModel.getProductEtalaseId());
        serviceModel.setProductUploadTo(domainModel.getProductUploadTo());
        return serviceModel;
    }
}
