package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.TimesEstimateEntity;
import com.tokopedia.ride.common.ride.domain.model.TimesEstimate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 3/20/17.
 */

public class TimeEstimateEntityMapper {
    public TimeEstimateEntityMapper() {
    }

    public TimesEstimate transform(TimesEstimateEntity estimateEntity) {
        TimesEstimate estimate = null;
        if (estimateEntity != null) {
            estimate = new TimesEstimate();
            estimate.setDisplayName(estimateEntity.getDisplayName());
            estimate.setEstimate(estimateEntity.getEstimate());
            estimate.setLocalizedDisplayName(estimateEntity.getLocalizedDisplayName());
            estimate.setProductId(estimateEntity.getProductId());
        }
        return estimate;
    }

    public List<TimesEstimate> transform(List<TimesEstimateEntity> estimateEntities){
        List<TimesEstimate> timesEstimates = new ArrayList<>();
        TimesEstimate timesEstimate = null;
        for (TimesEstimateEntity entity : estimateEntities){
            timesEstimate = transform(entity);
            if (timesEstimate != null)
                timesEstimates.add(timesEstimate);
        }
        return timesEstimates;
    }
}
