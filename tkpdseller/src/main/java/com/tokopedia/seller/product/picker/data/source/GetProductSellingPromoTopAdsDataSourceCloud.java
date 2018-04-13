package com.tokopedia.seller.product.picker.data.source;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.product.apis.PromoTopAdsApi;
import com.tokopedia.core.product.facade.NetworkParam;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hadi-putra on 11/04/18.
 */

public class GetProductSellingPromoTopAdsDataSourceCloud {
    private static final String PARAM_ADS_TYPE = "type";
    private static final String PARAM_DEVICE_ID = "device";

    private final PromoTopAdsApi promoTopAdsApi;

    public GetProductSellingPromoTopAdsDataSourceCloud(PromoTopAdsApi promoTopAdsApi) {
        this.promoTopAdsApi = promoTopAdsApi;
    }

    public Observable<String> checkPromoAds(RequestParams requestParams) {
        requestParams.putString(PARAM_ADS_TYPE, "1");
        requestParams.putString(PARAM_DEVICE_ID, "android");
        return promoTopAdsApi.checkPromoAds(requestParams.getParamsAllValueInString())
                .map(new Func1<Response<String>, String>() {
                    @Override
                    public String call(Response<String> stringResponse) {
                        if (stringResponse.isSuccessful()){
                            try {
                                JSONObject object = new JSONObject(stringResponse.body());
                                return object.getJSONObject("data").getString("ad_id");
                            } catch (JSONException e) {
                                return null;
                            }
                        }
                        return null;
                    }
                });
    }
}
