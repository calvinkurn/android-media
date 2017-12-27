package com.tokopedia.seller.product.edit.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.product.edit.data.source.cloud.api.UploadProductApi;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.DeleteProductPictureServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductvalidation.AddProductValidationServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editimageproduct.EditImageProductServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproduct.EditProductServiceModel;
import com.tokopedia.seller.shopscore.data.common.GetData;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class UploadProductCloud {
    public static final String PIC_OBJ = "pic_obj";
    public static final String PRODUCT_ID = "product_id";
    public static final String PICTURE_ID = "picture_id";
    public static final String SHOP_ID = "shop_id";
    private final UploadProductApi api;
    private final Context context;

    @Inject
    public UploadProductCloud(UploadProductApi api, @ApplicationContext Context context) {
        this.api = api;
        this.context = context;
    }

    public Observable<AddProductValidationServiceModel> addProductValidation(AddProductValidationInputServiceModel serviceModel) {
        return api.addProductValidation(AuthUtil.generateParamsNetwork(context, serviceModel.generateMapParam()))
                .map(new GetData<AddProductValidationServiceModel>());
    }

    public Observable<AddProductSubmitServiceModel> addProductSubmit(AddProductSubmitInputServiceModel serviceModel) {
        return api.addProductSubmit(AuthUtil.generateParamsNetwork(context, serviceModel.generateMapParam()))
                .map(new GetData<AddProductSubmitServiceModel>());
    }

    public Observable<EditProductServiceModel> editProduct(EditProductInputServiceModel serviceModel) {
        return api.editProduct(AuthUtil.generateParamsNetwork(context, serviceModel.generateMapParam()))
                .map(new GetData<EditProductServiceModel>());
    }

    public Observable<EditImageProductServiceModel> editProductImage(String picObj) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(PIC_OBJ, picObj);
        return api.editProductPicture(AuthUtil.generateParamsNetwork(context, params))
                .map(new GetData<EditImageProductServiceModel>());
    }

    public Observable<DeleteProductPictureServiceModel> deleteProductPicture(String picId, String productId) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(PRODUCT_ID, productId);
        params.put(PICTURE_ID, picId);
        params.put(SHOP_ID, SessionHandler.getShopID(context));
        return api.deleteProductPicture(AuthUtil.generateParamsNetwork(context, params))
                .map(new GetData<DeleteProductPictureServiceModel>());
    }
}
