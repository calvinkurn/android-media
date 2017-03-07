package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.domain.interactor.TopAdsDefaultParamUseCase;
import com.tokopedia.seller.topads.domain.model.ProductDomain;
import com.tokopedia.seller.topads.domain.model.ProductListDomain;
import com.tokopedia.seller.topads.utils.DefaultErrorSubscriber;
import com.tokopedia.seller.topads.view.TopAdsSearchProductView;
import com.tokopedia.seller.topads.view.model.NonPromotedTopAdsAddProductModel;
import com.tokopedia.seller.topads.view.model.PromotedTopAdsAddProductModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.view.model.TypeBasedModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author normansyahputa on 2/20/17.
 */

public class TopAdsAddProductListPresenter extends BaseDaggerPresenter<TopAdsSearchProductView> {

    public static final int PAGE_ROW = 12;
    private static final int
            SEARCHVIEW_NETWORK_CALL = 0,
            LOAD_MORE_NETWORK_CALL = 1,
            PULL_TO_REFRESH_NETWORK_CALL = 2,
            RETRY_NETWORK_CALL = 3,
            ON_ACTIVITY_FOR_RESULT = 4,
            NO_NETWORK_CALL = -1;

    private SessionHandler sessionHandler;
    private TopAdsDefaultParamUseCase topAdsDefaultParamUseCase;
    private Map<String, String> params;
    private TopAdsSearchProductView view;
    private int page;
    private String query;
    private int selectedFilterEtalaseId = -1;
    private int selectedFilterStatus = -1;
    private DefaultErrorSubscriber.ErrorNetworkListener errorNetworkListener;
    private NetworkStatus networkStatus;
    private int networkCallCount = 0;
    public TopAdsAddProductListPresenter() {
        page = 0;
        params = new TKPDMapParam<>();
        fillParam(sessionHandler);

        // set this flag to hit network
        setNetworkStatus(NetworkStatus.PULLTOREFRESH);
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

    public void setErrorNetworkListener(DefaultErrorSubscriber.ErrorNetworkListener errorNetworkListener) {
        this.errorNetworkListener = errorNetworkListener;
    }

    private void resetHitNetwork() {
        setNetworkStatus(NetworkStatus.NONETWORKCALL);
    }

    public boolean isFirstTime(){
        return (isHitNetwork() && networkCallCount <= 0);
    }

    private void fillParam(SessionHandler sessionHandler) {
        if (sessionHandler != null)
            params.put(TopAdsNetworkConstant.PARAM_SHOP_ID, sessionHandler.getShopID());
        if (query != null) {
            params.put(TopAdsNetworkConstant.PARAM_KEYWORD, query);
        } else {
            params.remove(TopAdsNetworkConstant.PARAM_KEYWORD);
        }

        params.put(TopAdsNetworkConstant.PARAM_ROWS, Integer.toString(PAGE_ROW));
        params.put(TopAdsNetworkConstant.PARAM_START, Integer.toString(PAGE_ROW * page));
        if (selectedFilterEtalaseId > 0) {
            params.put(TopAdsNetworkConstant.PARAM_ETALASE, Integer.toString(selectedFilterEtalaseId));
        } else {
            params.remove(TopAdsNetworkConstant.PARAM_ETALASE);
        }

        if (selectedFilterStatus > 0) {
            params.put(TopAdsNetworkConstant.PARAM_IS_PROMOTED, Integer.toString(selectedFilterStatus));
        } else {
            params.remove(TopAdsNetworkConstant.PARAM_IS_PROMOTED);
        }
    }

    public NetworkStatus getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(NetworkStatus networkStatus) {
        this.networkStatus = networkStatus;
    }

    public void loadMore() {
        if (isHitNetwork()) {
            fillParam(sessionHandler);
            topAdsDefaultParamUseCase.execute(params,
                    new DefaultErrorSubscriber<ProductListDomain>(errorNetworkListener) {
                        @Override
                        public void onCompleted() {
                            resetHitNetwork();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onNext(ProductListDomain productListDomain) {
                            if (isViewAttached()) {
                                getView().dismissSnackbar();
                                getView().setLoadMoreFlag(productListDomain.isEof());
                                getView().loadMore(convertTo(productListDomain.getProductDomains()));
                                networkCallCount++;
                            }
                        }
                    });
        }
    }

    public void searchProduct() {
        if (isHitNetwork()) {
            fillParam(sessionHandler);
            topAdsDefaultParamUseCase.execute(params,
                    new DefaultErrorSubscriber<ProductListDomain>(errorNetworkListener) {
                        @Override
                        public void onCompleted() {
                            resetHitNetwork();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                }

                        @Override
                        public void onNext(ProductListDomain productListDomain) {
                            if (isViewAttached()) {
                                getView().dismissSnackbar();
                                getView().setLoadMoreFlag(productListDomain.isEof());
                                getView().loadData(convertTo(productListDomain.getProductDomains()));
                                networkCallCount++;
                            }
                        }
                    });
        }
    }

    private boolean isHitNetwork() {
        switch (networkStatus) {
            case LOADMORE:
            case PULLTOREFRESH:
            case SEARCHVIEW:
            case RETRYNETWORKCALL:
            case ONACTIVITYFORRESULT:
                return true;
            case NONETWORKCALL:
            default:
                return false;
        }
    }

    private List<TypeBasedModel> convertTo(List<ProductDomain> productDomains) {
        List<TypeBasedModel> typeBasedModels = new ArrayList<>();

        boolean skipWithAdId = getView().isExistingGroup();

        for (ProductDomain productDomain : productDomains) {

            if (skipWithAdId && productDomain.isPromoted()) {
                PromotedTopAdsAddProductModel promotedTopAdsAddProductModel
                        = new PromotedTopAdsAddProductModel(
                        productDomain.getName(),
                        productDomain.getGroupName(),
                        convertModelFromDomainToView(productDomain)
                );
                typeBasedModels.add(promotedTopAdsAddProductModel);
            } else {
                NonPromotedTopAdsAddProductModel nonPromotedTopAdsAddProductModel
                        = new NonPromotedTopAdsAddProductModel(
                        productDomain.getName(),
                        productDomain.getGroupName(),
                        convertModelFromDomainToView(productDomain)
                );
                typeBasedModels.add(nonPromotedTopAdsAddProductModel);
            }


        }


        return typeBasedModels;
    }

    private TopAdsProductViewModel convertModelFromDomainToView(
            ProductDomain productDomain
    ) {
        TopAdsProductViewModel pd
                = new TopAdsProductViewModel();
        pd.setAdId(productDomain.getAdId());
        pd.setGroupName(productDomain.getGroupName());
        pd.setId(productDomain.getId());
        pd.setImageUrl(productDomain.getImageUrl());
        pd.setName(productDomain.getName());
        pd.setPromoted(productDomain.isPromoted());

        return pd;
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

    public void putSelectedEtalaseId(int etalaseId){
        this.selectedFilterEtalaseId = etalaseId;
    }

    public void putSelectedFilterStatus(int filterStatus){
        this.selectedFilterStatus = filterStatus;
    }

    public int getSelectedFilterEtalaseId() {
        return selectedFilterEtalaseId;
    }

    public int getSelectedFilterStatus() {
        return selectedFilterStatus;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public enum NetworkStatus {
        SEARCHVIEW(SEARCHVIEW_NETWORK_CALL),
        LOADMORE(LOAD_MORE_NETWORK_CALL),
        PULLTOREFRESH(PULL_TO_REFRESH_NETWORK_CALL),
        NONETWORKCALL(NO_NETWORK_CALL),
        RETRYNETWORKCALL(RETRY_NETWORK_CALL),
        ONACTIVITYFORRESULT(ON_ACTIVITY_FOR_RESULT);

        private int type;

        NetworkStatus(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
