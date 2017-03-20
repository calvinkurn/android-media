package com.tokopedia.ride.bookingride.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmBookingRideFragment extends BaseFragment {
    public static String EXTRA_PRODUCT = "EXTRA_PRODUCT";
    @BindView(R2.id.cabAppIcon)
    ImageView productIconImageView;
    @BindView(R2.id.topHeaderConfirmBooking)
    TextView headerTextView;
    @BindView(R2.id.tv_pool_price)
    TextView priceTextView;
    @BindView(R2.id.tv_seats)
    TextView seatsTextView;
    @BindView(R2.id.tvSurgeRate)
    TextView surgeRateTextView;
    @BindView(R2.id.cab_confirmation)
    TextView bookingConfirmationTextView;

    public ConfirmBookingRideFragment() {

    }

    public static ConfirmBookingRideFragment newInstance(Bundle bundle){
        ConfirmBookingRideFragment fragment = new ConfirmBookingRideFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_confirm_booking_ride;
    }

}
