package com.tokopedia.tokocash.pendingcashback.data;

import com.tokopedia.tokocash.activation.data.entity.PendingCashbackEntity;
import com.tokopedia.tokocash.activation.presentation.model.PendingCashback;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class PendingCashbackMapper implements Func1<PendingCashbackEntity, PendingCashback> {

    @Inject
    public PendingCashbackMapper() {
    }

    @Override
    public PendingCashback call(PendingCashbackEntity pendingCashbackEntity) {
        if (pendingCashbackEntity != null) {
            PendingCashback pendingCashback = new PendingCashback();
            pendingCashback.setAmount(pendingCashbackEntity.getAmount());
            pendingCashback.setAmountText(pendingCashbackEntity.getAmountText());
            return pendingCashback;
        }
        throw new RuntimeException("Error");
    }
}
