package com.tokopedia.seller.shop.setting.data.source.cloud;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.open.view.model.CourierServiceId;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseCreateShop;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseSaveShopDesc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopSettingInfoDataSourceCloud {

    public static final long SUCCESS = 1;
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
    public static final String SHIPMENT_AGENCY = "shipment_agency";
    public static final String PACKAGE = "package";
    private static final int STEP_INFO_3 = 3;
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
                        if(responseSaveShopDescResponse.isSuccessful() && (responseSaveShopDescResponse.body().getData().getReserveStatus()==SUCCESS)){
                            return Observable.just(true);
                        }else{
                            return Observable.just(false);
                        }
                    }
                });
    }

    private Observable<Boolean> checkResponseError(Response<ResponseSaveShopDesc> responseSaveShopDescResponse){
            JSONObject json;
            try {
                json = new JSONObject(responseSaveShopDescResponse.body().toString());
                JSONArray errorMessage = json.optJSONArray("message_error");
                if (errorMessage != null) {
                    String errorListMessage = "";
                    for(int i = 0; i<errorMessage.length(); i++) {
                        if(errorMessage.get(i) instanceof String) {
                            errorListMessage = errorListMessage + errorMessage.getString(i);
                        }
                    }
                    if(!TextUtils.isEmpty(errorListMessage)){
                        return Observable.error(new ShopSettingException(errorListMessage));
                    }else{
                        return Observable.just(true);
                    }
                }else{
                    return Observable.just(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return Observable.just(true);
            }
    }

    public Observable<Boolean> saveShopSettingStep2(RequestParams requestParams){
        Map<String, String> values = generateRequestMapParams(requestParams.getString(LONGITUDE, null),
                requestParams.getString(LATITUDE, null),
                requestParams.getString(GEOLOCATION_CHECKSUM, null),
                requestParams.getString(LOC_COMPLETE, null),
                requestParams.getString(LOCATION, null),
                requestParams.getString(ADDR_STREET, null),
                requestParams.getString(POSTAL_CODE, null),
                requestParams.getString(DISTRICT_ID, null));

        return tomeApi.reserveShopDescInfo(values)
                .flatMap(new Func1<Response<ResponseSaveShopDesc>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseSaveShopDesc> responseSaveShopDescResponse) {
                        if(responseSaveShopDescResponse.isSuccessful() && (responseSaveShopDescResponse.body().getData().getReserveStatus()==SUCCESS)){
                            return Observable.just(true);
                        }else{
                            return Observable.just(false);
                        }
                    }
                });
    }

    public Observable<Boolean> saveShopSettingStep3(CourierServiceIdWrapper courierServiceIdWrapper) {
        Map<String, String> values = generateRequestMapParamsStep3(courierServiceIdWrapper);
        return tomeApi.reserveShopDescInfo(values)
                .flatMap(new Func1<Response<ResponseSaveShopDesc>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseSaveShopDesc> responseSaveShopDescResponse) {
                        if (responseSaveShopDescResponse.isSuccessful()) {
                            if (responseSaveShopDescResponse.body().getReserveStatus().equals(SUCCESS)) {
                                return Observable.just(true);
                            } else {
                                List<String> messageErrorList = responseSaveShopDescResponse.body().getMessageError();
                                if (messageErrorList != null && messageErrorList.size() > 0) {
                                    return Observable.error(new ErrorMessageException(messageErrorList.get(0)));
                                } else {
                                    return Observable.error(new HttpException(responseSaveShopDescResponse));
                                }
                            }
                        } else {
                            return Observable.error(new HttpException(responseSaveShopDescResponse));
                        }
                    }
                });
    }

    public Observable<Boolean> createShop() {
        return tomeApi.createShop()
                .flatMap(new Func1<Response<ResponseCreateShop>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseCreateShop> responseCreateShopResponse) {
                        if (responseCreateShopResponse.isSuccessful()) {
                            if (responseCreateShopResponse.body().getReserveStatus().equals(SUCCESS)) {
                                return Observable.just(true);
                            } else {
                                List<String> messageErrorList = responseCreateShopResponse.body().getMessageError();
                                if (messageErrorList != null && messageErrorList.size() > 0) {
                                    return Observable.error(new ErrorMessageException(messageErrorList.get(0)));
                                } else {
                                    return Observable.error(new HttpException(responseCreateShopResponse));
                                }
                            }
                        } else {
                            return Observable.error(new HttpException(responseCreateShopResponse));
                        }
                    }
                });
    }

    private final Map<String, String> generateRequestMapParams(String longitude,
                                                               String latitude,
                                                               String geolocation_checksum,
                                                               String loc_complete,
                                                               String location,
                                                               String addr_street,
                                                               String postal_code,
                                                               String district_id) {
        Map<String, String> params = new HashMap<>();

        if (!TextUtils.isEmpty(longitude))
            params.put(LONGITUDE, longitude);

        if (!TextUtils.isEmpty(latitude))
            params.put(LATITUDE, latitude);

        if (!TextUtils.isEmpty(geolocation_checksum))
            params.put(GEOLOCATION_CHECKSUM, geolocation_checksum);


        params.put(LOC_COMPLETE, loc_complete);
        params.put(LOCATION, location);

        if (!TextUtils.isEmpty(addr_street))
            params.put(ADDR_STREET, String.valueOf(addr_street));

        params.put(POSTAL_CODE, String.valueOf(postal_code));
        params.put(DISTRICT_ID, String.valueOf(district_id));
        params.put(STEP, Integer.toString(STEP_INFO_2));
        return params;
    }

    private final Map<String, String> generateRequestMapParamsStep3(CourierServiceIdWrapper courierServiceIdWrapperObj) {
        Map<String, String> params = new HashMap<>();
        List<String> courierIdList = courierServiceIdWrapperObj.getSelectedServiceIdList();
        String courierIdListStr = "";
        for (int i = 0, sizei = courierIdList.size(); i < sizei; i++) {
            if (i > 0) {
                courierIdListStr += ",";
            }
            courierIdListStr += courierIdList.get(i);
        }
        params.put(SHIPMENT_AGENCY, courierIdListStr);

        List<CourierServiceId> courierServiceIds = courierServiceIdWrapperObj.getCourierServiceIdList();
        try {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0, sizei = courierServiceIds.size(); i < sizei; i++) {
                CourierServiceId courierServiceId = courierServiceIds.get(i);
                String courierID = courierServiceId.getCourierID();
                List<String> courierServiceStrIdList = courierServiceId.getCourierServiceIdList();
                JSONArray jsonArray = new JSONArray();
                for (int j = 0, sizej = courierServiceStrIdList.size(); j < sizej; j++) {
                    jsonArray.put(courierServiceStrIdList.get(j));
                }
                jsonObject.put(courierID, jsonArray);
            }
            params.put(PACKAGE, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put(STEP, Integer.toString(STEP_INFO_3));
        return params;
    }
}
