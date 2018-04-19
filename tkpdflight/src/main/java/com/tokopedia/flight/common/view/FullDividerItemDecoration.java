package com.tokopedia.flight.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.flight.R;

public class FullDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public FullDividerItemDecoration(Context context) {
        this(context, context.getResources().getDrawable(R.drawable.bg_flight_full_item_item_decoration));
    }

    public FullDividerItemDecoration(Context context, Drawable drawable) {
        mDivider = drawable;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
