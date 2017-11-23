package com.tokopedia.discovery.newdiscovery.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by errysuprayogi on 10/13/17.
 */

public class OfficialStoreBannerMapper implements Func1<Response<String>, OfficialStoreBannerModel> {

    private final Gson gson;

    public OfficialStoreBannerMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public OfficialStoreBannerModel call(Response<String> stringResponse) {
        OfficialStoreBannerModel bannerModel = new OfficialStoreBannerModel();
        if(stringResponse.isSuccessful()){
            bannerModel = gson.fromJson(stringResponse.body(), OfficialStoreBannerModel.class);
        } else {
            bannerModel.setStatus("ERROR");
        }
        return bannerModel;
    }
}
