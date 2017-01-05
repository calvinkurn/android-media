package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.DataCredit;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface TopAdsListPromoViewListener<T extends Ad> {

    void onSearchAdLoaded(@NonNull List<T> adList, int totalItem);

    void onLoadSearchAdError();

    void onTurnOnAdSuccess();

    void onTurnOnAdFailed();

    void onTurnOffAdSuccess();

    void onTurnOffAdFailed();

    void moveToDetail(Ad ad);
}
