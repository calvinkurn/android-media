package com.tokopedia.ride.bookingride.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
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

    private final ItemClickListener mItemClickListener;
    private PlaceAutoCompeleteViewModel mItem;

    public PlaceAutoCompleteViewHolder(View itemView, ItemClickListener mItemClickListener) {
        super(itemView);
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void bind(PlaceAutoCompeleteViewModel element) {
        mPlaceTitleTextView.setText(element.getTitle());
        mPlaceAddressTextView.setText(element.getAddress());
        if (element.getType() == PlaceAutoCompeleteViewModel.TYPE.MARKETPLACE_PLACE) {
            mIndicator.setImageResource(R.drawable.ic_history_black_24dp);
        } else {
            mIndicator.setImageResource(R.drawable.ic_place_black_24dp);
        }
        mItem = element;
    }

    @OnClick(R2.id.place_container)
    public void actionPlaceClicked() {
        mItemClickListener.onPlaceSelected(mItem);
    }
}
