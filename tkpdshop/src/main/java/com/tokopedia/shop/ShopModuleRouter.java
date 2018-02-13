package com.tokopedia.shop;

import com.tokopedia.reputation.speed.SpeedReputation;

import rx.Observable;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopModuleRouter {
    Observable<SpeedReputation> getSpeedReputationUseCase();
}
