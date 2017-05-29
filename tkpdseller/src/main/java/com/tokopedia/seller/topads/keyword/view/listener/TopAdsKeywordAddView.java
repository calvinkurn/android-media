package com.tokopedia.seller.topads.keyword.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.List;

/**
 * Created by normansyahputa on 5/22/17.
 */

public interface TopAdsKeywordAddView extends CustomerView {
    void onSuccessSaveKeyword();
    void onFailedSaveKeyword();
}
