package com.tokopedia.loyalty.view.view;

import android.content.Context;

import com.tokopedia.loyalty.view.data.PromoData;

import java.util.List;

public interface IPromoDetailView extends IMvpView {

    void renderPromoDetail(PromoData promoData);

//    void renderNextPage(boolean hasNextPage);
//
//    void renderErrorGetPromoDetail(String message);
//
//    void renderErrorHttpGetPromoDetail(String message);
//
//    void renderErrorNoConnectionGetPromoDetail(String message);
//
//    void renderErrorTimeoutConnectionGetPromoDetail(String message);
//
//    void disableSwipeRefresh();
//
//    void enableSwipeRefresh();
//
//    Context getActivityContext();

}
