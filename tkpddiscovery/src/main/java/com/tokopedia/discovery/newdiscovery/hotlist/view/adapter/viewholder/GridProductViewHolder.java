package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.HotlistListener;

/**
 * Created by hangnadi on 10/8/17.
 */

public class GridProductViewHolder extends ListProductViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_grid;

    public GridProductViewHolder(View parent, HotlistListener mHotlistListener) {
        super(parent, mHotlistListener);
    }
}
