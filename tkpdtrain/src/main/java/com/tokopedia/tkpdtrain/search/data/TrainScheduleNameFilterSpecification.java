package com.tokopedia.tkpdtrain.search.data;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable_Table;

import java.util.List;

/**
 * Created by Rizky on 15/03/18.
 */

public class TrainScheduleNameFilterSpecification implements DbFlowSpecification {

    private List<String> trains;

    public TrainScheduleNameFilterSpecification(List<String> trains) {
        this.trains = trains;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        for (String trainName : trains) {
            conditions.or(TrainScheduleDbTable_Table.train_name.eq(trainName));
        }
        return conditions;
    }

}
