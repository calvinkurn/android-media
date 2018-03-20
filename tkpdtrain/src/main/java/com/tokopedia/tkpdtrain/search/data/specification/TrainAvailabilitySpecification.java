package com.tokopedia.tkpdtrain.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.search.data.typedef.ScheduleTypeDef;
import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable_Table;
import com.tokopedia.tkpdtrain.search.data.entity.ScheduleAvailabilityEntity;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainAvailabilitySpecification implements DbFlowSpecification {

    private List<ScheduleAvailabilityEntity> scheduleAvailabilityEntities;
    private int scheduleVariant;

    public TrainAvailabilitySpecification(List<ScheduleAvailabilityEntity> scheduleAvailabilityEntities,
                                          int scheduleVariant) {
        this.scheduleAvailabilityEntities = scheduleAvailabilityEntities;
        this.scheduleVariant = scheduleVariant;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        for (ScheduleAvailabilityEntity scheduleAvailabilityEntity : scheduleAvailabilityEntities) {
            conditionGroup.or(TrainScheduleDbTable_Table.schedule_id.eq(scheduleAvailabilityEntity.getIdSchedule()));
            if (isReturnSchedule()) {
                conditionGroup.or(TrainScheduleDbTable_Table.is_return_schedule.eq(true));
            }

        }
        return conditionGroup;
    }

    private boolean isReturnSchedule() {
        return scheduleVariant == ScheduleTypeDef.RETURN_SCHEDULE;
    }

}
