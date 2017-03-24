package com.tokopedia.ride.bookingride.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.view.ConfirmBookingContract;
import com.tokopedia.ride.bookingride.view.ConfirmBookingPresenter;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.SeatViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmBookingRideFragment extends BaseFragment implements ConfirmBookingContract.View {
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
    @BindView(R2.id.cab_select_seat)
    LinearLayout selectSeatContainer;
    ConfirmBookingContract.Presenter presenter;
    OnFragmentInteractionListener mListener;
    ConfirmBookingViewModel confirmBookingViewModel;


    public interface OnFragmentInteractionListener {
        void actionChangeSeatCount(List<SeatViewModel> seatViewModels);

        void actionRequestRide(ConfirmBookingViewModel confirmBookingViewModel);
    }

    public ConfirmBookingRideFragment() {

    }

    public static ConfirmBookingRideFragment newInstance(Bundle bundle) {
        ConfirmBookingRideFragment fragment = new ConfirmBookingRideFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_confirm_booking_ride;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ConfirmBookingPresenter();
        presenter.attachView(this);
        confirmBookingViewModel = getArguments().getParcelable(EXTRA_PRODUCT);
        setViewListener();
    }

    private void setViewListener() {
        Glide.with(getActivity()).load(confirmBookingViewModel.getProductImage())
                .asBitmap()
                .fitCenter()
                .dontAnimate()
                .error(R.mipmap.ic_launcher)
                .into(productIconImageView);
        headerTextView.setText(confirmBookingViewModel.getHeaderTitle());
        priceTextView.setText(confirmBookingViewModel.getPrice());
//        seatsTextView.setText(confirmBookingViewModel.getSeatCount());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @OnClick(R2.id.cab_select_seat)
    public void actionSelectSeatButtonClicked() {
        if (confirmBookingViewModel.getMaxCapacity() > 1) {
            List<SeatViewModel> seatViewModels = new ArrayList<>();
            for (int i = 1; i <= confirmBookingViewModel.getMaxCapacity(); i++) {
                String seatTitle = null;
                if (i == 1) {
                    seatTitle = String.format("%d Seat", i);
                } else {
                    seatTitle = String.format("%d Seats", i);
                }
                seatViewModels.add(new SeatViewModel(i, seatTitle));
            }
            mListener.actionChangeSeatCount(seatViewModels);
        } else {
            Toast.makeText(getActivity(), "Capacity 1, cant change seat", Toast.LENGTH_LONG).show();
        }
    }

    public void updateSeatCount(int seat) {
        seatsTextView.setText(String.valueOf(seat));
    }

    @OnClick(R2.id.cab_confirmation)
    public void actionConfirmButtonClicked(){
        mListener.actionRequestRide(confirmBookingViewModel);
    }
}
