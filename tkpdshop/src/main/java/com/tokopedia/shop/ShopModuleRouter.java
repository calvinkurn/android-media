package com.tokopedia.shop;

import android.support.v4.app.Fragment;

import com.tokopedia.reputation.speed.SpeedReputation;

import rx.Observable;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopModuleRouter {
    Observable<SpeedReputation> getSpeedReputationUseCase();

    Fragment getShopReputationFragmentShop(String shopId, String shopDomain);

    Fragment getShopTalkFragment();
}
