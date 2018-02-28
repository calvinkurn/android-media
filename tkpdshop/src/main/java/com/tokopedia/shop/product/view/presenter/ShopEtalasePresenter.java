package com.tokopedia.shop.product.view.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.product.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.product.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.product.domain.interactor.GetShopEtalaseUseCase;
import com.tokopedia.shop.product.domain.model.ShopEtalaseRequestModel;
import com.tokopedia.shop.product.view.listener.ShopEtalaseView;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.model.ShopEtalaseViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalasePresenter extends BaseDaggerPresenter<ShopEtalaseView> {

    private GetShopEtalaseUseCase getShopEtalaseUseCase;

    private static final String TAG = "ShopEtalasePresenter";

    @Inject
    public ShopEtalasePresenter(GetShopEtalaseUseCase getShopEtalaseUseCase) {
        this.getShopEtalaseUseCase = getShopEtalaseUseCase;
    }


    public void getShopEtalase(String shopId, String shopDomain){
        ShopEtalaseRequestModel shopEtalaseRequestModel = new ShopEtalaseRequestModel();
        shopEtalaseRequestModel.setShopId(shopId);
        shopEtalaseRequestModel.setShopDomain(shopDomain);


        RequestParams params = GetShopEtalaseUseCase.createParams(shopEtalaseRequestModel);
        getShopEtalaseUseCase.execute(params, new Subscriber<PagingListOther<EtalaseModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(PagingListOther<EtalaseModel> pagingListOther) {
                if(isViewAttached()){
                    getView().renderList(mergeListOther(pagingListOther), PagingListUtils.checkNextPage(pagingListOther));
                }
            }
        });
    }

    private List<ShopEtalaseViewModel> mergeListOther(PagingListOther<EtalaseModel> pagingListOther){
        pagingListOther.getListOther().addAll(pagingListOther.getList());
        List<ShopEtalaseViewModel> shopEtalaseViewModels = new ArrayList<>();
        for (EtalaseModel etalaseModel : pagingListOther.getListOther()) {
            ShopEtalaseViewModel model = new ShopEtalaseViewModel();
            model.setEtalaseBadge(etalaseModel.getEtalaseBadge());
            model.setEtalaseId(etalaseModel.getEtalaseId());
            model.setEtalaseName(etalaseModel.getEtalaseName());
            model.setEtalaseNumProduct(etalaseModel.getEtalaseNumProduct());
            model.setEtalaseTotalProduct(etalaseModel.getEtalaseTotalProduct());
            model.setUseAce(etalaseModel.getUseAce());
            shopEtalaseViewModels.add(model);
        }

        return shopEtalaseViewModels;

    }

}
