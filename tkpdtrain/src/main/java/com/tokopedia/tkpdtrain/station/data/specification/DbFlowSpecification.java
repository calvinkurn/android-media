package com.tokopedia.tkpdtrain.station.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.tkpdtrain.station.domain.model.FlightStation;

/**
 * @author  by alvarisi on 3/7/18.
 */

public interface DbFlowSpecification extends Specification {
    ConditionGroup toCondition();
}
