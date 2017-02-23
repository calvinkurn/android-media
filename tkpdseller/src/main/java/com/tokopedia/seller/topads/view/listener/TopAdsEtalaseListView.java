package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.domain.model.data.Etalase;

import java.util.List;

/**
 * Created by zulfikarrahman on 2/17/17.
 */
public interface TopAdsEtalaseListView extends CustomerView {

    void onLoadSuccess(@NonNull List<Etalase> etalaseList);

    void onLoadSuccessEtalaseEmpty();

    void onLoadConnectionError();

    void onLoadError(String message);

    void showLoad (boolean isShow);
}
