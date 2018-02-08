package com.tokopedia.ride.bookingride.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.bookingride.view.adapter.ItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PlaceAutoCompeleteViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by alvarisi on 3/15/17.
 */

public class PlaceAutoCompleteViewHolder extends AbstractViewHolder<PlaceAutoCompeleteViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.place_auto_complete_list_item;

    @BindView(R2.id.place_title)
    TextView mPlaceTitleTextView;
    @BindView(R2.id.place_address)
    TextView mPlaceAddressTextView;
    @BindView(R2.id.indicator)
    ImageView mIndicator;
    @BindView(R2.id.tv_distance)
    TextView mDistanceTextView;

    private final ItemClickListener mItemClickListener;
    private PlaceAutoCompeleteViewModel mItem;

    public PlaceAutoCompleteViewHolder(View itemView, ItemClickListener mItemClickListener) {
        super(itemView);
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void bind(PlaceAutoCompeleteViewModel element) {
        mPlaceTitleTextView.setText(MethodChecker.fromHtml(element.getTitle()));
        mPlaceAddressTextView.setText(Html.fromHtml(element.getAddress()));
        if (element.getType() == PlaceAutoCompeleteViewModel.TYPE.MARKETPLACE_PLACE) {
            mIndicator.setImageResource(R.drawable.location);
        } else {
            mIndicator.setImageResource(R.drawable.location);
        }

        if (!TextUtils.isEmpty(element.getDistance())) {
            mDistanceTextView.setText(element.getDistance());
            mDistanceTextView.setVisibility(View.VISIBLE);
        } else {
            mDistanceTextView.setVisibility(View.GONE);
        }

        mItem = element;
    }

    @OnClick(R2.id.place_container)
    public void actionPlaceClicked() {
        mItemClickListener.onPlaceSelected(mItem);
    }
}
