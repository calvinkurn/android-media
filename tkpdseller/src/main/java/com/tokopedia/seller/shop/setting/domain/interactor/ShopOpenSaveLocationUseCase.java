package com.tokopedia.seller.shop.setting.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 12/22/17.
 */

public class ShopOpenSaveLocationUseCase extends UseCase<Boolean> {

    private static final String DISTRICT_ID = "district_id";
    private static final String POSTAL_CODE = "postal_code";
    private static final String ADDR_STREET = "addr_street";
    private static final String LOCATION = "location";
    private static final String LOC_COMPLETE = "loc_complete";
    private static final String GEOLOCATION_CHECKSUM = "geolocation_checksum";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private final ShopSettingSaveInfoRepository shopSettingSaveInfoRepository;

    @Inject
    public ShopOpenSaveLocationUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      ShopSettingSaveInfoRepository shopSettingSaveInfoRepository,
                                      UploadImageUseCase<UploadShopImageModel> uploadImageUseCase) {
        super(threadExecutor, postExecutionThread);
        this.shopSettingSaveInfoRepository = shopSettingSaveInfoRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return shopSettingSaveInfoRepository.saveShopSettingStep2(requestParams);
    }

    public static RequestParams createRequestParams(String longitude,
                                                    String latitude,
                                                    String geolocation_checksum,
                                                    String loc_complete,
                                                    String location,
                                                    String addr_street,
                                                    String postal_code,
                                                    String district_id) {
        RequestParams params = RequestParams.create();
        params.putString(LONGITUDE, longitude);
        params.putString(LATITUDE, latitude);
        params.putString(GEOLOCATION_CHECKSUM, geolocation_checksum);
        params.putString(LOC_COMPLETE, loc_complete);
        params.putString(LOCATION, location);
        params.putString(ADDR_STREET, String.valueOf(addr_street));
        params.putString(POSTAL_CODE, String.valueOf(postal_code));
        params.putString(DISTRICT_ID, String.valueOf(district_id));
        return params;
    }
}
