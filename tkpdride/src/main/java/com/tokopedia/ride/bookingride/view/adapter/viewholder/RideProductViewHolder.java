package com.tokopedia.ride.bookingride.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.bookingride.view.adapter.RideProductItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductViewHolder extends AbstractViewHolder<RideProductViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.ride_product_list_item;

    @BindView(R2.id.iv_cab_img)
    ImageView productImageImageView;
    @BindView(R2.id.tv_cab_name)
    TextView productNameTextView;
    @BindView(R2.id.tv_cab_time)
    TextView timeEstimateTextView;
    @BindView(R2.id.iv_cab_surge)
    ImageView surgePriceImageView;
    @BindView(R2.id.tv_cab_price)
    TextView productPriceTextView;
    @BindView(R2.id.tv_cab_base_fare)
    TextView baseFareTextView;
    @BindView(R2.id.cab_price_estimate_layout)
    FrameLayout priceEstimateContainerFrameLayout;

    RideProductItemClickListener mItemClickListener;
    RideProductViewModel mRideProductViewModel;
    Context mContext;

    public RideProductViewHolder(View parent, RideProductItemClickListener itemClickListener) {
        super(parent);
        mContext = parent.getContext();
        mItemClickListener = itemClickListener;
    }

    @Override
    public void bind(RideProductViewModel element) {
        mRideProductViewModel = element;
        productNameTextView.setText(element.getProductName());
        timeEstimateTextView.setText(String.valueOf(element.getTimeEstimate()));
        if (element.isSurgePrice()) {
            surgePriceImageView.setVisibility(View.VISIBLE);
        } else {
            surgePriceImageView.setVisibility(View.GONE);
        }
        productPriceTextView.setText(String.valueOf(element.getProductPrice()));
        baseFareTextView.setText(String.valueOf(element.getBaseFare()));
        Glide.with(mContext).load(element.getProductImage())
                .asBitmap()
                .fitCenter()
                .dontAnimate()
                .error(R.mipmap.ic_launcher)
                .into(productImageImageView);
    }

    @OnClick(R2.id.row_cab_list)
    public void actionOnProductClicked() {
        mItemClickListener.onProductSelected(mRideProductViewModel);
    }
}
