package com.tokopedia.seller.shop.setting.data.source.cloud;

import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseSaveShopDesc;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopSettingInfoDataSourceCloud {

    public static final String SUCCESS = "1";
    private final TomeApi tomeApi;

    @Inject
    public ShopSettingInfoDataSourceCloud(TomeApi tomeApi) {
        this.tomeApi = tomeApi;
    }

    public Observable<Boolean> saveShopSetting(HashMap<String, String> paramsRequest) {
        return tomeApi.reserveShopDescInfo(paramsRequest)
                .flatMap(new Func1<Response<ResponseSaveShopDesc>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseSaveShopDesc> responseSaveShopDescResponse) {
                        if(responseSaveShopDescResponse.isSuccessful() && responseSaveShopDescResponse.body().getReserveStatus().equals(SUCCESS)){
                            return Observable.just(true);
                        }else{
                            return Observable.just(false);
                        }
                    }
                });
    }
}
