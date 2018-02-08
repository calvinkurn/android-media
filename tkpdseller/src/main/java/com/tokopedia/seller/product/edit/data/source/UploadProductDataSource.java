package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.seller.product.edit.data.source.cloud.UploadProductCloud;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.DeleteProductPictureServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductvalidation.AddProductValidationServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editimageproduct.EditImageProductServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproduct.EditProductServiceModel;

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

    public Observable<AddProductSubmitServiceModel> addProductSubmit(AddProductSubmitInputServiceModel serviceModel) {
        return uploadProductCloud.addProductSubmit(serviceModel);
    }

    public Observable<EditProductServiceModel> editProduct(EditProductInputServiceModel serviceModel) {
        return uploadProductCloud.editProduct(serviceModel);
    }

    public Observable<EditImageProductServiceModel> editProductImage(String picObj) {
        return uploadProductCloud.editProductImage(picObj);
    }

    public Observable<DeleteProductPictureServiceModel> deleteProductPicture(String picId, String productId) {
        return uploadProductCloud.deleteProductPicture(picId, productId);
    }
}
