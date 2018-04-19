package com.tokopedia.tokocash.autosweepmf.view.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepDetail;

import javax.inject.Inject;

public class AutoSweepDetailMapper {
    @Inject
    public AutoSweepDetailMapper() {
    }

    @Nullable
    public AutoSweepDetail transform(@NonNull com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetail detail) {
        AutoSweepDetail data = new AutoSweepDetail();
        data.setAccountStatus(detail.getAccountStatus());
        data.setAmountLimit(detail.getAmountLimit());
        data.setAutoSweepStatus(detail.getAutoSweepStatus());
        data.setBalance(detail.getBalance());
        data.setTitle(detail.getTitle());
        data.setContent(detail.getContent());
        data.setTooltipContent(detail.getTooltipContent());
        data.setMessage(detail.getMessage());
        data.setError(detail.getError());
        data.setCode(detail.getCode());
        data.setLatency(detail.getLatency());
        return data;
    }
}
