package com.tokopedia.seller.shop.setting.domain;


import com.tokopedia.seller.shop.setting.data.model.GenerateHostModel;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public interface ShopSettingSaveInfoRepository {
    Observable<Boolean> saveShopSetting(String imageUrl, String shopDescription, String tagLine);

    Observable<GenerateHostModel.GenerateHost> generateHost();

    Observable<UploadShopImageModel.Data> uploadImage(GenerateHostModel.GenerateHost generateHost, String pathFileImage);
}
