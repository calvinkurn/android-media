package com.tokopedia.seller.goldmerchant.featured.view.adapter.viewholder;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.featured.helper.ItemTouchHelperViewHolder;
import com.tokopedia.seller.goldmerchant.featured.helper.OnStartDragListener;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.model.FeaturedProductModel;

/**
 * Created by normansyahputa on 9/8/17.
 */

public class FeaturedProductViewHolder extends RecyclerView.ViewHolder implements
        ItemTouchHelperViewHolder {
    private final AppCompatImageView icGMFeaturedProduct;
    private final TextView textProductName;
    private final TextView textPrice;
    private final AppCompatCheckBox checkBoxGM;
    AppCompatImageView dragAndDropGMFeaturedProduct;
    private OnStartDragListener mDragStartListener;
    private PostDataListener useCaseListener;

    public FeaturedProductViewHolder(View itemView, OnStartDragListener mDragStartListener, PostDataListener useCaseListener) {
        super(itemView);

        dragAndDropGMFeaturedProduct = (AppCompatImageView) itemView.findViewById(R.id.ic_drag_and_drop_gm_featured_product);
        icGMFeaturedProduct = (AppCompatImageView) itemView.findViewById(R.id.ic_gm_featured_product);
        textProductName = (TextView) itemView.findViewById(R.id.text_product_name_gm_featured_product);
        textPrice = (TextView) itemView.findViewById(R.id.text_price_gm_featured_product);
        checkBoxGM = (AppCompatCheckBox) itemView.findViewById(R.id.check_box_gm_featured_product);
        this.mDragStartListener = mDragStartListener;
        this.useCaseListener = useCaseListener;
    }

    public void bindData(FeaturedProductModel featuredProductModel) {
        textProductName.setText(featuredProductModel.getProductName());

        textPrice.setText(featuredProductModel.getProductPrice());

        ImageHandler.LoadImage(icGMFeaturedProduct, featuredProductModel.getImageUrl());

        dragAndDropGMFeaturedProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(FeaturedProductViewHolder.this);
                }
                return false;
            }
        });

        // TODO check box
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {
        if (useCaseListener != null) {
            useCaseListener.postData();
        }
    }

    public interface PostDataListener {
        void postData();
    }
}
