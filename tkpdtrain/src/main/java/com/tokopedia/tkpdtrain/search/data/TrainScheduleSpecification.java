package com.tokopedia.tkpdtrain.search.data;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.tkpdtrain.common.specification.CloudNetworkSpecification;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;

import java.util.Map;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainScheduleSpecification implements DbFlowSpecification, CloudNetworkSpecification {

    private Map<String, Object> mapParam;

    public TrainScheduleSpecification(Map<String, Object> mapParam) {
        this.mapParam = mapParam;
    }

    @Override
    public Map<String, Object> networkParam() {
        return mapParam;
    }

    @Override
    public ConditionGroup getCondition() {
        return null;
    }
}
