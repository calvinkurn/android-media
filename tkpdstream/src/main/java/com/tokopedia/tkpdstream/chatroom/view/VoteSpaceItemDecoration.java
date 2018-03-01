package com.tokopedia.tkpdstream.chatroom.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author by StevenFredian on 27/02/18.
 */

public class VoteSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int space;

    public VoteSpaceItemDecoration(int verticalSpaceHeight) {
        this.space = verticalSpaceHeight;
        spanCount = 0;
    }

    public VoteSpaceItemDecoration(int dimension, int spanCount) {
        this.space = dimension;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (spanCount == 0) {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.top = space;
            }
        }else {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.right = space/2;
            }else if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1){
                outRect.left = space/2;
            }else {
                outRect.right = space/2;
                outRect.left = space/2;
            }
        }
    }
}