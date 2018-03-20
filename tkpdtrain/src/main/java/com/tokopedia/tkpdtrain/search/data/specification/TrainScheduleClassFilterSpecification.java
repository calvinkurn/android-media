package com.tokopedia.tkpdtrain.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable_Table;

/**
 * @author Rizky on 14/03/18.
 */

public class TrainScheduleClassFilterSpecification implements DbFlowSpecification {

    private String trainClass;

    public TrainScheduleClassFilterSpecification(String trainClass) {
        this.trainClass = trainClass;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainScheduleDbTable_Table.train_class.eq(trainClass));
        return conditions;
    }

}
