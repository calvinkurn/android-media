package com.tokopedia.shop.product.view.presenter;

import com.tokopedia.shop.product.data.source.cloud.model.DynamicFilterModel;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.interactor.GetShopProductFilterUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.model.ShopProductFilterModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductFilterPresenter extends ShopProductListPresenter {

    private GetShopProductFilterUseCase getShopProductFilterUseCase;

    @Inject
    public ShopProductFilterPresenter(GetShopProductFilterUseCase getShopProductFilterUseCase,
                                      GetShopProductListUseCase getShopProductListUseCase) {
        super(getShopProductListUseCase);
        this.getShopProductFilterUseCase = getShopProductFilterUseCase;
    }

    public void getShopFilterList(){
        getShopProductFilterUseCase.execute(RequestParams.EMPTY, new Subscriber<DynamicFilterModel.DataValue>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(DynamicFilterModel.DataValue dataValue) {
                getView().renderList(convertSort(dataValue.getSort()));
            }
        });
    }

    public static List<ShopProductViewModel> convertSort(List<DynamicFilterModel.Sort> datas){
        List<ShopProductViewModel> result = new ArrayList<>();
        for (DynamicFilterModel.Sort data : datas) {
            ShopProductFilterModel shopProductFilterModel = new ShopProductFilterModel();
            shopProductFilterModel.setInputType(data.getInputType());
            shopProductFilterModel.setKey(data.getKey());
            shopProductFilterModel.setName(data.getName());
            shopProductFilterModel.setValue(data.getValue());

            result.add(shopProductFilterModel);
        }
        return result;


    }
}
