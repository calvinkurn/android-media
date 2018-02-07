package com.tokopedia.seller.seller.info.data.source;

import com.tokopedia.seller.seller.info.constant.SellerInfoConstant;
import com.tokopedia.seller.seller.info.data.model.ResponseSellerInfoModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by normansyahputa on 11/30/17.
 */

public interface SellerInfoApi {
    @GET(SellerInfoConstant.LIST)
    Observable<Response<ResponseSellerInfoModel>> listSellerInfo(@QueryMap Map<String, String> params);
}
