package com.tokopedia.seller.product.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
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
    private final Context context;

    public UploadProductCloud(UploadProductApi api, Context context) {
        this.api = api;
        this.context = context;
    }

    public Observable<AddProductValidationServiceModel> addProductValidation(AddProductValidationInputServiceModel serviceModel) {
        return api.addProductValidation(AuthUtil.generateParamsNetwork(context, serviceModel.generateMapParam()))
                .map(new GetData<AddProductValidationServiceModel>());
    }

    public Observable<AddProductPictureServiceModel> addProductPicture(AddProductPictureInputServiceModel serviceModel) {
        return api.addProductPicture(AuthUtil.generateParamsNetwork(context,serviceModel.generateMapParam()))
                .map(new GetData<AddProductPictureServiceModel>());
    }

    public Observable<AddProductSubmitServiceModel> addProductSubmit(AddProductSubmitInputServiceModel serviceModel) {
        return api.addProductSubmit(AuthUtil.generateParamsNetwork(context, serviceModel.generateMapParam()))
                .map(new GetData<AddProductSubmitServiceModel>());
    }
}
