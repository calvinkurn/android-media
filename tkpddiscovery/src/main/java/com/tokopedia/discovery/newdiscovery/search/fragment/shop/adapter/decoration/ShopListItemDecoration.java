package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ShopListItemDecoration extends RecyclerView.ItemDecoration {
    public ShopListItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = 4;
        outRect.right = 4;
        outRect.bottom = -4;
        outRect.top = 0;
    }
}
