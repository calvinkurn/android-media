package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.PriceEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateEntity;
import com.tokopedia.ride.common.ride.domain.model.TimePriceEstimate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 7/17/17.
 */

public class TimePriceEstimateEntityMapper {

    public TimePriceEstimateEntityMapper() {
    }

    public List<TimePriceEstimate> transform(List<TimesEstimateEntity> timesEstimateEntities, List<PriceEntity> priceEntities) {
        List<TimePriceEstimate> timePriceEstimates = new ArrayList<>();
        TimePriceEstimate timePriceEstimate = null;
        for (TimesEstimateEntity estimateEntity : timesEstimateEntities) {
            for (PriceEntity priceEntity : priceEntities) {
                if (estimateEntity.getProductId().equalsIgnoreCase(priceEntity.getProductId())) {
                    timePriceEstimate = transform(priceEntity, estimateEntity);
                    if (timePriceEstimate != null) {
                        timePriceEstimates.add(timePriceEstimate);
                    }
                }
            }
        }
        return timePriceEstimates;
    }

    private TimePriceEstimate transform(PriceEntity priceEntity, TimesEstimateEntity timesEstimateEntity) {
        TimePriceEstimate estimate = null;
        if (priceEntity != null && timesEstimateEntity != null) {
            estimate = new TimePriceEstimate();
            estimate.setEstimateTime(timesEstimateEntity.getEstimate());
            estimate.setEstimateFmt(priceEntity.getEstimate());
            estimate.setProductId(priceEntity.getProductId());
            estimate.setLowEstimate(priceEntity.getLowEstimate());
            estimate.setHighEstimate(priceEntity.getHighEstimate());
        }
        return estimate;
    }
}
