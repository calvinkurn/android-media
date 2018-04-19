package com.tokopedia.tokocash.autosweepmf.data.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit;

import javax.inject.Inject;

public class AutoSweepLimitMapper {
    @Inject
    public AutoSweepLimitMapper() {
    }

    @Nullable
    public AutoSweepLimit transform(@NonNull ResponseAutoSweepLimit data) {
        AutoSweepLimit sweepEnable = new AutoSweepLimit();

        if (data.getData() != null) {
            sweepEnable.setAmountLimit(data.getData().getAmountLimit());
            sweepEnable.setAutoSweep(data.getData().getAutoSweep());
            sweepEnable.setShowAutoSweep(data.getData().getShowAutoSweep());
            sweepEnable.setStatus(data.getData().isStatus());
        }

        sweepEnable.setMessage(data.getMessage());
        sweepEnable.setError(data.getError());
        sweepEnable.setCode(data.getCode());
        sweepEnable.setLatency(data.getLatency());
        return sweepEnable;
    }
}
