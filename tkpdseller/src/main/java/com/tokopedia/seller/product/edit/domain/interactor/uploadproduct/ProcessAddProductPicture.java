package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.mapper.AddProductDomainMapper;
import com.tokopedia.seller.product.edit.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class ProcessAddProductPicture implements Func1<AddProductPictureDomainModel, AddProductSubmitInputDomainModel> {

    private final UploadProductInputDomainModel uploadProductInputDomainModel;

    private final String postKey;

    public ProcessAddProductPicture(UploadProductInputDomainModel uploadProductInputDomainModel, String postKey) {
        this.uploadProductInputDomainModel = uploadProductInputDomainModel;
        this.postKey = postKey;
    }
    @Override
    public AddProductSubmitInputDomainModel call(AddProductPictureDomainModel addProductPictureDomainModel) {
        return AddProductDomainMapper.mapUploadToSubmit(addProductPictureDomainModel, uploadProductInputDomainModel, postKey);
    }

}