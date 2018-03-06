package com.tokopedia.home.beranda.listener;

import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;

/**
 * @author by errysuprayogi on 11/29/17.
 */

public interface HomeCategoryListener {

    void onSectionItemClicked(String actionLink);

    void onDigitalMoreClicked(int pos);

    void onCloseTicker(int pos);

    void onPromoClick(BannerSlidesModel slidesModel);

    void openShop();

    void actionAppLinkWalletHeader(String redirectUrlBalance, String appLinkBalance);

    void onRequestPendingCashBack();

    void actionInfoPendingCashBackTokocash(CashBackData cashBackData, String redirectUrlActionButton, String appLinkActionButton);

    void actionTokoPointClicked(String tokoPointUrl, String pageTitle);

    boolean isMainViewVisible();

    void showNetworkError(String message);

    void onDynamicChannelClicked(String applink);
}
