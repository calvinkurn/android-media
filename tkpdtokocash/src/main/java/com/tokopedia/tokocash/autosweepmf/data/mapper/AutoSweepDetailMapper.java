package com.tokopedia.tokocash.autosweepmf.data.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetail;

import javax.inject.Inject;

public class AutoSweepDetailMapper {
    @Inject
    public AutoSweepDetailMapper() {
    }

    @Nullable
    public AutoSweepDetail transform(@NonNull ResponseAutoSweepDetail detail) {
        AutoSweepDetail data = new AutoSweepDetail();

        if (detail.getData() != null) {
            data.setAccountStatus(detail.getData().getAccountStatus());
            data.setAmountLimit(detail.getData().getAmountLimit());
            data.setAutoSweepStatus(detail.getData().getAutoSweepStatus());
            data.setBalance(detail.getData().getBalance());
            if (detail.getData().getText() != null) {
                data.setTitle(detail.getData().getText().getTitle());
                data.setContent(detail.getData().getText().getContent());
                data.setTooltipContent(detail.getData().getText().getTooltipContent());
            }

        }

        data.setMessage(detail.getMessage());
        data.setError(detail.getError());
        data.setCode(detail.getCode());
        data.setLatency(detail.getLatency());
        return data;
    }
}
