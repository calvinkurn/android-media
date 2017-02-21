package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.domain.interactor.TopAdsDefaultParamUseCase;
import com.tokopedia.seller.topads.domain.model.ProductDomain;
import com.tokopedia.seller.topads.view.TopAdsSearchProductView;
import com.tokopedia.seller.topads.view.models.TopAdsAddProductModel;
import com.tokopedia.seller.topads.view.models.TypeBasedModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * @author normansyahputa on 2/20/17.
 */

public class TopAdsAddProductListPresenter extends BaseDaggerPresenter<TopAdsSearchProductView> {

    public static final int PAGE_ROW = 12;
    private SessionHandler sessionHandler;
    private TopAdsDefaultParamUseCase topAdsDefaultParamUseCase;
    private Map<String, String> params;
    private TopAdsSearchProductView view;
    private int page;
    private String query;

    public TopAdsAddProductListPresenter() {
        page = 0;
        params = new TKPDMapParam<>();
        fillParam(sessionHandler);
    }

    public void incrementPage() {
        page++;
    }

    public void resetPage() {
        page = 0;
    }

    public void setSessionHandler(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    public void setTopAdsDefaultParamUseCase(TopAdsDefaultParamUseCase topAdsDefaultParamUseCase) {
        this.topAdsDefaultParamUseCase = topAdsDefaultParamUseCase;
    }

    private void fillParam(SessionHandler sessionHandler) {
        if (sessionHandler != null)
            params.put("shop_id", sessionHandler.getShopID());
        if (query != null)
            params.put("keyword", query);
        params.put("rows", Integer.toString(PAGE_ROW));
        params.put("start", Integer.toString(PAGE_ROW * page));
    }

    public void loadMore() {
        fillParam(sessionHandler);
        topAdsDefaultParamUseCase.execute(params, new Subscriber<List<ProductDomain>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ProductDomain> productDomains) {
                if (isViewAttached()) {
                    getView().loadMore(convertTo(productDomains));
                }
            }
        });
    }

    public void searchProduct() {
        fillParam(sessionHandler);
        topAdsDefaultParamUseCase.execute(params, new Subscriber<List<ProductDomain>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ProductDomain> productDomains) {
                if (isViewAttached()) {
                    getView().loadData(convertTo(productDomains));
                }
            }
        });
    }

    private List<TypeBasedModel> convertTo(List<ProductDomain> productDomains) {
        List<TypeBasedModel> typeBasedModels = new ArrayList<>();

        for (ProductDomain productDomain : productDomains) {
            TopAdsAddProductModel topAdsAddProductModel =
                    new TopAdsAddProductModel(
                            productDomain.getImageUrl(),
                            productDomain.getName(),
                            productDomain.getGroupName(),
                            productDomain
                    );

            typeBasedModels.add(topAdsAddProductModel);
        }


        return typeBasedModels;
    }


    @Override
    public void attachView(TopAdsSearchProductView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsDefaultParamUseCase.unsubscribe();
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
