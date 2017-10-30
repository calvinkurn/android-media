package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.AddPaymentEntity;
import com.tokopedia.ride.common.ride.data.entity.PaymentMethodEntity;
import com.tokopedia.ride.common.ride.data.entity.PaymentMethodListEntity;
import com.tokopedia.ride.common.ride.data.entity.ScroogeWebviewPostDataBodyEntity;
import com.tokopedia.ride.common.ride.domain.model.AddPayment;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethod;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethodList;
import com.tokopedia.ride.common.ride.domain.model.ScroogeWebviewPostDataBody;

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
                paymentMethod.setLabel(paymentMethodEntity.getLabel());
                paymentMethod.setMaskedNumber(paymentMethodEntity.getMaskedNum());
                paymentMethodList.add(paymentMethod);
            }

        }

        return paymentMethodList;
    }

    private ScroogeWebviewPostDataBody transform(ScroogeWebviewPostDataBodyEntity entity) {
        ScroogeWebviewPostDataBody body = null;

        if (entity != null) {
            body = new ScroogeWebviewPostDataBody();
            body.setCallbackUrl(entity.getCallbackUrl());
            body.setCustomerEmail(entity.getCustomerEmail());
            body.setCustomerName(entity.getCustomerName());
            body.setDate(entity.getDate());
            body.setIpAddress(entity.getIpAddress());
            body.setMerchantCode(entity.getMerchantCode());
            body.setProfileCode(entity.getProfileCode());
            body.setSignature(entity.getSignature());
            body.setToken(entity.getToken());
            body.setUserId(entity.getUserId());
        }

        return body;
    }
}
