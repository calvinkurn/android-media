package com.tokopedia.tokocash.autosweepmf.view.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit;

import javax.inject.Inject;

public class AutoSweepLimitMapper {
    @Inject
    public AutoSweepLimitMapper() {
    }

    @Nullable
    public AutoSweepLimit transform(@NonNull AutoSweepLimitDomain data) {
        AutoSweepLimit sweepEnable = new AutoSweepLimit();
        sweepEnable.setAmountLimit(data.getAmountLimit());
        sweepEnable.setAutoSweep(data.getAutoSweep());
        sweepEnable.setShowAutoSweep(data.getShowAutoSweep());
        sweepEnable.setStatus(data.isStatus());
        sweepEnable.setMessage(data.getMessage());
        sweepEnable.setError(data.getError());
        sweepEnable.setCode(data.getCode());
        sweepEnable.setLatency(data.getLatency());
        return sweepEnable;
    }
}
