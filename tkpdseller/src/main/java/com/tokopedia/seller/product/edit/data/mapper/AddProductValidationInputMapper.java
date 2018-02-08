package com.tokopedia.seller.product.edit.data.mapper;

import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ProductWholesaleServiceModel;
import com.tokopedia.seller.product.edit.domain.model.ProductWholesaleDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class AddProductValidationInputMapper extends UploadProductPictureInputMapper{

    @Inject
    public AddProductValidationInputMapper() {
    }

    public void map(AddProductValidationInputServiceModel serviceModel, UploadProductInputDomainModel domainModel) {
        mapProductPhoto(serviceModel, domainModel);
        serviceModel.setProductWholesale(mapWholesale(domainModel.getProductPriceOne()));
        serviceModel.setProductCatalogId(domainModel.getProductCatalogId());
        serviceModel.setProductCondition(domainModel.getProductCondition());
        serviceModel.setProductDepartmentId(domainModel.getProductDepartmentId());
        serviceModel.setProductDescription(domainModel.getProductDescription());
        serviceModel.setProductEtalaseId(domainModel.getProductEtalaseId());
        serviceModel.setProductMinOrder(domainModel.getProductMinOrder());
        serviceModel.setProductMustInsurance(domainModel.getProductMustInsurance());
        serviceModel.setProductName(domainModel.getProductName());
        serviceModel.setProductPrice(domainModel.getProductPrice());
        serviceModel.setProductPriceCurrency(domainModel.getProductPriceCurrency());
        serviceModel.setProductReturnable(domainModel.getProductReturnable());
        serviceModel.setProductUploadTo(domainModel.getProductUploadTo());
        serviceModel.setProductInvenageSwitch(domainModel.getProductInvenageSwitch());
        serviceModel.setProductInvenageValue(domainModel.getProductInvenageValue());
        serviceModel.setProductWeight(domainModel.getProductWeight());
        serviceModel.setProductWeightUnit(domainModel.getProductWeightUnit());
        serviceModel.setPoProcessType(domainModel.getPoProcessType());
        serviceModel.setPoProcessValue(domainModel.getPoProcessValue());
        serviceModel.setProductVideo(domainModel.getProductVideos());
        serviceModel.setSwitchVariant(domainModel.getSwitchVariant());
        if (domainModel.getSwitchVariant() > 0 ) {
            serviceModel.setProductVariantDataSubmit(domainModel.getProductVariantDataSubmit());
        }
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
}
