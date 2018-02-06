package com.tokopedia.transaction.checkout.view.activity;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author anggaprasetiyo on 06/02/18.
 */

public class CartItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;
    private final boolean excludeFirstLastItem;
    private int start;

    public CartItemDecoration(int verticalSpaceHeight, boolean excludeFirstLastItem, int start) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.excludeFirstLastItem = excludeFirstLastItem;
        this.start = start;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (excludeFirstLastItem) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1
                    && parent.getChildAdapterPosition(view) >= start) {
                outRect.bottom = verticalSpaceHeight;
            }
        } else {
            outRect.bottom = verticalSpaceHeight;
        }
    }

    public void setStart(int start) {
        this.start = start;
    }
}
