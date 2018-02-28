package com.tokopedia.shop.product.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.product.data.source.cloud.model.DynamicFilterModel;
import com.tokopedia.shop.product.domain.interactor.GetShopProductFilterUseCase;
import com.tokopedia.shop.product.view.listener.ShopFilterListView;
import com.tokopedia.shop.product.view.model.ShopProductFilterModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductFilterPresenter extends BaseDaggerPresenter<ShopFilterListView> {

    private GetShopProductFilterUseCase getShopProductFilterUseCase;

    @Inject
    public ShopProductFilterPresenter(GetShopProductFilterUseCase getShopProductFilterUseCase) {
        this.getShopProductFilterUseCase = getShopProductFilterUseCase;
    }

    public static List<ShopProductFilterModel> convertSort(List<DynamicFilterModel.Sort> datas) {
        List<ShopProductFilterModel> result = new ArrayList<>();
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

    public void getShopFilterList(){
        getShopProductFilterUseCase.execute(new Subscriber<DynamicFilterModel.DataValue>() {
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
}
