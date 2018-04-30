package com.tokopedia.tokocash.autosweepmf.data.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;

import javax.inject.Inject;

public class AutoSweepLimitMapperEntity {
    @Inject
    public AutoSweepLimitMapperEntity() {
    }

    @Nullable
    public AutoSweepLimitDomain transform(@NonNull ResponseAutoSweepLimit entity) {
        AutoSweepLimitDomain sweepEnable = new AutoSweepLimitDomain();

        if (entity.getData() != null) {
            sweepEnable.setAmountLimit(entity.getData().getAmountLimit());
            sweepEnable.setAutoSweep(entity.getData().getAutoSweep());
            sweepEnable.setShowAutoSweep(entity.getData().getShowAutoSweep());
            sweepEnable.setStatus(entity.getData().isStatus());
        }

        sweepEnable.setMessage(entity.getMessage());
        sweepEnable.setError(entity.getError());
        sweepEnable.setCode(entity.getCode());
        sweepEnable.setLatency(entity.getLatency());
        return sweepEnable;
    }
}
