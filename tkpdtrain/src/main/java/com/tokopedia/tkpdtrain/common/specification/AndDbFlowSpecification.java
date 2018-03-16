package com.tokopedia.tkpdtrain.common.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.OrderBy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rizky on 14/03/18.
 */

public class AndDbFlowSpecification implements DbFlowSpecification, DbFlowWithOrderSpecification {

    private Specification first;
    private Specification second;

    public AndDbFlowSpecification(Specification first, Specification second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        if (first instanceof DbFlowSpecification) {
            DbFlowSpecification firstDbFlowSpec = (DbFlowSpecification) first;
            if (firstDbFlowSpec.getCondition().size() != 0) {
                conditions.and(firstDbFlowSpec.getCondition());
            }
        }
        if (second instanceof DbFlowSpecification) {
            DbFlowSpecification secondDbFlowSpecification = (DbFlowSpecification) second;
            if (secondDbFlowSpecification.getCondition().size() != 0) {
                conditions.and(secondDbFlowSpecification.getCondition());
            }
        }
        return conditions;
    }

    @Override
    public List<OrderBy> toOrder() {
        List<OrderBy> orderBies = new ArrayList<OrderBy>();
        if (first instanceof DbFlowWithOrderSpecification) {
            DbFlowWithOrderSpecification firstDbFlowWithOrderSpec = (DbFlowWithOrderSpecification) first;
            if (!firstDbFlowWithOrderSpec.toOrder().isEmpty()) {
                orderBies.addAll(firstDbFlowWithOrderSpec.toOrder());
            }
        }
        if (second instanceof DbFlowWithOrderSpecification) {
            DbFlowWithOrderSpecification secondDbFlowWithOrderSpec = (DbFlowWithOrderSpecification) second;
            if (!secondDbFlowWithOrderSpec.toOrder().isEmpty()) {
                orderBies.addAll(secondDbFlowWithOrderSpec.toOrder());
            }
        }
        return orderBies;
    }

}