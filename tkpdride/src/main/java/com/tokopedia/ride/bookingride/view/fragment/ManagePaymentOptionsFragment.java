package com.tokopedia.ride.bookingride.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.view.ManagePaymentOptionsContract;
import com.tokopedia.ride.bookingride.view.ManagePaymentOptionsPresenter;
import com.tokopedia.ride.bookingride.view.activity.AddCreditCardActivity;
import com.tokopedia.ride.bookingride.view.adapter.PaymentMethodAdapter;
import com.tokopedia.ride.bookingride.view.adapter.PaymentMethodItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.factory.PaymentMethodAdapterTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.factory.PaymentMethodTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.common.ride.di.RideComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by alvarisi on 4/25/17.
 */


public class ManagePaymentOptionsFragment extends BaseFragment implements ManagePaymentOptionsContract.View, PaymentMethodItemClickListener {

    @BindView(R2.id.payment_method_list)
    RecyclerView paymentMethodsRecyclerView;


    @Inject
    ManagePaymentOptionsPresenter presenter;

    private PaymentMethodAdapter paymentMethodAdapter;

    public static ManagePaymentOptionsFragment newInstance() {
        Bundle bundle = new Bundle();
        ManagePaymentOptionsFragment fragment = new ManagePaymentOptionsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onPaymentMethodSelected(PaymentMethodViewModel paymentMethodViewModel) {

    }

    public interface OnFragmentInteractionListener {
        void openWebView(String url);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

        init();
    }

    private void init() {
        //populate list of payment methods
        List<Visitable> paymentMethods = new ArrayList<>();
        PaymentMethodViewModel paymentMethodViewModel1 = new PaymentMethodViewModel("TokoCash", true, "");
        PaymentMethodViewModel paymentMethodViewModel2 = new PaymentMethodViewModel("4355 **** ****", true, "");
        paymentMethods.add(paymentMethodViewModel1);
        paymentMethods.add(paymentMethodViewModel2);


        PaymentMethodTypeFactory paymentMethodTypeFactory = new PaymentMethodAdapterTypeFactory(this);
        paymentMethodAdapter = new PaymentMethodAdapter(paymentMethodTypeFactory);
        paymentMethodAdapter.setElement(paymentMethods);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        paymentMethodsRecyclerView.setLayoutManager(layoutManager);
        paymentMethodsRecyclerView.setHasFixedSize(true);
        paymentMethodsRecyclerView.setAdapter(paymentMethodAdapter);
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
        return R.layout.fragment_manage_payment_option;
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @OnClick(R2.id.layout_add_credit_card)
    public void actionAddCreditCard() {
        startActivity(AddCreditCardActivity.getCallingActivity(getActivity()));
    }
}
