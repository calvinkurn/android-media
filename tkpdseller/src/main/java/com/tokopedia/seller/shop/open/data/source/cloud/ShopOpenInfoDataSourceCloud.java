package com.tokopedia.seller.shop.open.data.source.cloud;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.common.data.mapper.DataResponseMapper;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.seller.shop.open.data.model.response.DataResponse;
import com.tokopedia.seller.shop.open.data.model.response.ResponseOpenShopPicture;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseSaveShopDesc;
import com.tokopedia.seller.shop.open.data.source.cloud.api.OpenShopApi;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.open.view.model.CourierServiceId;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCreateShop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopOpenInfoDataSourceCloud {

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
    public static final String SHOP_LOGO = "shop_logo";
    public static final String HTTPS = "https://";

    private final TomeApi tomeApi;
    private final Retrofit.Builder retrofitBuilder;
    private final  OkHttpClient okHttpClient;

    @Inject
    public ShopOpenInfoDataSourceCloud(@ShopQualifier TomeApi tomeApi, Retrofit.Builder retrofitBuilder, @DefaultAuthWithErrorHandler OkHttpClient okHttpClient) {
        this.tomeApi = tomeApi;
        this.retrofitBuilder = retrofitBuilder;
        this.okHttpClient = okHttpClient;
    }

    public Observable<Boolean> saveShopSetting(HashMap<String, String> paramsRequest) {
        return tomeApi.reserveShopDescInfo(paramsRequest)
                .map(new DataResponseMapper<ResponseSaveShopDesc>())
                .flatMap(new Func1<ResponseSaveShopDesc, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(ResponseSaveShopDesc responseSaveShopDesc) {
                        if (responseSaveShopDesc == null) {
                            throw new RuntimeException();
                        } else {
                            return Observable.just(responseSaveShopDesc.getReserveStatus() == SUCCESS);
                        }
                    }
                });
    }

    public Observable<Boolean> saveShopSettingStep2(RequestParams requestParams) {
        Map<String, String> values = generateRequestMapParams(requestParams.getString(LONGITUDE, null),
                requestParams.getString(LATITUDE, null),
                requestParams.getString(GEOLOCATION_CHECKSUM, null),
                requestParams.getString(LOC_COMPLETE, null),
                requestParams.getString(LOCATION, null),
                requestParams.getString(ADDR_STREET, null),
                requestParams.getString(POSTAL_CODE, null),
                requestParams.getString(DISTRICT_ID, null));

        return tomeApi.reserveShopDescInfo(values)
                .map(new DataResponseMapper<ResponseSaveShopDesc>())
                .flatMap(new Func1<ResponseSaveShopDesc, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(ResponseSaveShopDesc responseSaveShopDesc) {
                        if (responseSaveShopDesc == null) {
                            throw new RuntimeException();
                        } else {
                            return Observable.just(responseSaveShopDesc.getReserveStatus() == SUCCESS);
                        }
                    }
                });
    }

    public Observable<Boolean> saveShopSettingStep3(CourierServiceIdWrapper courierServiceIdWrapper) {
        Map<String, String> values = generateRequestMapParamsStep3(courierServiceIdWrapper);
        return tomeApi.reserveShopDescInfo(values)
                .flatMap(new Func1<Response<DataResponse<ResponseSaveShopDesc>>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<DataResponse<ResponseSaveShopDesc>> dataResponseResponse) {
                        if (dataResponseResponse == null) {
                            throw new RuntimeException();
                        } else {
                            return Observable.just(dataResponseResponse.body().getData().getReserveStatus() == SUCCESS);
                        }
                    }
                });
    }

    public Observable<ResponseCreateShop> createShop() {
        return tomeApi.createShop()
                .map(new DataResponseMapper<ResponseCreateShop>())
                .flatMap(new Func1<ResponseCreateShop, Observable<ResponseCreateShop>>() {
                    @Override
                    public Observable<ResponseCreateShop> call(ResponseCreateShop responseCreateShop) {
                        if (responseCreateShop == null) {
                            throw new RuntimeException();
                        } else {
                            return Observable.just(responseCreateShop);
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
                    jsonArray.put(Integer.parseInt(courierServiceStrIdList.get(j)));
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

    public Observable<String> openShopPicture(String picSrc, String serverId, String url) {
        Retrofit retrofit = retrofitBuilder.client(okHttpClient).build();
        String urlFull = generateUrlOpenShopPicture(url);
        Map<String, String> params = new HashMap<>();
        params.put(SHOP_LOGO, picSrc);
        params.put(SERVER_ID, serverId);
        return retrofit.create(OpenShopApi.class)
                .openShopPicture(urlFull, params)
                .flatMap(new Func1<Response<ResponseOpenShopPicture>, Observable<String>>() {
                    @Override
                    public Observable<String> call(Response<ResponseOpenShopPicture> response) {
                        return Observable.just(response.body().getData().getFile_uploaded());
                    }
                });
    }

    private String generateUrlOpenShopPicture(String urlHost) {
        return HTTPS + urlHost + TkpdBaseURL.Upload.PATH_UPLOAD_IMAGE_HELPER + TkpdBaseURL.Upload.PATH_OPEN_SHOP_PICTURE;
    }
}
