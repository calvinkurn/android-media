package com.tokopedia.seller.shop.setting.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
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
    public static final String LOGO = "logo";
    public static final String SERVER_ID = "server_id";
    public static final String PHOTO_OBJ = "photo_obj";
    public static final String SHORT_DESC = "short_desc";
    public static final String TAG_LINE = "tag_line";
    public static final String STEP = "step";
    public static final String DISTRICT_ID = "district_id";
    public static final String POSTAL_CODE = "postal_code";
    public static final String ADDR_STREET = "addr_street";
    public static final String LOCATION = "location";
    public static final String LOC_COMPLETE = "loc_complete";
    public static final String GEOLOCATION_CHECKSUM = "geolocation_checksum";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    private static final int STEP_INFO_2 = 2;
    private final TomeApi tomeApi;

    @Inject
    public ShopSettingInfoDataSourceCloud(TomeApi tomeApi) {
        this.tomeApi = tomeApi;
    }

    public Observable<Boolean> saveShopSetting(String logo, String serverId, String photoObj, String shopDescription, String tagLine, int stepInfo1) {
        return tomeApi.reserveShopDescInfo(generateRequestMapParams(logo, serverId, photoObj, shopDescription, tagLine, stepInfo1))
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

    private Map<String, String> generateRequestMapParams(String logo, String serverId, String photoObj, String shopDescription, String tagLine, int stepInfo1) {
        Map<String, String> params = new HashMap<>();
        params.put(LOGO, logo);
        params.put(SERVER_ID, serverId);
        params.put(PHOTO_OBJ, photoObj);
        params.put(SHORT_DESC, shopDescription);
        params.put(TAG_LINE, tagLine);
        params.put(STEP, String.valueOf(stepInfo1));
        return params;
    }

    public Observable<Boolean> saveShopSettingStep2(RequestParams requestParams){
        return tomeApi.reserveShopDescInfo(generateRequestMapParams(requestParams.getString(LONGITUDE, null),
                requestParams.getString(LATITUDE, null),
                requestParams.getString(GEOLOCATION_CHECKSUM, null),
                requestParams.getString(LOC_COMPLETE, null),
                requestParams.getString(LOCATION, null),
                requestParams.getString(ADDR_STREET, null),
                requestParams.getString(POSTAL_CODE, null),
                requestParams.getString(DISTRICT_ID, null)))
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

    public Map<String, String> generateRequestMapParams(String longitude,
                                               String latitude,
                                               String geolocation_checksum,
                                               String loc_complete,
                                               String location,
                                               String addr_street,
                                               String postal_code,
                                               String district_id){
        Map<String, String> params = new HashMap<>();
        params.put(LONGITUDE, longitude);
        params.put(LATITUDE, latitude);
        params.put(GEOLOCATION_CHECKSUM, geolocation_checksum);
        params.put(LOC_COMPLETE, loc_complete);
        params.put(LOCATION, location);
        params.put(ADDR_STREET, String.valueOf(addr_street));
        params.put(POSTAL_CODE, String.valueOf(postal_code));
        params.put(DISTRICT_ID, String.valueOf(district_id));
        params.put(STEP, Integer.toString(STEP_INFO_2));
        return params;
    }
}
