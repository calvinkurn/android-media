package com.tokopedia.seller.product.data.source.cloud;

import com.tokopedia.seller.product.data.source.cloud.api.UploadProductApi;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductpicture.AddProductPictureServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductvalidation.AddProductValidationServiceModel;
import com.tokopedia.seller.shopscore.data.common.GetData;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class UploadProductCloud {
    private final UploadProductApi api;

    public UploadProductCloud(UploadProductApi api) {
        this.api = api;
    }

    public Observable<AddProductValidationServiceModel> addProductValidation(AddProductValidationInputServiceModel serviceModel) {
        return api.addProductValidation(serviceModel.generateMapParam())
                .map(new GetData<AddProductValidationServiceModel>());
    }

    public Observable<AddProductPictureServiceModel> addProductPicture(AddProductPictureInputServiceModel serviceModel) {
        return api.addProductPicture(serviceModel.generateMapParam())
                .map(new GetData<AddProductPictureServiceModel>());
    }

    public Observable<AddProductSubmitServiceModel> addProductSubmit(AddProductSubmitInputServiceModel serviceModel) {
        return api.addProductSubmit(serviceModel.generateMapParam())
                .map(new GetData<AddProductSubmitServiceModel>());
    }
}
