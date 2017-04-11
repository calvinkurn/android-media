package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.source.cloud.UploadProductCloud;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductpicture.AddProductPictureServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductvalidation.AddProductValidationServiceModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class UploadProductDataSource {
    private final UploadProductCloud uploadProductCloud;

    public UploadProductDataSource(UploadProductCloud uploadProductCloud) {
        this.uploadProductCloud = uploadProductCloud;
    }

    public Observable<AddProductValidationServiceModel> addProductValidation(AddProductValidationInputServiceModel serviceModel){
        return uploadProductCloud.addProductValidation(serviceModel);
    }

    public Observable<AddProductPictureServiceModel> addProductPicture(AddProductPictureInputServiceModel addProductPictureInputServiceModel) {
        return uploadProductCloud.addProductPicture(addProductPictureInputServiceModel);
    }

    public Observable<AddProductSubmitServiceModel> addProductSubmit(AddProductSubmitInputServiceModel serviceModel) {
        return uploadProductCloud.addProductSubmit(serviceModel);
    }
}
