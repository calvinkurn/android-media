package com.tokopedia.seller.product.imagepicker;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.seller.product.imagepicker.model.CatalogImage;
import com.tokopedia.seller.product.imagepicker.model.DataResponseCatalogImage;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogImageDataSourceCloud {

    private CatalogApi catalogApi;

    @Inject
    public CatalogImageDataSourceCloud(CatalogApi catalogApi) {
        this.catalogApi = catalogApi;
    }

    public Observable<List<CatalogImage>> getCatalogImage(String catalogId) {
        return catalogApi.getCatalogImage(catalogId)
                .flatMap(new Func1<Response<DataResponse<DataResponseCatalogImage>>, Observable<List<CatalogImage>>>() {
                    @Override
                    public Observable<List<CatalogImage>> call(Response<DataResponse<DataResponseCatalogImage>> dataResponseResponse) {
                        if(dataResponseResponse.isSuccessful() && dataResponseResponse.body() != null
                                && dataResponseResponse.body().getData() != null) {
                            return Observable.just(dataResponseResponse.body().getData().getCatalogs().get(0).getCatalogImages());
                        }else{
                            throw new RuntimeException();
                        }
                    }
                });
    }
}
