package com.tokopedia.gm.featured.view.adapter;

import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseEmptyDataBinder;

/**
 * Created by Nisie on 2/26/16.
 */
public class ProductEmptyDataBinder extends BaseEmptyDataBinder {

    public ProductEmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter, R.drawable.ic_empty_state_kaktus);
    }
}