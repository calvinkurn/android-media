package com.tokopedia.tkpdtrain.station.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;

/**
 * @author  by alvarisi on 3/7/18.
 */

public interface DbFlowGroupBySpecification extends Specification {
    IProperty[] getProperty();
}
