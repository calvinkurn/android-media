package com.tokopedia.discovery.newdiscovery.domain.usecase;

import android.content.Context;

import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class ProductWishlistUrlUseCase extends UseCase<Boolean> {

    public static final String PRODUCT_WISHLIST_URL = "product_wishlist_url";
    private final TopAdsService topAdsService;
    private final Context context;

    public ProductWishlistUrlUseCase(TopAdsService topAdsService, Context context) {
        this.topAdsService = topAdsService;
        this.context = context;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return topAdsService.productWishlistUrl(requestParams.getString(PRODUCT_WISHLIST_URL, ""))
                .map(new Func1<Response<String>, Boolean>() {
                    @Override
                    public Boolean call(Response<String> response) {
                        if(response.isSuccessful()){
                            try {
                                JSONObject object = new JSONObject(response.body());
                                return object.getJSONObject("data").getBoolean("success");
                            } catch (JSONException e) {
                                return false;
                            }
                        } else {
                            throw new RuntimeHttpErrorException(response.code());
                        }
                    }
                });
    }
}
