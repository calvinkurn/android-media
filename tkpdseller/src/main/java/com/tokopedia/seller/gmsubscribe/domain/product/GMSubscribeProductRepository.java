package com.tokopedia.seller.gmsubscribe.domain.product;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMAutoSubscribeDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public interface GMSubscribeProductRepository {
    Observable<List<GMProductDomainModel>> getCurrentProductSelection();

    Observable<List<GMProductDomainModel>> getExtendProductSelection();

    Observable<GMProductDomainModel> getCurrentProductSelectedData(Integer productId);

    Observable<GMAutoSubscribeDomainModel> getExtendProductSelectedData(Integer autoSubscribeProductId, Integer productId);
}
