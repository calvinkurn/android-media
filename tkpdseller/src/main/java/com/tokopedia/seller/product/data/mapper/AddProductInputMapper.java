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
    public static AddProductValidationInputServiceModel mapValidation(UploadProductInputDomainModel uploadProductInputDomainModel) {
        return null;
    }

    public static AddProductPictureInputServiceModel mapPicture(AddProductPictureInputDomainModel addProductValidationDomainModel) {
        return null;
    }

    public static AddProductSubmitInputServiceModel mapSubmit(AddProductSubmitInputDomainModel domainModel) {
        return null;
    }
}
