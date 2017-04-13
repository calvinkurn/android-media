package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.source.cloud.UploadProductCloud;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.EditProductServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductpicture.AddProductPictureServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductvalidation.AddProductValidationServiceModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class UploadProductDataSource {
    private final UploadProductCloud uploadProductCloud;

    @Inject
    public UploadProductDataSource(UploadProductCloud uploadProductCloud) {
        this.uploadProductCloud = uploadProductCloud;
    }

    public Observable<AddProductValidationServiceModel> addProductValidation(AddProductValidationInputServiceModel serviceModel){
        return uploadProductCloud.addProductValidation(serviceModel);
    }

    public Observable<AddProductPictureServiceModel> addProductPicture(AddProductPictureInputServiceModel serviceModel) {
        return uploadProductCloud.addProductPicture(serviceModel);
    }

    public Observable<AddProductSubmitServiceModel> addProductSubmit(AddProductSubmitInputServiceModel serviceModel) {
        return uploadProductCloud.addProductSubmit(serviceModel);
    }

    public Observable<EditProductServiceModel> editProduct(EditProductInputServiceModel serviceModel) {
        return null;
    }
}
