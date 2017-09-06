package com.tokopedia.seller.product.featured.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.seller.product.featured.domain.model.FeaturedProductDomainModel;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductUseCase extends UseCase<FeaturedProductDomainModel> {
    @Override
    public Observable<FeaturedProductDomainModel> createObservable(RequestParams requestParams) {
        return null;
    }
}
