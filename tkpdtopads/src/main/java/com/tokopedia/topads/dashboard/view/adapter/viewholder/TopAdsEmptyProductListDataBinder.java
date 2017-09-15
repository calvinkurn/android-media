package com.tokopedia.seller.topads.dashboard.view.adapter.viewholder;

import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseEmptyDataBinder;

/**
 * Created by zulfikarrahman on 8/13/17.
 */

public class TopAdsEmptyProductListDataBinder extends BaseEmptyDataBinder {
    public TopAdsEmptyProductListDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter, R.drawable.ic_empty_product_list);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        super.bindViewHolder(holder, position);
        EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
        if (TextUtils.isEmpty(emptyTitleText)) {
            emptyViewHolder.emptyTitleTextView.setVisibility(View.GONE);
        }
    }


}
