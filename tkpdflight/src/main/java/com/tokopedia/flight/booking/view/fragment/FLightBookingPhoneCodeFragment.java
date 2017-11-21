package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.adapter.FlightBookingPhoneCodeAdapter;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPhoneCodePresenterImpl;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPhoneCodeView;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FLightBookingPhoneCodeFragment extends BaseSearchListFragment<FlightBookingPhoneCodeViewModel> implements
        FlightBookingPhoneCodeView, BaseListAdapter.OnBaseListV2AdapterListener<FlightBookingPhoneCodeViewModel> {

    public static final String EXTRA_SELECTED_PHONE_CODE = "EXTRA_SELECTED_PHONE_CODE";

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
        return new FlightBookingPhoneCodeAdapter(this);
    }

    @Override
    public void onItemClicked(FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_PHONE_CODE, flightBookingPhoneCodeViewModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        flightBookingPhoneCodePresenter.getPhoneCodeList();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flightBookingPhoneCodePresenter.attachView(this);
    }

}
