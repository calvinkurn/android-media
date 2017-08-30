package com.tokopedia.seller.reputation.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.utils.GoldMerchantDateUtils;
import com.tokopedia.seller.reputation.data.model.request.SellerReputationRequest;
import com.tokopedia.seller.reputation.domain.interactor.ReviewReputationMergeUseCase;
import com.tokopedia.seller.reputation.domain.interactor.ReviewReputationUseCase;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;
import com.tokopedia.seller.reputation.util.ReputationDateUtils;
import com.tokopedia.seller.reputation.view.SellerReputationView;
import com.tokopedia.seller.reputation.view.model.EmptyListModel;
import com.tokopedia.seller.reputation.view.model.ReputationReviewModel;
import com.tokopedia.seller.reputation.view.model.SetDateHeaderModel;
import com.tokopedia.seller.topads.dashboard.utils.DefaultErrorSubscriber;
import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsAddProductListPresenter;
import com.tokopedia.seller.util.ShopNetworkController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author normansyahputa on 3/15/17.
 */
public class SellerReputationFragmentPresenter extends BaseDaggerPresenter<SellerReputationView> {
    public static final String REPUTATION_DATE = "dd-MM-yyyy";
    private SellerReputationRequest sellerReputationRequest;
    private SessionHandler sessionHandler;
    private TopAdsAddProductListPresenter.NetworkStatus networkStatus;
    private int networkCallCount = 0;
    private GCMHandler gcmHandler;
    private ReviewReputationUseCase reviewReputationUseCase;
    private DefaultErrorSubscriber.ErrorNetworkListener errorNetworkListener;
    private ReviewReputationMergeUseCase reviewReputationMergeUseCase;
    private ShopNetworkController.ShopInfoParam shopInfoParam;
    private String messageExceptionDesc;

    public SellerReputationFragmentPresenter() {
        sellerReputationRequest = new SellerReputationRequest();
        sellerReputationRequest.setStartDate(ReputationDateUtils.getDateFormat(
                Calendar.getInstance().getTimeInMillis(), 7
        ));
        sellerReputationRequest.setsDate(
                GoldMerchantDateUtils.getPreviousDate(Calendar.getInstance().getTimeInMillis(), 7));
        sellerReputationRequest.setEndDate(ReputationDateUtils.getDateFormat(
                Calendar.getInstance().getTimeInMillis(), 0
        ));
        sellerReputationRequest.seteDate(
                GoldMerchantDateUtils.getPreviousDate(Calendar.getInstance().getTimeInMillis(), 0));

        // set this flag to hit network
        setNetworkStatus(TopAdsAddProductListPresenter.NetworkStatus.PULLTOREFRESH);

        shopInfoParam = new ShopNetworkController.ShopInfoParam();
    }

    public void incrementPage() {
        sellerReputationRequest.incrementPage();
    }

    public void resetPage() {
        sellerReputationRequest.resetPage();
    }

    public void resetHitNetwork() {
        setNetworkStatus(TopAdsAddProductListPresenter.NetworkStatus.NONETWORKCALL);
    }

    public void setStartDate(long startDate) {
        sellerReputationRequest.setStartDate(
                GoldMerchantDateUtils
                        .getDateFormatForInput(startDate, ReputationDateUtils.DATE_FORMAT)
        );
    }

    public void setEndDate(long endDate) {
        sellerReputationRequest.setEndDate(
                GoldMerchantDateUtils
                        .getDateFormatForInput(endDate, ReputationDateUtils.DATE_FORMAT)
        );
    }

    public boolean isFirstTime() {
        return (isHitNetwork() && networkCallCount <= 0);
    }

