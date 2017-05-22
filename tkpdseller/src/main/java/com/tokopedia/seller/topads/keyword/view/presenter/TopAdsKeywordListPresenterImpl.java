package com.tokopedia.seller.topads.keyword.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.view.domain.interactor.KeywordDashboardUseCase;
import com.tokopedia.seller.topads.keyword.view.domain.model.KeywordDashboardDomain;
import com.tokopedia.seller.topads.view.presenter.TopAdsAdListPresenter;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author normansyahputa on 5/17/17.
 */

public class TopAdsKeywordListPresenterImpl extends TopAdsKeywordListPresenter implements TopAdsAdListPresenter<GroupAd> {

    public static final String KEYWORD_DATE_FORMAT = "yyyy-MM-dd";
    private static final String TAG = "TopAdsKeywordListPresen";
    private KeywordDashboardUseCase keywordDashboardUseCase;

    @Inject
    public TopAdsKeywordListPresenterImpl(KeywordDashboardUseCase keywordDashboardUseCase) {
        this.keywordDashboardUseCase = keywordDashboardUseCase;
    }

    public void fetchPositiveKeyword(BaseKeywordParam baseKeywordParam) {
        keywordDashboardUseCase.execute(generateParam(baseKeywordParam), new Subscriber<KeywordDashboardDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
            }

            @Override
            public void onNext(KeywordDashboardDomain keywordDashboardDomain) {
                Log.d(TAG, "fetchPositiveKeyword " + keywordDashboardDomain);
                revealData(keywordDashboardDomain);
            }
        });
    }

    protected void revealData(KeywordDashboardDomain keywordDashboardDomain) {
        if (isViewAttached()) {
            getView().onSearchAdLoaded(
                    keywordDashboardDomain.getData(),
                    keywordDashboardDomain.getPage().getTotal()
            );
        }
    }

    public String formatDate(long date) {
        return GoldMerchantDateUtils.getDateFormatForInput(
                date, KEYWORD_DATE_FORMAT
        );
    }

    public void fetchNegativeKeyword(BaseKeywordParam baseKeywordParam) {
        keywordDashboardUseCase.execute(generateParam(baseKeywordParam), new Subscriber<KeywordDashboardDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
            }

            @Override
            public void onNext(KeywordDashboardDomain keywordDashboardDomain) {
                Log.d(TAG, "fetchNegativeKeyword " + keywordDashboardDomain);
                revealData(keywordDashboardDomain);
            }
        });
    }

    @Override
    public void unSubscribe() {

    }

    private RequestParams generateParam(BaseKeywordParam baseKeywordParam) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("shop_id", baseKeywordParam.shopId);
        if (baseKeywordParam.keywordTag != null) {
            requestParams.putString("keyword", baseKeywordParam.keywordTag);
        }
        if (baseKeywordParam.startDateDesc != null) {
            requestParams.putString("start_date", baseKeywordParam.startDateDesc);
        }
        if (baseKeywordParam.endDateDesc != null) {
            requestParams.putString("end_date", baseKeywordParam.endDateDesc);
        }
        requestParams.putString("page", Integer.toString(baseKeywordParam.page));
        requestParams.putString("is_positive", Integer.toString(baseKeywordParam.isPositive()));

//        requestParams.putString("keyword_id", Long.toString(baseKeywordParam.keywordId));
//        requestParams.putString("group_id", Long.toString(baseKeywordParam.groupId));
//        requestParams.putString("keyword_status", Integer.toString(baseKeywordParam.keywordStatus));
//        requestParams.putString("keyword_type_id", Integer.toString(baseKeywordParam.keywordTypeId));
//        requestParams.putString("sorting", Integer.toString(baseKeywordParam.sortingParam));
        return requestParams;
    }

    public KeywordPositiveParam generateKeywordPositiveParam() {
        return new KeywordPositiveParam();
    }

    public KeywordNegativeParam generateKeywordNegativeParam() {
        return new KeywordNegativeParam();
    }

    public BaseKeywordParam generateParam(
            Context context, String query, int page, boolean isPositive
    ) {
        BaseKeywordParam baseKeywordParam = isPositive ? generateKeywordPositiveParam() : generateKeywordNegativeParam();
        baseKeywordParam.shopId = SessionHandler.getShopID(context);
        if (query != null)
            baseKeywordParam.keywordTag = query;
        baseKeywordParam.page = page;

        return baseKeywordParam;
    }

    public BaseKeywordParam generateParam(
            Context context, String query, int page, boolean isPositive, long startDate, long endDate
    ) {
        BaseKeywordParam baseKeywordParam = generateParam(context, query, page, isPositive);
        baseKeywordParam.startDate = startDate;
        baseKeywordParam.endDate = endDate;

        baseKeywordParam.startDateDesc = formatDate(startDate);
        baseKeywordParam.endDateDesc = formatDate(endDate);

        return baseKeywordParam;
    }

    public static class BaseKeywordParam {
        long startDate;
        long endDate;
        String startDateDesc;
        String endDateDesc;

        String shopId;
        String keywordTag;
        int page;

        // filtering
        long groupId;
        int keywordStatus;
        int keywordTypeId;
        int sortingParam;
        boolean isPositive;
        long keywordId;

        public int isPositive() {
            return isPositive ? 1 : 0;
        }
    }

    public static class KeywordPositiveParam extends BaseKeywordParam {

        public KeywordPositiveParam() {
            isPositive = true;
        }
    }

    public static class KeywordNegativeParam extends BaseKeywordParam {
        public KeywordNegativeParam() {
            isPositive = false;
        }
    }
}
