package com.tokopedia.seller.product.edit.data.source.cloud;

import com.tokopedia.seller.product.edit.data.source.cloud.api.ProductApi;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.shop.open.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class UploadProductCloud {
    private final ProductApi productApi;

    @Inject
    public UploadProductCloud(ProductApi productApi) {
        this.productApi = productApi;
    }

    public Observable<AddProductSubmitServiceModel> addProductSubmit(ProductViewModel productViewModel) {
        return productApi.addProductSubmit(productViewModel)
                .map(new Func1<Response<DataResponse<AddProductSubmitServiceModel>>, AddProductSubmitServiceModel>() {
                    @Override
                    public AddProductSubmitServiceModel call(Response<DataResponse<AddProductSubmitServiceModel>> dataResponseResponse) {
                        return null;
                    }
                });
//                .map(new GetData<AddProductSubmitServiceModel>());
    }

    public Observable<AddProductSubmitServiceModel> editProduct(ProductViewModel productViewModel) {
        return productApi.editProductSubmit(productViewModel, String.valueOf(productViewModel.getProductId()))
                .map(new Func1<Response<DataResponse<AddProductSubmitServiceModel>>, AddProductSubmitServiceModel>() {
                    @Override
                    public AddProductSubmitServiceModel call(Response<DataResponse<AddProductSubmitServiceModel>> dataResponseResponse) {
                        return null;
                    }
                });
//                .map(new GetData<AddProductSubmitServiceModel>());
    }
}
