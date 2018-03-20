package com.tokopedia.tkpdtrain.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.tokopedia.tkpdtrain.common.specification.DbFlowWithOrderSpecification;
import com.tokopedia.tkpdtrain.search.constant.TrainSortOption;
import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable_Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rizky on 15/03/18.
 */

public class TrainScheduleSortSpecification implements DbFlowWithOrderSpecification {

    private int sortOptionId;

    public TrainScheduleSortSpecification(int sortOptionId) {
        this.sortOptionId = sortOptionId;
    }

    @Override
    public List<OrderBy> toOrder() {
        List<OrderBy> orderBies = new ArrayList<OrderBy>();
        switch (sortOptionId) {
            case TrainSortOption.EARLIEST_DEPARTURE:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.departure_timestamp).ascending());
                break;
            case TrainSortOption.LATEST_DEPARTURE:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.departure_timestamp).descending());
                break;
            case TrainSortOption.SHORTEST_DURATION:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.duration).ascending());
                break;
            case TrainSortOption.LONGEST_DURATION:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.duration).descending());
                break;
            case TrainSortOption.EARLIEST_ARRIVAL:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.arrival_timestamp).ascending());
                break;
            case TrainSortOption.LATEST_ARRIVAL:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.arrival_timestamp).descending());
                break;
            case TrainSortOption.CHEAPEST:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.adult_fare).ascending());
                break;
            case TrainSortOption.MOST_EXPENSIVE:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.adult_fare).descending());
                break;
        }
        return orderBies;
    }

}
