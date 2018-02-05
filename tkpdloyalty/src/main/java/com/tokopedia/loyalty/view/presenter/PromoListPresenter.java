package com.tokopedia.loyalty.view.presenter;

import android.util.Log;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.view.IPromoListView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public class PromoListPresenter implements IPromoListPresenter {
    private final IPromoInteractor promoInteractor;
    private final IPromoListView view;
    private int page = 1;

    @Inject
    public PromoListPresenter(IPromoInteractor promoInteractor, IPromoListView view) {
        this.promoInteractor = promoInteractor;
        this.view = view;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public void processGetPromoList(String subCategories, final String categoryName) {
        view.disableSwipeRefresh();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("categories", subCategories);
        param.put("categories_exclude", "30");
        param.put("page", String.valueOf(page));
        this.promoInteractor.getPromoList(param, new Subscriber<List<PromoData>>() {
            @Override
            public void onCompleted() {
                view.enableSwipeRefresh();
            }

            @Override
            public void onError(Throwable e) {
                handleError(e);
            }

            @Override
            public void onNext(List<PromoData> promoData) {
                sendImpressionTrackingData(promoData, categoryName);
                if (promoData.size() > 0) {
                    if (promoData.size() == 10) {
                        page++;
                        view.renderNextPage(true);
                    } else {
                        view.renderNextPage(false);
                    }
                    view.renderPromoDataList(promoData, true);
                } else {
                    view.renderErrorGetPromoDataList("Empty Data");
                }
            }
        });
    }

    public void sendImpressionTrackingData(List<PromoData> promoDataList, String categoryName) {

        List<Object> dataLayerSinglePromoCodeList = new ArrayList<>();
        for (int i = 0, promoDataListSize = promoDataList.size(); i < promoDataListSize; i++) {
            PromoData promoData = promoDataList.get(i);
            dataLayerSinglePromoCodeList.add(DataLayer.mapOf(
                    "id", promoData.getId(),
                    "name", "promo list - P" + String.valueOf(page) + " - " + categoryName,
                    "creative", promoData.getThumbnailImage(),
                    "position", String.valueOf(i + 1),
                    "promo_id", "0",
                    "promo_code", promoData.isMultiplePromo() ? promoData.getPromoCodeList() : promoData.getPromoCode())
            );
        }
        TrackingUtils.eventImpressionPromoList(dataLayerSinglePromoCodeList, "");
        Log.d("TOTOT", "sendImpressionTrackingData: ");
    }

    public void sendClickItemPromoListTrackingData(PromoData promoData, int position, String categoryName) {
        List<Object> dataLayerSinglePromoCodeList = new ArrayList<>();
        dataLayerSinglePromoCodeList.add(DataLayer.mapOf(
                "id", promoData.getId(),
                "name", "promo list - P" + String.valueOf(page) + " - " + categoryName,
                "creative", promoData.getThumbnailImage(),
                "position", String.valueOf(position + 1),
                "promo_id", "0",
                "promo_code", promoData.isMultiplePromo() ? promoData.getPromoCodeList() : promoData.getPromoCode())
        );
        TrackingUtils.eventClickPromoListItem(dataLayerSinglePromoCodeList, promoData.getTitle());
    }


    @Override
    public void processGetPromoListLoadMore(String subCategories, final String categoryName) {
        view.disableSwipeRefresh();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("categories", subCategories);
        param.put("categories_exclude", "30");
        param.put("page", String.valueOf(page));
        this.promoInteractor.getPromoList(param, new Subscriber<List<PromoData>>() {
            @Override
            public void onCompleted() {
                view.enableSwipeRefresh();
            }

            @Override
            public void onError(Throwable e) {
                handleError(e);
            }

            @Override
            public void onNext(List<PromoData> promoData) {
                sendImpressionTrackingData(promoData, categoryName);
                if (promoData.size() > 0) {
                    if (promoData.size() == 10) {
                        page++;
                        view.renderNextPage(true);
                    } else {
                        page = 1;
                        view.renderNextPage(false);
                    }
                } else {
                    page = 1;
                    view.renderNextPage(false);
                }
                view.renderPromoDataList(promoData, false);
            }
        });
    }

    private void handleError(Throwable e) {
        e.printStackTrace();
        if (e instanceof UnknownHostException) {
                             /* Ini kalau ga ada internet */
            view.renderErrorNoConnectionGetPromoDataList(
                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
            );
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                            /* Ini kalau timeout */
            view.renderErrorTimeoutConnectionGetPromoDataListt(
                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
            );
        } else if (e instanceof HttpErrorException) {
                            /* Ini Http error, misal 403, 500, 404,
                            code http errornya bisa diambil
                             e.getErrorCode */
            view.renderErrorHttpGetPromoDataList(e.getMessage());
        } else {
                             /* Ini diluar dari segalanya hahahaha */
            view.renderErrorHttpGetPromoDataList(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }
}
