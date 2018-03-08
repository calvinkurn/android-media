package com.tokopedia.tkpdtrain.station.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.tokopedia.tkpdtrain.station.data.databasetable.TrainStationDb_Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by alvarisi on 3/7/18.
 */

public class TrainPopularStationSpecification implements DbFlowSpecification, DbFlowWithOrderSpecification, NetworkSpecification {

    public TrainPopularStationSpecification() {
    }

    @Override
    public ConditionGroup toCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainStationDb_Table.popularity_order.greaterThan(0));
        return conditions;
    }

    @Override
    public List<OrderBy> toOrder() {
        List<OrderBy> orderBies = new ArrayList<OrderBy>();
        orderBies.add(OrderBy.fromProperty(TrainStationDb_Table.popularity_order).ascending());
        return orderBies;
    }

    @Override
    public Map<String, Object> networkParam() {
        return new HashMap<>();
    }
}
