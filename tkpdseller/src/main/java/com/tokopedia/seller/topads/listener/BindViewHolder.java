package com.tokopedia.seller.topads.listener;


import com.tokopedia.seller.topads.view.models.TopAdsAddProductModel;

/**
 * Created by normansyahputa on 2/13/17.
 */

public interface BindViewHolder<E> {
    void bind(TopAdsAddProductModel model);
}
