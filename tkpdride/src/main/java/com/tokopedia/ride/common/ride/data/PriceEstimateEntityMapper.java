package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.PriceEntity;
import com.tokopedia.ride.common.ride.domain.model.PriceEstimate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 7/17/17.
 */

public class PriceEstimateEntityMapper {
    public PriceEstimate transform(PriceEntity entity) {
        PriceEstimate estimate = null;
        if (entity != null) {
            estimate = new PriceEstimate();
            estimate.setProductId(entity.getProductId());
            estimate.setDuration(entity.getDuration());
            estimate.setEstimate(entity.getEstimate());
            estimate.setCurrencyCode(entity.getCurrencyCode());
            estimate.setHighEstimate(entity.getHighEstimate());
            estimate.setLowEstimate(entity.getLowEstimate());
            estimate.setDistance(entity.getDistance());
        }
        return estimate;
    }

    public List<PriceEstimate> transform(List<PriceEntity> entities) {
        List<PriceEstimate> priceEstimates = new ArrayList<>();
        PriceEstimate estimate = null;
        for (PriceEntity entity : entities) {
            estimate = transform(entity);
            if (estimate != null)
                priceEstimates.add(estimate);
        }
        return priceEstimates;
    }
}
