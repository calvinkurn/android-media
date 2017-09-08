package com.tokopedia.seller.goldmerchant.featured.view.adapter.viewholder;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.featured.helper.ItemTouchHelperViewHolder;
import com.tokopedia.seller.goldmerchant.featured.helper.OnStartDragListener;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.model.FeaturedProductModel;

/**
 * Created by normansyahputa on 9/8/17.
 */

public class FeaturedProductViewHolder extends RecyclerView.ViewHolder implements
        ItemTouchHelperViewHolder {
    AppCompatImageView dragAndDropGMFeaturedProduct;
    private OnStartDragListener mDragStartListener;

    public FeaturedProductViewHolder(View itemView, OnStartDragListener mDragStartListener) {
        super(itemView);

        dragAndDropGMFeaturedProduct = (AppCompatImageView) itemView.findViewById(R.id.ic_drag_and_drop_gm_featured_product);
        this.mDragStartListener = mDragStartListener;
    }

    public void bindData(FeaturedProductModel featuredProductModel) {
        dragAndDropGMFeaturedProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(FeaturedProductViewHolder.this);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
