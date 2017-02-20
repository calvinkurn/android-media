package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.domain.model.data.Product;

import java.util.List;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsAddProductFragmentListener {

    void onProductListLoaded(@NonNull List<Product> creditList);

    void onLoadProductListError();
}
