package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.seller.product.edit.data.source.cloud.UploadProductCloud;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.DeleteProductPictureServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ProductUploadResultModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductvalidation.AddProductValidationServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editimageproduct.EditImageProductServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproduct.EditProductServiceModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

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

    public Observable<ProductUploadResultModel> addProductSubmit(ProductViewModel serviceModel) {
        return uploadProductCloud.addProductSubmit(serviceModel);
    }

    public Observable<ProductUploadResultModel> editProduct(ProductViewModel productViewModel) {
        return uploadProductCloud.editProduct(productViewModel);
    }
}
