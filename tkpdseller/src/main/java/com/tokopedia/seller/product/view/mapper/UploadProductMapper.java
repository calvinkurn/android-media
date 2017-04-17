package com.tokopedia.seller.product.view.mapper;

import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class UploadProductMapper {
    public static UploadProductInputDomainModel map(UploadProductInputViewModel viewModel) {
        UploadProductInputDomainModel domainModel = new UploadProductInputDomainModel();
        domainModel.setProductName(viewModel.getProductName());
        return domainModel;
    }
}
