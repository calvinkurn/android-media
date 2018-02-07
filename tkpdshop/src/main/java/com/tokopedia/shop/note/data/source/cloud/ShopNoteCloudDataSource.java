package com.tokopedia.shop.note.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.common.data.source.cloud.api.WS4ShopApi;
import com.tokopedia.shop.note.data.source.cloud.model.ResponseList;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopNoteCloudDataSource {

    public static final String SHOP_ID = "shop_id";
    public static final String SHOW_ALL = "show_all";

    private WS4ShopApi shopApi;
    private UserSession userSession;

    @Inject
    public ShopNoteCloudDataSource(WS4ShopApi shopApi, UserSession userSession) {
        this.shopApi = shopApi;
        this.userSession = userSession;
    }

    public Observable<Response<DataResponse<ResponseList<ShopNote>>>> getShopNoteList(String shopId) {
        Map<String, String> params = new HashMap<>();
        params.put(SHOP_ID, shopId);
        params.put(SHOW_ALL, "1");
        params = AuthUtil.generateParams(userSession.getUserId(), userSession.getDeviceId(), params);
        return shopApi.getShopNotes(params);
    }
}
