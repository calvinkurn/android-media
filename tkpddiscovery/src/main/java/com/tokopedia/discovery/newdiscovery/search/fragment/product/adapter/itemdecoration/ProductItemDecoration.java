package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.discovery.R;

import java.util.Arrays;
import java.util.List;

public class ProductItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacing;

    private final List<Integer> allowedViewTypes = Arrays.asList(
            R.layout.search_result_product_item_big_grid,
            R.layout.search_result_product_item_grid,
            R.layout.search_result_product_item_list);

    public ProductItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        if (!isProductItem(parent, position)) {
            return;
        }

        final int totalSpanCount = getTotalSpanCount(parent);
        int spanSize = getItemSpanSize(parent, position);
        if (totalSpanCount == spanSize) {
            return;
        }

        outRect.top = isTopProductItem(parent, position, totalSpanCount) ? spacing : spacing / 2;
        outRect.left = isFirstInRow(position, totalSpanCount) ? spacing : spacing / 2;
        outRect.right = isLastInRow(position, totalSpanCount) ? spacing : spacing / 2;
        outRect.bottom = isBottomProductItem(parent, position, totalSpanCount) ? spacing : spacing / 2;
    }

    private boolean isTopProductItem(RecyclerView parent, int position, int totalSpanCount) {
        return !isProductItem(parent, position - position % totalSpanCount - 1);
    }

    private boolean isBottomProductItem(RecyclerView parent, int position, int totalSpanCount) {
        return !isProductItem(parent, position + totalSpanCount - position % totalSpanCount);
    }

    private boolean isFirstInRow(int position, int spanCount) {
        return position % spanCount == 0;
    }

    private boolean isLastInRow(int position, int spanCount) {
        return isFirstInRow(position + 1, spanCount);
    }

    private int getTotalSpanCount(RecyclerView parent) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return layoutManager instanceof GridLayoutManager
                ? ((GridLayoutManager) layoutManager).getSpanCount()
                : 1;
    }

    private int getItemSpanSize(RecyclerView parent, int position) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return layoutManager instanceof GridLayoutManager
                ? ((GridLayoutManager) layoutManager).getSpanSizeLookup().getSpanSize(position)
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
}
