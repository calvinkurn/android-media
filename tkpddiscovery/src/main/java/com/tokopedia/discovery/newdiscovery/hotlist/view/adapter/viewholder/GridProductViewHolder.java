package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;

import java.util.List;

/**
 * Created by hangnadi on 10/8/17.
 */

public class GridProductViewHolder extends ListProductViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_grid;

    public GridProductViewHolder(View parent, ItemClickListener mItemClickListener) {
        super(parent, mItemClickListener);
    }
}
