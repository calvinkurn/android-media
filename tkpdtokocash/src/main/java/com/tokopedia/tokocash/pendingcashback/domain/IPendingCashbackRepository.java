package com.tokopedia.tokocash.pendingcashback.domain;

import com.tokopedia.tokocash.activation.presentation.model.PendingCashback;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/8/18.
 */

public interface IPendingCashbackRepository {

    Observable<PendingCashback> getPendingCashback(HashMap<String, String> mapParam);
}
