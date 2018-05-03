package com.tokopedia.tokocash.autosweepmf.data.source.cloud.api;

import com.google.gson.JsonObject;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepLimit;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

import static com.tokopedia.tokocash.network.api.WalletUrl.AutoSweep.API_AUTO_SWEEP_HOME;

/**
 * Retrofit interface for autosweepmf apis
 */
public interface AutoSweepApi {
    @GET(API_AUTO_SWEEP_HOME)
    Observable<Response<ResponseAutoSweepDetail>> getAutoSweepDetail();

    @POST(API_AUTO_SWEEP_HOME)
    Observable<Response<ResponseAutoSweepLimit>> postAutoSweepLimit(@Body JsonObject data);
}
