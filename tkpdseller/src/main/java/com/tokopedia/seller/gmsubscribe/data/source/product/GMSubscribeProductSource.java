package com.tokopedia.seller.gmsubscribe.data.source.product;


import com.tokopedia.seller.gmsubscribe.domain.product.model.GMAutoSubscribeDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModelGroup;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;

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
        return Observable.zip(
                getExtendProductSelection().map(new SelectedProductFinder(autoSubscribeProductId)),
                getCurrentProductSelectedData(productId),
                getPaymentMethod(),
                new CombineExtendedProductSelected());
    }

    public Observable<String> getPaymentMethod() {
        return gmSubscribeProductListSource.getData().map(new GetPaymentMethod());
    }

    public Observable<Boolean> clearGMSubscribeProductCache() {
        return gmSubscribeProductListSource.clearData();
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

    private class GetPaymentMethod implements Func1<GMProductDomainModelGroup, String> {
        @Override
        public String call(GMProductDomainModelGroup gmProductDomainModelGroup) {
            return gmProductDomainModelGroup.getPaymentMethod();
        }
    }

    private class CombineExtendedProductSelected implements Func3<GMProductDomainModel, GMProductDomainModel, String, GMAutoSubscribeDomainModel> {
        @Override
        public GMAutoSubscribeDomainModel call(GMProductDomainModel autoSubscribeDomainModel, GMProductDomainModel currentProductDomainModel, String paymentMethod) {
            GMAutoSubscribeDomainModel resultDomainModel = new GMAutoSubscribeDomainModel();
            resultDomainModel.setTitle(autoSubscribeDomainModel.getName());
            resultDomainModel.setPrice(autoSubscribeDomainModel.getPrice());
            resultDomainModel.setNextAutoSubscribe(currentProductDomainModel.getNextInv());
            resultDomainModel.setPaymentMethod(paymentMethod);
            return resultDomainModel;
        }
    }
}
