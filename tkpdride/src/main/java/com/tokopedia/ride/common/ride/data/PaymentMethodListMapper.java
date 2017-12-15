package com.tokopedia.ride.common.ride.data;

import android.os.Bundle;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.ride.common.ride.data.entity.AddPaymentEntity;
import com.tokopedia.ride.common.ride.data.entity.PaymentMethodEntity;
import com.tokopedia.ride.common.ride.data.entity.PaymentMethodListEntity;
import com.tokopedia.ride.common.ride.domain.model.AddPayment;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethod;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethodList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Vishal on 25th Oct, 2017.
 */

public class PaymentMethodListMapper {

    private final int FOUR = 4;

    public PaymentMethodListMapper() {
    }

    public PaymentMethodList transform(PaymentMethodListEntity entity) {
        PaymentMethodList paymentMethodList = null;
        if (entity != null) {
            paymentMethodList = new PaymentMethodList();
            paymentMethodList.setPaymentMethods(transform(entity.getPaymentMethods()));
            paymentMethodList.setAddPayment(transform(entity.getAddPayment()));
        }
        return paymentMethodList;
    }

    private AddPayment transform(AddPaymentEntity entity) {
        AddPayment addPayment = null;
        if (entity != null) {
            addPayment = new AddPayment();

            addPayment.setActive(entity.getActive());
            addPayment.setDeleteUrl(entity.getDeleteUrl());
            addPayment.setLabel(entity.getLabel());
            addPayment.setMethod(entity.getMethod());
            addPayment.setMode(entity.getMode());
            addPayment.setSaveBody(transform(entity.getSaveBody()));
            addPayment.setSaveUrl(entity.getSaveUrl());
        }

        return addPayment;
    }

    private List<PaymentMethod> transform(List<PaymentMethodEntity> entity) {
        List<PaymentMethod> paymentMethodList = null;

        if (entity != null) {
            paymentMethodList = new ArrayList<PaymentMethod>();

            for (PaymentMethodEntity paymentMethodEntity : entity) {
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setSaveUrl(paymentMethodEntity.getSaveUrl());
                paymentMethod.setSaveBody(transform(paymentMethodEntity.getSaveBody()));
                paymentMethod.setMode(paymentMethodEntity.getMode());
                paymentMethod.setMethod(paymentMethodEntity.getMethod());
                paymentMethod.setActive(paymentMethodEntity.getActive());
                paymentMethod.setBank(paymentMethodEntity.getBank());
                paymentMethod.setBankImage(paymentMethodEntity.getBankImage());
                paymentMethod.setCardType(paymentMethodEntity.getCardType());
                paymentMethod.setCardTypeImage(paymentMethodEntity.getCardTypeImage());
                paymentMethod.setDeleteUrl(paymentMethodEntity.getDeleteUrl());
                paymentMethod.setRemoveBody(transform(paymentMethodEntity.getRemoveBody()));
                paymentMethod.setExpiryMonth(paymentMethodEntity.getExpiryMonth());
                paymentMethod.setImage(paymentMethodEntity.getImage());
                paymentMethod.setExpiryYear(paymentMethodEntity.getExpiryYear());
                paymentMethod.setLabel(transform(paymentMethodEntity.getLabel(), paymentMethodEntity.getMode()));
                paymentMethod.setMaskedNumber(paymentMethodEntity.getMaskedNum());
                paymentMethod.setSaveWebView(paymentMethodEntity.isSaveWebView());
                paymentMethodList.add(paymentMethod);
            }

        }

        return paymentMethodList;
    }

    private String transform(String label, String mode) {
        if (!mode.equalsIgnoreCase("wallet")) {
            //pick last 4 digits only
            if (label != null && label.length() > FOUR) {
                label = "\u2022\u2022\u2022\u2022 " + label.substring(label.length() - FOUR);
            }
        }

        return label;
    }

    private Bundle transform(JsonObject entity) {
        Bundle bundle = new Bundle();

        if (entity != null) {
            Set<Map.Entry<String, JsonElement>> set = entity.entrySet();
            for (Map.Entry<String, JsonElement> entry : set) {
                bundle.putString(entry.getKey(), entry.getValue().getAsString());
            }
        }

        return bundle;
    }
}
