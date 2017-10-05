package com.tokopedia.seller.opportunity.domain.model;

import com.tokopedia.seller.opportunity.data.OpportunityFilterModel;
import com.tokopedia.seller.opportunity.data.OpportunityModel;

/**
 * @author by nisie on 6/2/17.
 */

public class OpportunityFirstTimeModel {
    final OpportunityModel opportunityModel;
    final OpportunityFilterModel opportunityFilterModel;


    public OpportunityFirstTimeModel(OpportunityModel opportunityModel,
                                     OpportunityFilterModel opportunityFilterModel) {
        this.opportunityModel = opportunityModel;
        this.opportunityFilterModel = opportunityFilterModel;
    }

    public OpportunityModel getOpportunityModel() {
        return opportunityModel;
    }

    public OpportunityFilterModel getOpportunityFilterModel() {
        return opportunityFilterModel;
    }
}
