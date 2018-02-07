package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.adapter.FlightBookingNationalityAdapterTypeFactory;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPhoneCodePresenterImpl;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPhoneCodeView;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.util.FlightErrorUtil;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingNationalityFragment extends BaseSearchListFragment<FlightBookingPhoneCodeViewModel, FlightBookingNationalityAdapterTypeFactory> implements FlightBookingPhoneCodeView {

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
    public void loadData(int page) {
        flightBookingPhoneCodePresenter.getPhoneCodeList();
    }

    @Override
    protected FlightBookingNationalityAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightBookingNationalityAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_COUNTRY, flightBookingPhoneCodeViewModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }


    @Override
    public void onSearchSubmitted(String text) {
        flightBookingPhoneCodePresenter.getPhoneCodeList(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        flightBookingPhoneCodePresenter.getPhoneCodeList(text);
    }

    @Override
    protected String getMessageFromThrowable(Context context, Throwable t) {
        return FlightErrorUtil.getMessageFromException(context, t);
    }
}
