package com.tokopedia.shop.etalase.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.etalase.domain.interactor.GetShopEtalaseUseCase;
import com.tokopedia.shop.etalase.domain.model.ShopEtalaseRequestModel;
import com.tokopedia.shop.etalase.view.listener.ShopEtalaseView;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalasePresenter extends BaseDaggerPresenter<ShopEtalaseView> {

    private static final String TAG = "ShopEtalasePresenter";
    private GetShopEtalaseUseCase getShopEtalaseUseCase;

    @Inject
    public ShopEtalasePresenter(GetShopEtalaseUseCase getShopEtalaseUseCase) {
        this.getShopEtalaseUseCase = getShopEtalaseUseCase;
    }


    public void getShopEtalase(String shopId, String shopDomain) {
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
                if (isViewAttached()) {
                    getView().showGetListError(throwable);
                }
            }

            @Override
            public void onNext(PagingListOther<EtalaseModel> pagingListOther) {
                if (isViewAttached()) {
                    getView().renderList(mergeListOther(pagingListOther), false);
                }
            }
        });
    }

    private List<ShopEtalaseViewModel> mergeListOther(PagingListOther<EtalaseModel> pagingListOther) {
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
