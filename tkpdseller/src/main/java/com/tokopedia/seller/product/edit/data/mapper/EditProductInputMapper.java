package com.tokopedia.seller.product.edit.data.mapper;

import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class EditProductInputMapper extends AddProductValidationInputMapper{

    @Inject
    public EditProductInputMapper() {
    }

    public void map(EditProductInputServiceModel serviceModel, UploadProductInputDomainModel domainModel){
        map((AddProductValidationInputServiceModel) serviceModel, domainModel);
        serviceModel.setProductChangeCatalog(domainModel.getProductChangeCatalog());
        serviceModel.setProductChangePhoto(domainModel.getProductChangePhoto());
        serviceModel.setProductChangeWholesale(domainModel.getProductChangeWholesale());
        serviceModel.setProductId(domainModel.getProductId());
    }

}
