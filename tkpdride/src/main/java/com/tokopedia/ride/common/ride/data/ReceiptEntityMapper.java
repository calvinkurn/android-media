package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.ReceiptEntity;
import com.tokopedia.ride.completetrip.domain.model.Receipt;

/**
 * Created by alvarisi on 3/31/17.
 */

public class ReceiptEntityMapper {
    public ReceiptEntityMapper() {
    }

    public Receipt transform(ReceiptEntity entity) {
        Receipt receipt = null;
        if (entity != null) {
            receipt = new Receipt();
            receipt.setCurrency(entity.getCurrencyCode());
            receipt.setDistance(entity.getDistance());
            receipt.setDistanceUnit(entity.getDistanceLabel());
            receipt.setDuration(entity.getDuration());
            receipt.setDuratuinInMinute(transformDurationToMinute(entity.getDuration()));
            receipt.setRequestId(entity.getRequestId());
            receipt.setSubtotal(entity.getSubtotal());
            receipt.setTotalCharged(entity.getTotalCharged());
            receipt.setTotalFare(entity.getTotalFare());
            receipt.setTotalOwe(entity.getTotalOwe());
        }
        return receipt;
    }

    private int transformDurationToMinute(String input) {
        String[] parts = input.split(":");
        if (parts.length == 3) {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);
            return hours * 60 + minutes + Math.abs(seconds / 60);
        } else if (parts.length == 2) {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours * 60 + minutes;
        } else {
            return 0;
        }
    }
}
