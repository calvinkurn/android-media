package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.PaymentEntity;
import com.tokopedia.ride.common.ride.data.entity.RideHistoryEntity;
import com.tokopedia.ride.history.domain.model.Payment;
import com.tokopedia.ride.history.domain.model.RideHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 4/19/17.
 */

public class RideHistoryEntityMapper {
    private RideRequestEntityMapper rideRequestEntityMapper;
    private RatingEntityMapper ratingEntityMapper;

    public RideHistoryEntityMapper() {
        rideRequestEntityMapper = new RideRequestEntityMapper();
        ratingEntityMapper = new RatingEntityMapper();
    }

    public List<RideHistory> transform(List<RideHistoryEntity> rideHistoryEntities) {
        List<RideHistory> rideHistories = new ArrayList<>();

        if (rideHistoryEntities != null) {
            RideHistory rideHistory;
            for (RideHistoryEntity entity : rideHistoryEntities) {
                rideHistory = transform(entity);
                if (rideHistory != null) {
                    rideHistories.add(rideHistory);
                }
            }
        }

        return rideHistories;
    }

    public RideHistory transform(RideHistoryEntity entity) {
        RideHistory rideHistory = null;
        if (entity != null) {
            rideHistory = new RideHistory();
            rideHistory.setProductId(entity.getProductId());
            rideHistory.setRequestId(entity.getRequestId());
            rideHistory.setStatus(entity.getStatus() == null ? "" : entity.getStatus().toUpperCase());
            rideHistory.setShared(entity.isShared());
            rideHistory.setVehicle(rideRequestEntityMapper.transform(entity.getVehicleEntity()));
            rideHistory.setDriver(rideRequestEntityMapper.transform(entity.getDriverEntity()));
            rideHistory.setDestination(rideRequestEntityMapper.transform(entity.getDestination()));
            rideHistory.setPickup(rideRequestEntityMapper.transform(entity.getPickupEntity()));
            rideHistory.setPayment(transform(entity.getPaymentEntity()));
            rideHistory.setCashbackAmount(entity.getCashbackAmount());
            rideHistory.setDiscountAmount(entity.getDiscountAmount());
            rideHistory.setRequestTime(entity.getRequestTime());
            rideHistory.setRating(ratingEntityMapper.transform(entity.getRating()));
            rideHistory.setHelpUrl(entity.getHelpUrl());
        }
        return rideHistory;
    }

    private Payment transform(PaymentEntity paymentEntity) {
        Payment payment = null;
        if (paymentEntity != null) {
            payment = new Payment();
            payment.setCurrency(transformCurrency(paymentEntity.getCurrencyCode()));
            payment.setTotalAmount(paymentEntity.getTotalAmount());
            payment.setPaidAmount(paymentEntity.getPaidAmount());
            payment.setPendingAmount(paymentEntity.getPendingAmount());
            payment.setPaymentMethod(paymentEntity.getPaymentMethod());
        }
        return payment;
    }

    public String transformCurrency(String currencyCode) {
        if (currencyCode != null && currencyCode.equalsIgnoreCase("IDR")) {
            return "Rp";
        }

        return currencyCode;
    }


}
