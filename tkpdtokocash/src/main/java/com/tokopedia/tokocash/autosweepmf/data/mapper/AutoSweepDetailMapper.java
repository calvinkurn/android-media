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
    public AutoSweepDetail transform(@NonNull ResponseAutoSweepDetail data) {
        AutoSweepDetail domain = new AutoSweepDetail();

        if (data.getData() != null) {
            domain.setAccountStatus(data.getData().getAccountStatus());
            domain.setAmountLimit(data.getData().getAmountLimit());
            domain.setAutoSweepStatus(data.getData().getAutoSweepStatus());
            domain.setBalance(data.getData().getBalance());
            domain.setDashboardLink(data.getData().getDashboardLink());
            domain.setMfInfoLink(data.getData().getMfInfoLink());

            if (data.getData().getShowAutoSweep() == 1) {
                domain.setEnable(true);
            } else {
                domain.setEnable(false);
            }

            if (data.getData().getText() != null) {
                domain.setTitle(data.getData().getText().getTitle());
                domain.setContent(data.getData().getText().getContent());
                domain.setTooltipContent(data.getData().getText().getTooltipContent());
                domain.setDescription(data.getData().getText().getDescription());

                if (data.getData().getText().getDialog() != null) {
                    domain.setDialogContent(data.getData().getText().getDialog().getContent());
                    domain.setDialogTitle(data.getData().getText().getDialog().getTitle());
                    domain.setDialogLabelPositive(data.getData().getText().getDialog().getPositiveLabel());
                    domain.setDialogLabelNegative(data.getData().getText().getDialog().getNegativeLabel());
                    domain.setDialogNegativeButtonLink(data.getData().getText().getDialog().getDialogNegativeLink());
                }
            }
        }

        domain.setMessage(data.getMessage());
        domain.setError(data.getError());
        domain.setCode(data.getCode());
        domain.setLatency(data.getLatency());
        return domain;
    }
}
