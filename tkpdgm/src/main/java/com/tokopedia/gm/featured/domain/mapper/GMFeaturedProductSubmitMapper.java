package com.tokopedia.gm.featured.domain.mapper;

import com.tokopedia.gm.featured.data.model.GMFeaturedProductModel;
import com.tokopedia.gm.featured.domain.model.GMFeaturedProductSubmitDomainModel;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by normansyahputa on 9/8/17.
 */

public class GMFeaturedProductSubmitMapper implements Func1<GMFeaturedProductModel, GMFeaturedProductSubmitDomainModel> {

    @Inject
    public GMFeaturedProductSubmitMapper() {
    }

    @Override
    public GMFeaturedProductSubmitDomainModel call(GMFeaturedProductModel gmFeaturedProductModel) {
        return convert(gmFeaturedProductModel);
    }

    public GMFeaturedProductSubmitDomainModel convert(GMFeaturedProductModel gmFeaturedProductModel) {
        GMFeaturedProductSubmitDomainModel gmFeaturedProductSubmitDomainModel
                = new GMFeaturedProductSubmitDomainModel();

        gmFeaturedProductSubmitDomainModel.setData(gmFeaturedProductModel.isData());
        return gmFeaturedProductSubmitDomainModel;
    }
}
