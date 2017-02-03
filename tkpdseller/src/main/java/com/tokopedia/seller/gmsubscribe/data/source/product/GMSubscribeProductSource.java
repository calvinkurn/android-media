package com.tokopedia.seller.gmsubscribe.data.source.product;


import com.tokopedia.seller.gmsubscribe.domain.product.model.GMAutoSubscribeDomainModel;
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

    public Observable<GMProductDomainModel> getCurrentProductSelectedData(Integer productId) {
        return getCurrentProductSelection().map(new SelectedProductFinder(productId));
    }

    public Observable<GMAutoSubscribeDomainModel> getExtendProductSelectedData(Integer autoSubscribeProductId, Integer productId) {
        return getExtendProductSelection()
                .map(new SelectedProductFinder(autoSubscribeProductId))
                .map(new ConvertToAutoSubscribeData())
                .flatMap(new GetNextAutoSubscribeDate(productId));
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

    private class SelectedProductFinder implements Func1<List<GMProductDomainModel>, GMProductDomainModel> {
        private final Integer productId;

        public SelectedProductFinder(Integer productId) {
            this.productId = productId;
        }

        @Override
        public GMProductDomainModel call(List<GMProductDomainModel> gmProductDomainModels) {
            for(GMProductDomainModel domainModel: gmProductDomainModels){
                if(domainModel.getProductId() == productId){
                    return domainModel;
                }
            }
            return null;
        }
    }

    private class ConvertToAutoSubscribeData implements Func1<GMProductDomainModel, GMAutoSubscribeDomainModel> {
        @Override
        public GMAutoSubscribeDomainModel call(GMProductDomainModel domainModel) {
            return GMAutoSubscribeDomainModel.createFromGenericModel(domainModel);
        }
    }

    private class GetNextAutoSubscribeDate implements Func1<GMAutoSubscribeDomainModel, Observable<GMAutoSubscribeDomainModel>> {
        private final Integer currentProductId;

        private GetNextAutoSubscribeDate(Integer currentProductId) {
            this.currentProductId = currentProductId;
        }

        @Override
        public Observable<GMAutoSubscribeDomainModel> call(GMAutoSubscribeDomainModel gmAutoSubscribeDomainModel) {
            return getCurrentProductSelectedData(currentProductId)
                    .map(new SetDateToAutoSubscribeDomainModel(gmAutoSubscribeDomainModel));
        }

        private class SetDateToAutoSubscribeDomainModel implements Func1<GMProductDomainModel, GMAutoSubscribeDomainModel> {
            private final GMAutoSubscribeDomainModel autoSubscribeDomainModel;

            public SetDateToAutoSubscribeDomainModel(GMAutoSubscribeDomainModel autoSubscribeDomainModel) {
                this.autoSubscribeDomainModel = autoSubscribeDomainModel;
            }

            @Override
            public GMAutoSubscribeDomainModel call(GMProductDomainModel domainModel) {
                autoSubscribeDomainModel.setNextAutoSubscribe(domainModel.getNextInv());
                return autoSubscribeDomainModel;
            }
        }
    }


}
