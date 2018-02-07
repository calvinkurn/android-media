package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.seller.base.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.topads.R;

/**
 * Created by Nisie on 2/26/16.
 */
public class TopAdsEmptyAdDataBinder extends BaseEmptyDataBinder {

    public TopAdsEmptyAdDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter, R.drawable.ic_empty_state_kaktus);
    }

    public TopAdsEmptyAdDataBinder(DataBindAdapter dataBindAdapter, int errorDrawableRes) {
        super(dataBindAdapter, errorDrawableRes);
    }
}