package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.PendingPaymentEntity;
import com.tokopedia.ride.completetrip.domain.model.PendingPayment;

/**
 * Created by alvarisi on 7/10/17.
 */

public class PendingPaymentEntityMapper {
    public PendingPayment transform(PendingPaymentEntity entity) {
        PendingPayment pendingPayment = null;
        if (entity != null) {
            pendingPayment = new PendingPayment();
            pendingPayment.setBalance(entity.getBalance());
            pendingPayment.setCurrencyCode(entity.getCurrencyCode());
            pendingPayment.setPendingAmount(entity.getPendingAmount());
            pendingPayment.setTopUpOptions(entity.getTopUpOptions());
            pendingPayment.setTopupUrl(entity.getTopupUrl());
            pendingPayment.setOperatorId(entity.getOperatorId());
            pendingPayment.setCategoryId(entity.getCategoryId());
        }
        return pendingPayment;
    }
}
