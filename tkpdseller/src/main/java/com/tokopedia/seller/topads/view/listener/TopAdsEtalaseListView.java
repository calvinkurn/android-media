package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.product.model.etalase.Etalase;
import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 2/17/17.
 */
public interface TopAdsEtalaseListView extends CustomerView {

    void onLoadSuccess(@NonNull List<com.tokopedia.core.shopinfo.models.etalasemodel.List> etalaseList);

    void onLoadSuccessEtalaseEmpty();

    void onLoadConnectionError();

    void onLoadError(String message);

    void showLoad (boolean isShow);
}
