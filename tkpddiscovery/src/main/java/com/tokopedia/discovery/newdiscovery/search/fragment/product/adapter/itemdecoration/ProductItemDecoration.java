package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.TopAdsViewHolder;

import java.util.Arrays;
import java.util.List;

public class ProductItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacing;
    private final int color;

    private final List<Integer> allowedViewTypes = Arrays.asList(
            R.layout.search_result_product_item_big_grid,
            R.layout.search_result_product_item_grid,
            R.layout.search_result_product_item_list);

    public ProductItemDecoration(int spacing, int color) {
        this.spacing = spacing;
        this.color = color;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        final int absolutePos = parent.getChildAdapterPosition(view);

        if (isProductItem(parent, absolutePos)) {
            int firstProductItemPos = absolutePos;
            while (isProductItem(parent, firstProductItemPos - 1)) firstProductItemPos--;
            int relativePos = absolutePos - firstProductItemPos;

            final int totalSpanCount = getTotalSpanCount(parent);

            outRect.top = isTopProductItem(parent, absolutePos, relativePos, totalSpanCount) ? spacing : spacing / 2;
            outRect.left = isFirstInRow(relativePos, totalSpanCount) ? spacing : spacing / 2;
            if (parent.getLayoutManager() instanceof GridLayoutManager) {
                outRect.right = isLastInRow(relativePos, totalSpanCount) ? spacing : spacing / 2;
            } else {
                outRect.right = 0;
            }
            outRect.bottom = isBottomProductItem(parent, absolutePos, relativePos, totalSpanCount) ? spacing : spacing / 2;
        }
    }

    private boolean isTopProductItem(RecyclerView parent, int absolutePos, int relativePos, int totalSpanCount) {
        return !isProductItem(parent, absolutePos - relativePos % totalSpanCount - 1);
    }

    private boolean isBottomProductItem(RecyclerView parent, int absolutePos, int relativePos, int totalSpanCount) {
        return !isProductItem(parent, absolutePos + totalSpanCount - relativePos % totalSpanCount);
    }

    private boolean isFirstInRow(int relativePos, int spanCount) {
        return relativePos % spanCount == 0;
    }

    private boolean isLastInRow(int relativePos, int spanCount) {
        return isFirstInRow(relativePos + 1, spanCount);
    }

    private int getTotalSpanCount(RecyclerView parent) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return layoutManager instanceof GridLayoutManager
                ? ((GridLayoutManager) layoutManager).getSpanCount()
                : 1;
    }

    private boolean isProductItem(RecyclerView parent, int viewPosition) {
        final RecyclerView.Adapter adapter = parent.getAdapter();
        if (viewPosition < 0 || viewPosition > adapter.getItemCount() - 1) {
            return false;
        }
        final int viewType = adapter.getItemViewType(viewPosition);
        return allowedViewTypes.contains(viewType);
    }

    private boolean isAdsItem(RecyclerView parent, int viewPosition) {
        final RecyclerView.Adapter adapter = parent.getAdapter();
        if (viewPosition < 0 || viewPosition > adapter.getItemCount() - 1) {
            return false;
        }
        final int viewType = adapter.getItemViewType(viewPosition);
        return viewType == TopAdsViewHolder.LAYOUT;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        Paint paint = new Paint();
        paint.setColor(color);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int absolutePos = parent.getChildAdapterPosition(child);
            if (isProductItem(parent, absolutePos) || child == null) {
                canvas.drawRect(child.getLeft() - spacing, child.getTop() - spacing, child.getRight() + spacing, child.getTop(), paint);
                canvas.drawRect(child.getLeft() - spacing, child.getBottom(), child.getRight() + spacing, child.getBottom() + spacing, paint);
                canvas.drawRect(child.getLeft() - spacing, child.getTop(), child.getLeft(), child.getBottom(), paint);
                canvas.drawRect(child.getRight(), child.getTop(), child.getRight() + spacing, child.getBottom(), paint);
            }
        }
    }
}
