package com.tokopedia.seller.shop.open.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.shop.open.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.open.domain.ShopOpenSaveInfoRepository;

import javax.annotation.Nullable;
import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 12/22/17.
 *
 * https://phab.tokopedia.com/w/api/tome/#open-shop-api
 *
 * Reserve Shop Description And Details ( Step 2 )
 *
 * this link is important for references.
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

    private final ShopOpenSaveInfoRepository shopOpenSaveInfoRepository;

    @Inject
    public ShopOpenSaveLocationUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      ShopOpenSaveInfoRepository shopOpenSaveInfoRepository,
                                      UploadImageUseCase<UploadShopImageModel> uploadImageUseCase) {
        super(threadExecutor, postExecutionThread);
        this.shopOpenSaveInfoRepository = shopOpenSaveInfoRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return shopOpenSaveInfoRepository.saveShopSettingStep2(requestParams);
    }

    /**
     *
     * @param longitude optional if add pinpoint, longitude ex..83258720000003
     * @param latitude optional if add pinpoint, latitude ex. -6.2195686
     * @param geolocation_checksum optional if add pinpoint, checksum get from kero
     * @param loc_complete "A MUST"
     * @param location "A MUST"
     * @param addr_street 	optional if add pinpoint, ex. Jl.+Haji+R.+Rasuna+Said,+Kecamatan+Setiabudi,+Jaksel+12940
     * @param postal_code "A MUST"
     * @param district_id "A MUST"
     * @return RequestParam object
     */
    public static RequestParams createRequestParams(@Nullable String longitude,
                                                    @Nullable String latitude,
                                                    @Nullable String geolocation_checksum,
                                                    String loc_complete,
                                                    String location,
                                                    @Nullable String addr_street,
                                                    String postal_code,
                                                    String district_id) {
        RequestParams params = RequestParams.create();
        if(!TextUtils.isEmpty(longitude))
            params.putString(LONGITUDE, longitude);

        if(!TextUtils.isEmpty(latitude))
            params.putString(LATITUDE, latitude);

        if(!TextUtils.isEmpty(geolocation_checksum))
            params.putString(GEOLOCATION_CHECKSUM, geolocation_checksum);


        params.putString(LOC_COMPLETE, loc_complete);
        params.putString(LOCATION, location);

        if(!TextUtils.isEmpty(addr_street))
            params.putString(ADDR_STREET, String.valueOf(addr_street));

        params.putString(POSTAL_CODE, String.valueOf(postal_code));
        params.putString(DISTRICT_ID, String.valueOf(district_id));
        return params;
    }
}
