package com.tokopedia.shop.info.domain.interactor;

import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by normansyahputa on 2/13/18.
 */

public class GetSpeedReputationUseCase extends UseCase<SpeedReputation> {

    private Observable<SpeedReputation> speedReputationObservable;

    public GetSpeedReputationUseCase(Observable<SpeedReputation> speedReputationObservable) {
        this.speedReputationObservable = speedReputationObservable;
    }

    @Override
    public Observable<SpeedReputation> createObservable(RequestParams requestParams) {
        return speedReputationObservable;
    }
}
