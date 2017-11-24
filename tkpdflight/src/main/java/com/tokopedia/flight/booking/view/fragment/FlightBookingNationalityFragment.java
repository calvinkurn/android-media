package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.adapter.FlightBookingNationalityAdapter;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPhoneCodePresenterImpl;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPhoneCodeView;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingNationalityFragment extends BaseSearchListFragment<FlightBookingPhoneCodeViewModel> implements
        FlightBookingPhoneCodeView, BaseListAdapter.OnBaseListV2AdapterListener<FlightBookingPhoneCodeViewModel> {

    public static final String EXTRA_SELECTED_COUNTRY = "EXTRA_SELECTED_COUNTRY";

    @Inject
    FlightBookingPhoneCodePresenterImpl flightBookingPhoneCodePresenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightBookingComponent.class)
                .inject(this);
    }

    @Override
    protected BaseListAdapter<FlightBookingPhoneCodeViewModel> getNewAdapter() {
        return new FlightBookingNationalityAdapter(getContext(), this);
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        showLoading();
        flightBookingPhoneCodePresenter.getPhoneCodeList();
    }

    @Override
    public void onItemClicked(FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_COUNTRY, flightBookingPhoneCodeViewModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }



}
