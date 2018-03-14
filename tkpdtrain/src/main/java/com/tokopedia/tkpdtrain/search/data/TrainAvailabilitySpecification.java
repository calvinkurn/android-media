package com.tokopedia.tkpdtrain.search.data;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.search.data.entity.ScheduleAvailabilityEntity;

import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable_Table;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainAvailabilitySpecification implements DbFlowSpecification {

    private List<ScheduleAvailabilityEntity> scheduleAvailabilityEntities;

    public TrainAvailabilitySpecification(List<ScheduleAvailabilityEntity> scheduleAvailabilityEntities) {
        this.scheduleAvailabilityEntities = scheduleAvailabilityEntities;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        for (ScheduleAvailabilityEntity scheduleAvailabilityEntity : scheduleAvailabilityEntities) {
            conditionGroup.or(TrainScheduleDbTable_Table.schedule_id.eq(scheduleAvailabilityEntity.getIdSchedule()));
        }
        return conditionGroup;
    }
}
