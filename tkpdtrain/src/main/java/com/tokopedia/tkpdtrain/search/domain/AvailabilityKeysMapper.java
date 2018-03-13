package com.tokopedia.tkpdtrain.search.domain;

import com.tokopedia.tkpdtrain.search.data.entity.AvailabilityKeysEntity;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class AvailabilityKeysMapper implements Func1<List<AvailabilityKeysEntity>, List<AvailabilityKeySchedule>> {

    public AvailabilityKeysMapper() {
    }

    @Override
    public List<AvailabilityKeySchedule> call(List<AvailabilityKeysEntity> availabilityKeysEntities) {
        List<AvailabilityKeySchedule> availabilityKeySchedules = new ArrayList<>();
        if (availabilityKeysEntities != null) {
            for (AvailabilityKeysEntity availabilityKeysEntity : availabilityKeysEntities) {
                AvailabilityKeySchedule availabilityKeySchedule = new AvailabilityKeySchedule();
                availabilityKeySchedule.setIdTrain(availabilityKeysEntity.getIdTrain());
                availabilityKeySchedules.add(availabilityKeySchedule);
            }
        }
        return availabilityKeySchedules;
    }
}
