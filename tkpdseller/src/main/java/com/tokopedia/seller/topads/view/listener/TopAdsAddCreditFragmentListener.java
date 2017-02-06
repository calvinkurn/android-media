package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;

import java.util.List;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsAddCreditFragmentListener {

    void onCreditListLoaded(@NonNull List<DataCredit> creditList);

    void onLoadCreditListError();
}
