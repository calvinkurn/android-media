package com.tokopedia.tkpdtrain.search.data;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable_Table;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainDetailScheduleSpecification implements DbFlowSpecification {

    private String idSchedule;

    public TrainDetailScheduleSpecification(String idSchedule) {
        this.idSchedule = idSchedule;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainScheduleDbTable_Table.schedule_id.eq(idSchedule));
        return conditions;
    }
}
