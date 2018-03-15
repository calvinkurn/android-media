package com.tokopedia.tkpdtrain.search.data;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;

/**
 * @author Rizky on 15/03/18.
 */

public class TrainScheduleDepartureTimeFilterSpecification implements DbFlowSpecification {

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        return conditions;
    }

}
