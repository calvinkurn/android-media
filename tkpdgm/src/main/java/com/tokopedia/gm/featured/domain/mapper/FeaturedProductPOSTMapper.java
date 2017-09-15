package com.tokopedia.seller.goldmerchant.featured.domain.mapper;

import com.tokopedia.seller.goldmerchant.featured.data.model.FeaturedProductPOSTModel;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductPOSTDomainModel;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by normansyahputa on 9/8/17.
 */

public class FeaturedProductPOSTMapper implements Func1<FeaturedProductPOSTModel, FeaturedProductPOSTDomainModel> {

    @Inject
    public FeaturedProductPOSTMapper() {
    }

    @Override
    public FeaturedProductPOSTDomainModel call(FeaturedProductPOSTModel featuredProductPOSTModel) {
        return convert(featuredProductPOSTModel);
    }

    public FeaturedProductPOSTDomainModel convert(FeaturedProductPOSTModel featuredProductPOSTModel) {
        FeaturedProductPOSTDomainModel featuredProductPOSTDomainModel
                = new FeaturedProductPOSTDomainModel();

        featuredProductPOSTDomainModel.setData(featuredProductPOSTModel.isData());
        return featuredProductPOSTDomainModel;
    }
}