    public void setSessionHandler(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    public void setGcmHandler(GCMHandler gcmHandler) {
        this.gcmHandler = gcmHandler;
    }

    public void setReviewReputationUseCase(ReviewReputationUseCase reviewReputationUseCase) {
        this.reviewReputationUseCase = reviewReputationUseCase;
    }

    public void setErrorNetworkListener(DefaultErrorSubscriber.ErrorNetworkListener errorNetworkListener) {
        this.errorNetworkListener = errorNetworkListener;
    }

    public void setReviewReputationMergeUseCase(ReviewReputationMergeUseCase reviewReputationMergeUseCase) {
        this.reviewReputationMergeUseCase = reviewReputationMergeUseCase;
    }

    private TKPDMapParam<String, String> fillParam() {
        return AuthUtil.generateParamsNetwork(
                sessionHandler.getLoginID(), gcmHandler.getRegistrationId(),
                sellerReputationRequest.getParamSummaryReputation());
    }

    private ShopNetworkController.ShopInfoParam fillParamShopInfo() {
        shopInfoParam.shopId = sessionHandler.getShopID();
        return shopInfoParam;
    }

    private RequestParams fillParamShopInfo2() {
        return ShopNetworkController.RequestParamFactory.generateRequestParam(sessionHandler.getLoginID(),
                gcmHandler.getRegistrationId(), fillParamShopInfo());
    }

    private RequestParams fillParamReview() {
        TKPDMapParam<String, String> mapParam = fillParam();
        String shopID = sessionHandler.getShopID();

        return ReviewReputationUseCase.RequestParamFactory.generateRequestParam(shopID, mapParam);
    }

    private RequestParams mergeFillParam() {
        return ReviewReputationMergeUseCase.RequestParamFactory.generateRequestParam(
                fillParamReview(),
                fillParamShopInfo2()
        );
    }

    public TopAdsAddProductListPresenter.NetworkStatus getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(TopAdsAddProductListPresenter.NetworkStatus networkStatus) {
        this.networkStatus = networkStatus;

        switch (getNetworkStatus()) {
            case ONACTIVITYFORRESULT:
            case PULLTOREFRESH:
            case SEARCHVIEW:
                resetPage();
                break;
            default:
                break;
        }
    }

    public boolean isHitNetwork() {
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

    public void fillMessages(Context context) {
        messageExceptionDesc = context.getString(R.string.message_exception_description);
    }

    public void firstTimeNetworkCall2() {
        if (isHitNetwork()) {
            reviewReputationMergeUseCase.execute(
                    mergeFillParam()
                    , new DefaultErrorSubscriber<List<Object>>(errorNetworkListener, messageExceptionDesc) {
                        @Override
                        public void onCompleted() {
                            resetHitNetwork();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onNext(List<Object> objects) {
                            super.onNext(objects);
                            if (isViewAttached()) {

                                Object object = objects.get(0);
                                boolean isEmptyShop = false;
                                if (object != null && object instanceof ShopModel) {
                                    ShopModel shopModel = (ShopModel) object;
                                    getView().loadShopInfo(shopModel);
                                    if(shopModel.stats.shopReputationScore.equals("0") ||
                                            shopModel.stats.shopReputationScore.isEmpty()){
                                        isEmptyShop = true;
                                    }
                                }

                                object = objects.get(1);
                                if (object != null && object instanceof SellerReputationDomain) {
                                    SellerReputationDomain sellerReputationDomain
                                            = (SellerReputationDomain) object;

                                    getView().dismissSnackbar();
                                    getView().setLoadMoreFlag(
                                            sellerReputationDomain.getLinks().getNext() == null);
                                    List<ItemType> itemTypes = convertTo(sellerReputationDomain.getList());

                                    EmptyListModel emptyListModel = null;
                                    if (itemTypes.isEmpty()) {
                                        itemTypes.add(emptyListModel = new EmptyListModel());
                                        emptyListModel.setEmptyShop(isEmptyShop);
                                    }

                                    SetDateHeaderModel headerModel = getView().getHeaderModel();
                                    if (headerModel == null) {
                                        SetDateHeaderModel setDateHeaderModel = new SetDateHeaderModel();
                                        setDateHeaderModel.setStartDate(
                                                formatDate(sellerReputationRequest.getsDate()));
                                        setDateHeaderModel.setsDate(sellerReputationRequest.getsDate());
                                        setDateHeaderModel.setEndDate(
                                                formatDate(sellerReputationRequest.geteDate()));
                                        setDateHeaderModel.seteDate(sellerReputationRequest.geteDate());

                                        itemTypes.add(0, setDateHeaderModel);
                                        if (emptyListModel != null)
                                            emptyListModel.setSetDateHeaderModel(setDateHeaderModel);
                                    } else {
                                        itemTypes.add(0, headerModel);
                                        if (emptyListModel != null)
                                            emptyListModel.setSetDateHeaderModel(headerModel);
                                    }
                                    getView().loadMore(itemTypes);
                                }
                            }
                        }
                    }
            );
        }
    }

    public String formatDate(long date) {
        return GoldMerchantDateUtils.getDateFormatForInput(
                date, REPUTATION_DATE
        );
    }

    public void firstTimeNetworkCall() {
        if (isHitNetwork()) {
            reviewReputationUseCase.execute(sessionHandler.getShopID(), fillParam(),
                    new DefaultErrorSubscriber<SellerReputationDomain>(errorNetworkListener) {
                        @Override
                        public void onCompleted() {
                            resetHitNetwork();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onNext(SellerReputationDomain sellerReputationDomain) {
                            super.onNext(sellerReputationDomain);
                            if (isViewAttached()) {
                                getView().dismissSnackbar();
                                getView().setLoadMoreFlag(
                                        sellerReputationDomain.getLinks().getNext() == null);
                                getView().loadMore(convertTo(sellerReputationDomain.getList()));
                            }
                        }
                    });
        }
    }

    public void loadMoreNetworkCall() {
        if (isHitNetwork()) {
            reviewReputationUseCase.execute(fillParamReview(),
                    new DefaultErrorSubscriber<SellerReputationDomain>(errorNetworkListener, messageExceptionDesc) {
                        @Override
                        public void onCompleted() {
                            resetHitNetwork();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onNext(SellerReputationDomain sellerReputationDomain) {
                            super.onNext(sellerReputationDomain);
                            if (isViewAttached()) {
                                getView().dismissSnackbar();
                                getView().setLoadMoreFlag(
                                        sellerReputationDomain.getLinks().getNext() == null);
                                getView().loadData(convertTo(sellerReputationDomain.getList()));
                            }
                        }
                    });
        }
    }

    private List<ItemType> convertTo(List<SellerReputationDomain.Data> datas) {
        List<ItemType> reputationReviewModels =
                new ArrayList<>();

        for (SellerReputationDomain.Data data : datas) {
            ReputationReviewModel.Data resultData =
                    new ReputationReviewModel.Data();
            resultData.setPenaltyScore(data.getPenaltyScore());
            resultData.setDate(data.getDate());
            resultData.setInformation(data.getInformation());

            ReputationReviewModel reputationReviewModel =
                    new ReputationReviewModel();
            reputationReviewModel.setData(resultData);

            reputationReviewModels.add(reputationReviewModel);
        }

        return reputationReviewModels;

    }
}
