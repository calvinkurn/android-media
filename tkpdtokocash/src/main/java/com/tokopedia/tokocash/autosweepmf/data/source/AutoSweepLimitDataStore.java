package com.tokopedia.tokocash.autosweepmf.data.source;


import com.google.gson.JsonObject;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit;

import rx.Observable;

public interface AutoSweepLimitDataStore {
    Observable<AutoSweepLimit> autoSweepLimit(JsonObject data);
}
