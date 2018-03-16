package com.tokopedia.tkpdtrain.search.data;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable_Table;

/**
 * @author Rizky on 14/03/18.
 */

public class TrainSchedulePriceFilterSpecification implements DbFlowSpecification {

    private long minPriceFilterValue;
    private long maxPriceFilterValue;

    public TrainSchedulePriceFilterSpecification(long minPriceFilterValue, long maxPriceFilterValue) {
        this.minPriceFilterValue = minPriceFilterValue;
        this.maxPriceFilterValue = maxPriceFilterValue;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainScheduleDbTable_Table.adult_fare.lessThanOrEq(maxPriceFilterValue))
                .and(TrainScheduleDbTable_Table.adult_fare.greaterThanOrEq(minPriceFilterValue));
        return conditions;
    }

}
