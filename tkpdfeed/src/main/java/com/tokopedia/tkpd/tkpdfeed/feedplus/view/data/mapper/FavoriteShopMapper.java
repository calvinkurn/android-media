package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 5/26/17.
 */

public class FavoriteShopMapper implements Func1<Response<TkpdResponse>, String> {

    @Override
    public String call(Response<TkpdResponse> tkpdResponseResponse) {
        if (tkpdResponseResponse.isSuccessful())
            if (!tkpdResponseResponse.body().isError()) {
                return "sukses";
            } else {
                String errorMessage = tkpdResponseResponse.body().getErrorMessages().toString();
                return errorMessage;
            }
        else {
            return "gagal";
        }
    }
}
