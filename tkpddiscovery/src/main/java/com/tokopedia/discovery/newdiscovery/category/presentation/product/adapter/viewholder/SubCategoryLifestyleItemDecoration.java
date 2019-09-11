package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

class SubCategoryLifestyleItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public SubCategoryLifestyleItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            if (layoutManager.getOrientation() == GridLayoutManager.HORIZONTAL) {
                drawDividerGridHorizontal(position, layoutManager.getSpanCount(), outRect, parent);
            } else {
                drawEqualDividerGrid(position, layoutManager.getSpanCount(), outRect, parent);
            }
        }

        if (parent.getLayoutManager() instanceof LinearLayoutManager &&
                !(parent.getLayoutManager() instanceof GridLayoutManager)) {
            drawDividerLinearHorizontal(position, outRect, parent);
        }
    }

    private void drawDividerLinearHorizontal(int position, Rect outRect, RecyclerView parent) {
        if (position == 0) {
            outRect.left = spacing;
            outRect.right = spacing / 2;
        } else if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.left = spacing / 2;
            outRect.right = spacing;
        } else {
            outRect.left = spacing / 2;
            outRect.right = spacing / 2;
        }

        outRect.top = spacing;
        outRect.bottom = spacing;
    }

    private void drawEqualDividerGrid(int position, int spanCount, Rect outRect, RecyclerView parent) {
        int column = position % spanCount;

        outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
        outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

        outRect.top = spacing;
        outRect.bottom = spacing; // item bottom
    }

    private void drawDividerGridHorizontal(int position, int spanCount, Rect outRect, RecyclerView parent) {
        if (position < spanCount) {
            // left edge
            outRect.left = spacing;
            outRect.right = spacing / 2;
        } else {
            int positionEndEdge = parent.getAdapter().getItemCount() % spanCount;
            if (positionEndEdge == 0 && position >= parent.getAdapter().getItemCount() - spanCount) {
                // right edge even modulus
                outRect.left = spacing / 2;
                outRect.right = spacing;
            } else if (positionEndEdge > 0 && position >= parent.getAdapter().getItemCount() - positionEndEdge) {
                // right edge odd modulus
                outRect.left = spacing / 2;
                outRect.right = spacing;
            } else {
                outRect.left = spacing / 2;
                outRect.right = spacing / 2;
            }
        }

        if (position % spanCount == 0) {
            outRect.top = spacing;
            outRect.bottom = spacing / 2;
        } else {
            outRect.top = spacing / 2;
            outRect.bottom = spacing;
        }
    }
}
