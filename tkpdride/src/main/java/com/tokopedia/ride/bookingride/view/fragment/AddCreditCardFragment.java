package com.tokopedia.ride.bookingride.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.ride.R;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.view.AddCreditCardContract;
import com.tokopedia.ride.bookingride.view.AddCreditCardPresenter;
import com.tokopedia.ride.common.ride.di.RideComponent;

import javax.inject.Inject;

/**
 * Created by alvarisi on 4/25/17.
 */


public class AddCreditCardFragment extends BaseFragment implements AddCreditCardContract.View {

    @Inject
    AddCreditCardPresenter presenter;

    public static AddCreditCardFragment newInstance() {
        Bundle bundle = new Bundle();
        AddCreditCardFragment fragment = new AddCreditCardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void openWebView(String url);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    protected void initInjector() {
        RideComponent component = getComponent(RideComponent.class);
        BookingRideComponent bookingRideComponent = DaggerBookingRideComponent
                .builder()
                .rideComponent(component)
                .build();
        bookingRideComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_credit_card;
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
