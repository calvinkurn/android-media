package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.ProductWholesaleServiceModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.ProductWholesaleDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductInputMapper {
    public static AddProductValidationInputServiceModel mapValidation(UploadProductInputDomainModel domainModel) {

        AddProductValidationInputServiceModel serviceModel = new AddProductValidationInputServiceModel();
        serviceModel.setClickName(domainModel.getClickName());
        serviceModel.setDuplicate(domainModel.getDuplicate());
        serviceModel.setProductWholesale(mapWholesale(domainModel.getProductPriceOne()));
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
        serviceModel.setPoProcessType(domainModel.getPoProcessType());
        serviceModel.setPoProcessValue(domainModel.getPoProcessValue());
        serviceModel.setServerId(domainModel.getServerId());

        return serviceModel;
    }

    private static List<ProductWholesaleServiceModel> mapWholesale(List<ProductWholesaleDomainModel> wholesaleDomainModelList) {
        List<ProductWholesaleServiceModel> serviceModelList = new ArrayList<>();
        for (ProductWholesaleDomainModel domainModel : wholesaleDomainModelList){
            ProductWholesaleServiceModel serviceModel = new ProductWholesaleServiceModel();
            serviceModel.setPrice(domainModel.getPrice());
            serviceModel.setQtyMax(domainModel.getQtyMax());
            serviceModel.setQtyMin(domainModel.getQtyMin());
            serviceModelList.add(serviceModel);
        }
        return serviceModelList;
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
