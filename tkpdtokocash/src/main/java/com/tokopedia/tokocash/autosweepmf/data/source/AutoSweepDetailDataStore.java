package com.tokopedia.tokocash.autosweepmf.data.source;

import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetail;

import rx.Observable;

public interface AutoSweepDetailDataStore {
    Observable<AutoSweepDetail> autoSweepDetail();
}
