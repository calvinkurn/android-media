package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.PendingPaymentEntity;
import com.tokopedia.ride.common.ride.data.entity.TopUpOptionEntity;
import com.tokopedia.ride.common.ride.domain.model.PendingPayment;
import com.tokopedia.ride.completetrip.view.viewmodel.TokoCashProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 7/10/17.
 */

public class PendingPaymentEntityMapper {
    public PendingPayment transform(PendingPaymentEntity entity) {
        PendingPayment pendingPayment = null;
        if (entity != null
                && entity.getPendingAmount() != null
                && entity.getPendingAmount().length() != 0) {
            pendingPayment = new PendingPayment();
            pendingPayment.setBalance(entity.getBalance());
            pendingPayment.setCurrencyCode(transformCurrency(entity.getCurrencyCode()));
            pendingPayment.setPendingAmount(transformPendingAmount(entity.getPendingAmount(), entity.getCurrencyCode()));
            pendingPayment.setTopUpOptions(transformTopupOptions(entity.getTopUpOptions()));
            pendingPayment.setTopupUrl(entity.getTopupUrl());
            pendingPayment.setOperatorId(entity.getOperatorId());
            pendingPayment.setCategoryId(entity.getCategoryId());
            pendingPayment.setShowTopupOptions(entity.isShowTopupOptions());
        }
        return pendingPayment;
    }

    private List<TokoCashProduct> transformTopupOptions(List<TopUpOptionEntity> topUpOptions) {
        List<TokoCashProduct> options = new ArrayList<>();

        if (topUpOptions != null) {
            for (TopUpOptionEntity entity : topUpOptions) {
                TokoCashProduct option = new TokoCashProduct();
                option.setTitle(entity.getTitle());
                option.setId(entity.getId());
                option.setValue(String.valueOf(entity.getValue()));
                options.add(option);
            }
        }

        return options;
    }

    public String transformCurrency(String currencyCode) {
        if (currencyCode != null && currencyCode.equalsIgnoreCase("IDR")) {
            return "Rp";
        }

        return currencyCode;
    }

    public String transformPendingAmount(String amount, String currencyCode) {
        if (amount == null || amount.length() == 0) {
            amount = "0";
        }

        if (currencyCode != null && currencyCode.equalsIgnoreCase("IDR") || currencyCode.equalsIgnoreCase("Rp")) {
            currencyCode = "Rp";
            amount = amount.replace(",", ".");
        }

        return String.format("%s %s", currencyCode, amount);
    }
}
