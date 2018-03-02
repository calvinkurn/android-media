package com.tokopedia.tkpdstream.chatroom.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author by StevenFredian on 27/02/18.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int space;
    private boolean includeEdge;

    public SpaceItemDecoration(int verticalSpaceHeight) {
        this.space = verticalSpaceHeight;
        spanCount = 0;
        includeEdge = true;
    }

    public SpaceItemDecoration(int verticalSpaceHeight, boolean includeEdge) {
        this.space = verticalSpaceHeight;
        spanCount = 0;
        this.includeEdge = includeEdge;
    }

    public SpaceItemDecoration(int dimension, int spanCount) {
        this.space = dimension;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (spanCount == 0) {
            if(includeEdge){
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = space;
                }else if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1){
                    outRect.bottom = space;
                }else {
                    outRect.top = space/2;
                    outRect.bottom = space/2;
                }
            }else {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = space;
                }
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