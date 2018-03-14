package com.tokopedia.tkpdtrain.station.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.tokopedia.tkpdtrain.common.specification.DbFlowGroupBySpecification;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.station.data.databasetable.TrainStationDb_Table;

/**
 * @author by alvarisi on 3/12/18.
 */

public class TrainStationCityByKeywordSpecification implements DbFlowSpecification, DbFlowGroupBySpecification {

    private String keyword;

    public TrainStationCityByKeywordSpecification(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public ConditionGroup getCondition() {
        String query = "%" + keyword + "%";
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.or(TrainStationDb_Table.city_name.like(query));
        return conditions;
    }

    @Override
    public IProperty[] getProperty() {
        IProperty[] properties = new IProperty[1];
        properties[0] = TrainStationDb_Table.city_name;
        return properties;
    }
}
