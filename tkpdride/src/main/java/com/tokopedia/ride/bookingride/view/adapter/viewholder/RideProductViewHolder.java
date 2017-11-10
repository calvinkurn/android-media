package com.tokopedia.ride.bookingride.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.analytics.RideGATracking;
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
    @BindView(R2.id.cab_price_layout)
    LinearLayout priceEstimateContainerLinearLayout;
    @BindView(R2.id.price_progressbar)
    ProgressBar priceProgress;
    @BindView(R2.id.progress_bar_time)
    ProgressBar timeProgress;

    private RideProductItemClickListener itemClickListener;
    private RideProductViewModel rideProductViewModel;
    private Context context;

    public RideProductViewHolder(View parent, RideProductItemClickListener itemClickListener) {
        super(parent);
        context = parent.getContext();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void bind(RideProductViewModel element) {
        rideProductViewModel = element;
        productNameTextView.setText(element.getProductName());
        surgePriceImageView.setVisibility(element.isSurgePrice() ? View.VISIBLE : View.GONE);

        priceEstimateContainerLinearLayout.setVisibility(element.isDestinationSelected() ? View.VISIBLE : View.GONE);
        priceProgress.setVisibility(element.getProductPriceFmt() == null ? View.VISIBLE : View.GONE);
        productPriceTextView.setText(String.valueOf(element.getProductPriceFmt()));
        productPriceTextView.setVisibility(element.getProductPriceFmt() == null ? View.GONE : View.VISIBLE);

        if (element.getTimeEstimate() != null) {
            timeEstimateTextView.setText(String.valueOf(element.getTimeEstimate()));
            timeEstimateTextView.setVisibility(View.VISIBLE);
            timeProgress.setVisibility(View.GONE);
        } else {
            timeEstimateTextView.setVisibility(View.GONE);
            timeProgress.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(element.getProductImage())
                .asBitmap()
                .fitCenter()
                .dontAnimate()
                .error(R.drawable.cabs_uber_ic)
                .into(productImageImageView);
    }

    @OnClick(R2.id.row_cab_list)
    public void actionOnProductClicked() {
        RideGATracking.eventSelectRideOption(rideProductViewModel.getProductName(),rideProductViewModel.getTimeEstimate(),rideProductViewModel.getProductPriceFmt());

        itemClickListener.onProductSelected(rideProductViewModel);
    }
}
