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
    public static final String LOGO = "logo";
    public static final String SERVER_ID = "server_id";
    public static final String PHOTO_OBJ = "photo_obj";
    public static final String SHORT_DESC = "short_desc";
    public static final String TAG_LINE = "tag_line";
    public static final String STEP = "step";
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
                        if(responseSaveShopDescResponse.isSuccessful() && responseSaveShopDescResponse.body().getMessageStatus().equals(SUCCESS)){
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
}
