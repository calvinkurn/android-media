package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.EstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.FareAttributeEntity;
import com.tokopedia.ride.common.ride.data.entity.FareEntity;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.TripEntity;
import com.tokopedia.ride.common.ride.domain.model.Estimate;
import com.tokopedia.ride.common.ride.domain.model.Fare;
import com.tokopedia.ride.common.ride.domain.model.FareAttribute;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.Trip;

/**
 * Created by alvarisi on 3/21/17.
 */

public class FareEstimateMapper {
    public FareEstimateMapper() {
    }

    public FareEstimate transform(FareEstimateEntity estimateEntity) {
        FareEstimate fareEstimate = null;
        if (estimateEntity != null) {
            fareEstimate = new FareEstimate();
            fareEstimate.setId(estimateEntity.getId());
            fareEstimate.setSuccess(estimateEntity.isSuccess());
            fareEstimate.setType(estimateEntity.getType());
            fareEstimate.setMessageSuccess(estimateEntity.getMessageSuccess());
            fareEstimate.setExtraAmount(estimateEntity.getExtraAmount());
            fareEstimate.setCode(estimateEntity.getCode());
            fareEstimate.setCashbackAmount(estimateEntity.getCashbackAmount());
            fareEstimate.setCashbackTopCashAmount(estimateEntity.getCashbackTopCashAmount());
            fareEstimate.setCashbackVoucherAmount(estimateEntity.getCashbackVoucherAmount());
            fareEstimate.setDiscountAmount(estimateEntity.getDiscountAmount());
            fareEstimate.setAttributes(transformFareAttrEntity(estimateEntity.getAttributes()));
            fareEstimate.setFare(transformFareEntity(estimateEntity.getFare()));
            fareEstimate.setTrip(transformTripEntity(estimateEntity.getTrip()));
            fareEstimate.setEstimate(transformEstimateEntity(estimateEntity.getEstimate()));
            fareEstimate.setPickupEstimate(estimateEntity.getPickupEstimate());
        }
        return fareEstimate;
    }

    private FareAttribute transformFareAttrEntity(FareAttributeEntity entity) {
        FareAttribute fareAttribute = null;
        if (entity != null) {
            fareAttribute = new FareAttribute();
            fareAttribute.setCode(entity.getCode());
            fareAttribute.setDetail(entity.getDetail());
            fareAttribute.setDetailEn(entity.getDetailEn());
            fareAttribute.setDetailId(entity.getDetailId());
            fareAttribute.setMessage(entity.getMessage());
            fareAttribute.setStatus(entity.getStatus());
            fareAttribute.setTitle(entity.getTitle());
        }
        return fareAttribute;
    }

    private Trip transformTripEntity(TripEntity entity) {
        Trip trip = null;
        if (entity != null) {
            trip = new Trip();
            trip.setDistanceEstimate(entity.getDistanceEstimate());
            trip.setDistanceUnit(entity.getDistanceUnit());
            trip.setDurationEstimate(entity.getDurationEstimate());
        }
        return trip;
    }

    private Fare transformFareEntity(FareEntity entity) {
        Fare fare1 = null;
        if (entity != null) {
            fare1 = new Fare();
            fare1.setCurrencyCode(transformCurrency(entity.getCurrencyCode()));
            fare1.setDisplay(formatDisplayPrice(entity.getDisplay()));
            fare1.setExpiresAt(entity.getExpiresAt());
            fare1.setFareId(entity.getFareId());
            fare1.setValue(entity.getValue());
        }
        return fare1;
    }

    private Estimate transformEstimateEntity(EstimateEntity entity) {
        Estimate estimate = null;
        if (entity != null) {
            estimate = new Estimate();
            estimate.setCurrencyCode(transformCurrency(entity.getCurrencyCode()));
            estimate.setDisplay(entity.getDisplay());
            estimate.setHighEstimate(entity.getHighEstimate());
            estimate.setLowEstimate(entity.getLowEstimate());
            estimate.setSurgeConfirmationHref(entity.getSurgeConfirmationHref());
            estimate.setSurgeConfirmationId(entity.getSurgeConfirmationId());
            estimate.setSurgeMultiplier(entity.getSurgeMultiplier().floatValue());
        }
        return estimate;
    }

    public String transformCurrency(String currencyCode) {
        if (currencyCode != null && currencyCode.equalsIgnoreCase("IDR")) {
            return "Rp";
        }

        return currencyCode;
    }

    /**
     * This function added a space after IDR currency if not provided
     *
     * @param price
     */
    private String formatDisplayPrice(String price) {
        //format display to add space after currency
        if (price != null && price.contains("IDR") && !price.contains("IDR ")) {
            price = price.replace("IDR", "Rp ").replace(",", ".");
        }

        if (price != null && price.contains("Rp") && !price.contains("Rp ")) {
            price = price.replace("Rp", "Rp ").replace(",", ".");
        }

        return price;
    }
}
