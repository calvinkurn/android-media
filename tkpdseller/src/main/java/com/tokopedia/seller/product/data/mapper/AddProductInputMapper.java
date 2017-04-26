package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.ProductPhotoListServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.ProductPhotoServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.ProductWholesaleServiceModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.domain.model.ProductWholesaleDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductInputMapper {

    public static AddProductSubmitInputServiceModel mapSubmit(AddProductSubmitInputDomainModel domainModel) {
        AddProductSubmitInputServiceModel serviceModel = new AddProductSubmitInputServiceModel();
        serviceModel.setPostKey(domainModel.getPostKey());
        serviceModel.setFileUploaded(domainModel.getFileUploadedTo());
        serviceModel.setProductEtalseId(domainModel.getProductEtalaseId());
        serviceModel.setProductUploadTo(domainModel.getProductUploadTo());
        return serviceModel;
    }

}
