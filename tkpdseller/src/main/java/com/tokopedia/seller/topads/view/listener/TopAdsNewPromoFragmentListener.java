package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.model.data.DataCredit;

import java.util.List;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsNewPromoFragmentListener {

    void onGroupNameListLoaded(@NonNull List<DataCredit> creditList);

    void onLoadGroupNameListError();
}
