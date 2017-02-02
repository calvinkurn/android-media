package com.tokopedia.seller.gmsubscribe.data.source.product;


import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModelGroup;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GMSubscribeProductSource {

    private final GMSubscribeProductListSource gmSubscribeProductListSource;

    public GMSubscribeProductSource(GMSubscribeProductListSource gmSubscribeProductListSource) {
        this.gmSubscribeProductListSource = gmSubscribeProductListSource;
    }

    public Observable<List<GMProductDomainModel>> getCurrentProductSelection() {
        return gmSubscribeProductListSource.getData().map(new GetCurrentProductSelection());
    }

    public Observable<List<GMProductDomainModel>> getExtendProductSelection() {
        return gmSubscribeProductListSource.getData().map(new GetExtendProductSelection());
    }

    private class GetCurrentProductSelection implements Func1<GMProductDomainModelGroup, List<GMProductDomainModel>> {
        @Override
        public List<GMProductDomainModel> call(GMProductDomainModelGroup gmProductDomainModelGroup) {
            return gmProductDomainModelGroup.getCurrentProduct();
        }
    }

    private class GetExtendProductSelection implements Func1<GMProductDomainModelGroup, List<GMProductDomainModel>> {
        @Override
        public List<GMProductDomainModel> call(GMProductDomainModelGroup gmProductDomainModelGroup) {
            return gmProductDomainModelGroup.getExtendProduct();
        }
    }
}
