package com.tokopedia.seller.product.edit.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.product.edit.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.DeleteProductPictureServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductvalidation.AddProductValidationServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editimageproduct.EditImageProductServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproduct.EditProductServiceModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.shopscore.data.common.GetData;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class UploadProductCloud {
    private final TomeApi tomeApi;

    @Inject
    public UploadProductCloud(TomeApi tomeApi) {
        this.tomeApi = tomeApi;
    }

    public Observable<AddProductSubmitServiceModel> addProductSubmit(ProductViewModel productViewModel) {
        return tomeApi.addProductSubmit(productViewModel)
                .map(new GetData<AddProductSubmitServiceModel>());
    }

    public Observable<AddProductSubmitServiceModel> editProduct(ProductViewModel productViewModel) {
        return tomeApi.editProductSubmit(productViewModel, String.valueOf(productViewModel.getProductId()))
                .map(new GetData<AddProductSubmitServiceModel>());
    }
}
