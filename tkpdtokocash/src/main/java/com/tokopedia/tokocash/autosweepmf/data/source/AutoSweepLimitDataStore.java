package com.tokopedia.tokocash.autosweepmf.data.source;


import com.google.gson.JsonObject;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;

import rx.Observable;

public interface AutoSweepLimitDataStore {
    Observable<AutoSweepLimitDomain> autoSweepLimit(JsonObject data);
}
