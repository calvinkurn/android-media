package com.tokopedia.ride.bookingride.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.view.EditCardDetailContract;
import com.tokopedia.ride.bookingride.view.EditCardDetailPresenter;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.scrooge.ScroogePGUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.ride.bookingride.view.activity.EditDeleteCreditCardActivity.KEY_PAYMENT_METHOD_VIEW_MODEL;

/**
 * Created by sandeepgoyal on 28/09/17.
 */

public class EditDeleteCreditCardFragment extends BaseFragment implements EditCardDetailContract.View {


    @BindView(R2.id.txt_card_number)
    TextView txtCardNumber;
    @BindView(R2.id.txt_card_expiration)
    TextView txtCardExpiration;
    private PaymentMethodViewModel paymentMethodViewModel;

    private ProgressDialog progressDialog;

    @Inject
    EditCardDetailPresenter presenter;

    public static EditDeleteCreditCardFragment newInstance(PaymentMethodViewModel paymentMethodViewModel) {
        Bundle bundle = new Bundle();
        EditDeleteCreditCardFragment fragment = new EditDeleteCreditCardFragment();
        bundle.putParcelable(KEY_PAYMENT_METHOD_VIEW_MODEL, paymentMethodViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    public interface OnFragmentInteractionListener {
        void openWebView(String url);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

        paymentMethodViewModel = getArguments().getParcelable(KEY_PAYMENT_METHOD_VIEW_MODEL);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.message_please_wait));

        txtCardNumber.setText(paymentMethodViewModel.getName());
        txtCardExpiration.setText(String.format("%s/%s", paymentMethodViewModel.getExpiryMonth(), paymentMethodViewModel.getExpiryYear()));
    }

    @OnClick(R2.id.btn_delete)
    public void actionDestinationButtonClicked() {
        presenter.deleteCard(paymentMethodViewModel);
    }

    @Override
    public void showProgress() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void showErrorMessage(String message) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), message);
    }

    @Override
    public void closeActivity() {
        if (getActivity() != null) {
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ScroogePGUtil.RESULT_CODE_DELETE_CC_SUCCESS) {
            //show success message
            closeActivity();

        } else if (requestCode == ScroogePGUtil.RESULT_CODE_DELETE_CC_FAIL) {
            //show success message
            showErrorMessage(getString(R.string.error_message_delete_cc_fail));
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
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_delete_credit_card;
    }
}