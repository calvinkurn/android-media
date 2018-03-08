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

public class TrainStationByKeywordSpecification implements DbFlowSpecification, NetworkSpecification {

    private String keyword;

    public TrainStationByKeywordSpecification(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public ConditionGroup toCondition() {
        String query = "%" + keyword + "%";
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.or(TrainStationDb_Table.station_code.like(query));
        conditions.or(TrainStationDb_Table.station_name.like(query));
        conditions.or(TrainStationDb_Table.station_display_name.like(query));
        conditions.or(TrainStationDb_Table.city_name.like(query));
        return conditions;
    }

    @Override
    public Map<String, Object> networkParam() {
        return new HashMap<>();
    }
}
