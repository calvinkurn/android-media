package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.AddPaymentEntity;
import com.tokopedia.ride.common.ride.data.entity.PaymentMethodEntity;
import com.tokopedia.ride.common.ride.data.entity.PaymentMethodListEntity;
import com.tokopedia.ride.common.ride.data.entity.PriceDetailEntity;
import com.tokopedia.ride.common.ride.domain.model.AddPayment;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethod;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethodList;
import com.tokopedia.ride.common.ride.domain.model.PriceDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal on 25th Oct, 2017.
 */

public class PaymentMethodListMapper {

    public PaymentMethodListMapper() {
    }

    public PaymentMethodList transform(PaymentMethodListEntity entity) {
        PaymentMethodList paymentMethodList = null;
        if (entity != null) {
            paymentMethodList.setPaymentMethods(transform(entity.getPaymentMethods()));
            paymentMethodList.setAddPayment(transform(entity.getAddPayment()));
        }
        return paymentMethodList;
    }

    private AddPayment transform(AddPaymentEntity entity) {
        AddPayment addPayment = null;
        if (entity == null) {
            addPayment = new AddPayment();

            addPayment.setActive(entity.getActive());
            addPayment.setDeleteUrl(entity.getDeleteUrl());
            addPayment.setLabel(entity.getLabel());
            addPayment.setMethod(entity.getMethod());
            addPayment.setMode(entity.getMode());
            addPayment.setSaveBody(entity.getSaveBody());
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
                paymentMethod.setSaveBody(paymentMethodEntity.getSaveBody());
                paymentMethod.setMode(paymentMethodEntity.getMode());
                paymentMethod.setMethod(paymentMethodEntity.getMethod());
                paymentMethod.setActive(paymentMethodEntity.getActive());
                paymentMethod.setBank(paymentMethodEntity.getBank());
                paymentMethod.setBankImage(paymentMethodEntity.getBankImage());
                paymentMethod.setCardType(paymentMethodEntity.getCardType());
                paymentMethod.setCardTypeImage(paymentMethodEntity.getCardTypeImage());
                paymentMethod.setDeleteUrl(paymentMethodEntity.getDeleteUrl());
                paymentMethod.setExpiryMonth(paymentMethodEntity.getExpiryMonth());
                paymentMethod.setImage(paymentMethodEntity.getImage());
                paymentMethod.setExpiryYear(paymentMethodEntity.getExpiryYear());

                paymentMethodList.add(paymentMethod);
            }

        }

        return paymentMethodList;
    }
}
