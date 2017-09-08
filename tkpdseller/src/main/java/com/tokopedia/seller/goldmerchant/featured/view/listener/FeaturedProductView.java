package com.tokopedia.seller.goldmerchant.featured.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.model.FeaturedProductModel;

import java.util.List;

/**
 * Created by normansyahputa on 9/7/17.
 */

public interface FeaturedProductView extends CustomerView {

    void onSearchLoaded(@NonNull List<FeaturedProductModel> list, int totalItem);

    void onPostSuccess();
}
