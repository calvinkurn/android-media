package com.tokopedia.ride.bookingride.view.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.view.ManagePaymentOptionsContract;
import com.tokopedia.ride.bookingride.view.ManagePaymentOptionsPresenter;
import com.tokopedia.ride.bookingride.view.activity.EditDeleteCreditCardActivity;
import com.tokopedia.ride.bookingride.view.adapter.PaymentMethodAdapter;
import com.tokopedia.ride.bookingride.view.adapter.PaymentMethodItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.factory.PaymentMethodAdapterTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.factory.PaymentMethodTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.common.configuration.PaymentMode;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.scrooge.ScroogePGUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.ride.bookingride.view.activity.ManagePaymentOptionsActivity.TYPE_CHANGE_PAYMENT_OPTION;
import static com.tokopedia.ride.bookingride.view.activity.ManagePaymentOptionsActivity.TYPE_MANAGE_PAYMENT_OPTION;
import static com.tokopedia.ride.scrooge.ScroogePGUtil.REQUEST_CODE_OPEN_SCROOGE_PAGE;

/**
 * Created by Vishal
 */


public class ManagePaymentOptionsFragment extends BaseFragment implements ManagePaymentOptionsContract.View, PaymentMethodItemClickListener {


    private static final int REQUEST_CODE_EDIT_CARD_DETAIL = 100;
    private static final String KEY_TYPE = "KEY_TYPE";
    public static final String KEY_CHANGE_PAYMENT_RESULT = "KEY_CHANGE_PAYMENT_RESULT";

    @BindView(R2.id.payment_method_list)
    RecyclerView paymentMethodsRecyclerView;

    @BindView(R2.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    ManagePaymentOptionsPresenter presenter;

    private PaymentMethodAdapter paymentMethodAdapter;
    private int type = TYPE_MANAGE_PAYMENT_OPTION;
    private ProgressDialog progressDialog;

    public static ManagePaymentOptionsFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        ManagePaymentOptionsFragment fragment = new ManagePaymentOptionsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onPaymentMethodSelected(PaymentMethodViewModel paymentMethodViewModel) {

        if (type == TYPE_MANAGE_PAYMENT_OPTION && paymentMethodViewModel.getType().equalsIgnoreCase(PaymentMode.CC)) {
            startActivityForResult(EditDeleteCreditCardActivity.getCallingActivity(getActivity(), paymentMethodViewModel), REQUEST_CODE_EDIT_CARD_DETAIL);
        } else if (type == TYPE_CHANGE_PAYMENT_OPTION) {
            //set payment method and close the activity in result
            if (paymentMethodViewModel.isActive()) {
                closeActivity(paymentMethodViewModel);
            } else {
                presenter.selectPaymentOption(paymentMethodViewModel);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.type = getArguments().getInt(KEY_TYPE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

        init();
    }

    private void init() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.message_please_wait));
        progressDialog.setCancelable(false);

        //populate list of payment methods
        presenter.fetchPaymentMethodList();
    }

    @Override
    public void showErrorMessage(String message) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), message);
    }

    @Override
    public void renderPaymentMethodList(List<Visitable> visitables) {
        PaymentMethodTypeFactory paymentMethodTypeFactory = new PaymentMethodAdapterTypeFactory(this, (this.type == TYPE_MANAGE_PAYMENT_OPTION ? PaymentMethodAdapterTypeFactory.TYPE_MANAGE_PAYMENT : PaymentMethodAdapterTypeFactory.TYPE_CHOOSE_PAYMENT));
        paymentMethodAdapter = new PaymentMethodAdapter(paymentMethodTypeFactory);
        paymentMethodAdapter.setElement(visitables);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        paymentMethodsRecyclerView.setLayoutManager(layoutManager);
        paymentMethodsRecyclerView.setHasFixedSize(true);
        paymentMethodsRecyclerView.setAdapter(paymentMethodAdapter);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressBar() {
        if (getActivity() != null && progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressBar() {
        if (getActivity() != null && progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void closeActivity(PaymentMethodViewModel paymentMethodViewModel) {
        //close Activity
        Intent result = new Intent();
        result.putExtra(KEY_CHANGE_PAYMENT_RESULT, paymentMethodViewModel);
        getActivity().setResult(RESULT_OK, result);
        getActivity().finish();
    }

    @Override
    public void opeScroogePage(String saveUrl, boolean isPostReq, Bundle saveBody) {
        ScroogePGUtil.openScroogePage(this, saveUrl, isPostReq, saveBody, getString(R.string.toolbar_title_add_credit_card));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //referesh payment method list in case of success of delete card and add card
        if ((requestCode == REQUEST_CODE_EDIT_CARD_DETAIL && resultCode == Activity.RESULT_OK) ||
                (requestCode == REQUEST_CODE_OPEN_SCROOGE_PAGE)) {
            presenter.deletePaymentMethodCache();
            presenter.getPaymentMethodsFromCloud();
        }
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
        presenter.addCreditCard();
    }

    @Override
    public void showAutoDebitDialog(PaymentMethodViewModel paymentMethodViewModel) {
        DialogFragment dialogFragment = AutoDetbitConfirmationDialogFragment.newInstance(paymentMethodViewModel);
        dialogFragment.show(getFragmentManager().beginTransaction(), "auto_debit_dialog");
    }
}
