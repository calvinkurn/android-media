package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */


public class PrepareAddProductValidation implements Func1<List<ImageProductInputDomainModel>, UploadProductInputDomainModel> {

    private final UploadProductInputDomainModel domainModel;

    public PrepareAddProductValidation(UploadProductInputDomainModel domainModel) {
        this.domainModel = domainModel;
    }
    @Override
    public UploadProductInputDomainModel call(List<ImageProductInputDomainModel> imageProductInputDomainModels) {
        ProductPhotoListDomainModel productPhotos = domainModel.getProductPhotos();
        productPhotos.setPhotos(imageProductInputDomainModels);
        return domainModel;
    }

}