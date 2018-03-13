package com.tokopedia.tkpdtrain.common.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;

/**
 * @author  by alvarisi on 3/7/18.
 */

public interface DbFlowSpecification extends Specification {
    ConditionGroup getCondition();
}
