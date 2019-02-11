package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ShopListItemDecoration extends RecyclerView.ItemDecoration {
    private final int left;
    private final int right;
    private final int bottom;
    private final int top;

    public ShopListItemDecoration(int left,
                                  int right,
                                  int bottom,
                                  int top) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;
        outRect.top = top;
    }
}
