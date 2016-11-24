package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.model.data.Summary;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsProductFragmentListener {

    void onSummaryLoaded(@NonNull Summary summary);

    void onLoadSummaryError(@NonNull Throwable throwable);
}
