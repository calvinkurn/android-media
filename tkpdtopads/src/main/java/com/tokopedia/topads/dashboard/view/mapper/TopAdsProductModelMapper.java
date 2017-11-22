package com.tokopedia.topads.dashboard.view.mapper;

import com.tokopedia.topads.dashboard.domain.model.ProductDomain;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

/**
 * Created by Test on 5/31/2017.
 */

public class TopAdsProductModelMapper {
    public static TopAdsProductViewModel convertModelFromDomainToView(
            ProductDomain productDomain
    ) {
        TopAdsProductViewModel pd
                = new TopAdsProductViewModel();
        pd.setAdId(productDomain.getAdId());
        pd.setGroupName(productDomain.getGroupName());
        pd.setId(productDomain.getId());
        pd.setImageUrl(productDomain.getImageUrl());
        pd.setName(productDomain.getName());
        pd.setPromoted(productDomain.isPromoted());
        pd.setDepartmentId(productDomain.getDepartmentId());

        return pd;
    }
}
