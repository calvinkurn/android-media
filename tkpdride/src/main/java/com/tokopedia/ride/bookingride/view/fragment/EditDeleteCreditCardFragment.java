package com.tokopedia.ride.bookingride.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
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
import com.tokopedia.ride.bookingride.view.activity.EditDeleteCreditCardActivity;
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
    @BindView(R2.id.layout_confirm_dialog)
    View confirmLayoutDialog;
    @BindView(R2.id.btn_allow_auto_debit)
    TextView autoDebitButton;

    private PaymentMethodViewModel paymentMethodViewModel;
    private ProgressDialog progressDialog;

    @Inject
    EditCardDetailPresenter presenter;
    private boolean isConfirmDialogShown;
    private BottomSheetBehavior mBottomSheetBehavior;

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

    public EditDeleteCreditCardActivity.BackButtonListener getBackButtonListener() {
        return new EditDeleteCreditCardActivity.BackButtonListener() {
            @Override
            public void onBackPressed() {
                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public boolean canGoBack() {
                return (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED);
            }
        };
    }

    public interface OnFragmentInteractionListener {
        void openWebView(String url);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

        paymentMethodViewModel = getArguments().getParcelable(KEY_PAYMENT_METHOD_VIEW_MODEL);

        mBottomSheetBehavior = BottomSheetBehavior.from(confirmLayoutDialog);
        mBottomSheetBehavior.setPeekHeight(0);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.message_please_wait));

        txtCardNumber.setText(paymentMethodViewModel.getName());
        txtCardExpiration.setText(String.format("%s/%s", paymentMethodViewModel.getExpiryMonth(), paymentMethodViewModel.getExpiryYear()));

        autoDebitButton.setVisibility(paymentMethodViewModel.isSaveWebView() ? View.VISIBLE : View.GONE);
    }

    @OnClick(R2.id.btn_delete)
    public void actionDeleteButtonClicked() {
        showConfrimDialog();
    }

    @OnClick(R2.id.btn_dialog_cancel)
    public void actionDialogCancelButtonClicked() {
        hideConfirmDialog();
    }

    @OnClick(R2.id.btn_dialog_delete)
    public void actionDialogDeleteButtonClicked() {
        hideConfirmDialog();
        presenter.deleteCard(paymentMethodViewModel);
    }

    @OnClick(R2.id.btn_allow_auto_debit)
    public void actionAutoDebitButtonClicked() {
        ScroogePGUtil.openScroogePage(this, paymentMethodViewModel.getSaveurl(), true, paymentMethodViewModel.getSaveBody(), getString(R.string.toolbar_title_auto_debit));
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

        if (requestCode == ScroogePGUtil.REQUEST_CODE_OPEN_SCROOGE_PAGE) {
            if (resultCode == ScroogePGUtil.RESULT_CODE_DELETE_CC_FAIL) {
                //show success message
                showErrorMessage(getString(R.string.error_message_delete_cc_fail));
            } else {
                closeActivity();
            }
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

    public void hideConfirmDialog() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void showConfrimDialog() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}