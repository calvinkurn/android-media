package com.tokopedia.seller.topads.data.mapper;

import com.tokopedia.seller.topads.data.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.data.model.response.DataResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/1/17.
 */
public class TopAdsBulkActionMapper implements Func1<Response<DataResponse<ProductAdBulkAction>>, ProductAdBulkAction> {
    @Override
    public ProductAdBulkAction call(Response<DataResponse<ProductAdBulkAction>> dataResponseResponse) {
        return mappingresponse(dataResponseResponse);
    }

    private ProductAdBulkAction mappingresponse(Response<DataResponse<ProductAdBulkAction>> dataResponseResponse) {
        if (dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null
                && dataResponseResponse.body().getData() != null) {
            return dataResponseResponse.body().getData();
        } else {
            return null;
        }
    }
}
