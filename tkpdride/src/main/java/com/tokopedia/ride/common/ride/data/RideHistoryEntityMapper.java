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

    public RideHistoryEntityMapper() {
        rideRequestEntityMapper = new RideRequestEntityMapper();
    }

    public List<RideHistory> transform(List<RideHistoryEntity> rideHistoryEntities) {
        List<RideHistory> rideHistories = new ArrayList<>();
        RideHistory rideHistory;
        for (RideHistoryEntity entity : rideHistoryEntities) {
            rideHistory = transform(entity);
            if (rideHistory != null) {
                rideHistories.add(rideHistory);
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
            rideHistory.setRequestTime(entity.getRequestTime());
        }
        return rideHistory;
    }

    private Payment transform(PaymentEntity paymentEntity) {
        Payment payment = null;
        if (paymentEntity != null) {
            payment = new Payment();
            payment.setCurrency(paymentEntity.getCurrencyCode());
            payment.setValue(paymentEntity.getAmount());
        }
        return payment;
    }


}
