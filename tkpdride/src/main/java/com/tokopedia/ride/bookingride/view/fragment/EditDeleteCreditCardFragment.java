package com.tokopedia.ride.bookingride.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.view.AddCreditCardContract;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.scrooge.ScroogePGUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.ride.bookingride.view.activity.EditDeleteCreditCardActivity.KEY_PAYMENT_METHOD_VIEW_MODEL;

/**
 * Created by sandeepgoyal on 28/09/17.
 */

public class EditDeleteCreditCardFragment extends BaseFragment implements AddCreditCardContract.View {


    @BindView(R2.id.txt_card_number)
    TextView txtCardNumber;
    @BindView(R2.id.txt_card_expiration)
    TextView txtCardExpiration;
    private PaymentMethodViewModel paymentMethodViewModel;

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
        paymentMethodViewModel = getArguments().getParcelable(KEY_PAYMENT_METHOD_VIEW_MODEL);

        txtCardNumber.setText(paymentMethodViewModel.getName());
        txtCardExpiration.setText(String.format("%s/%s", paymentMethodViewModel.getExpiryMonth(), paymentMethodViewModel.getExpiryYear()));
    }

    @OnClick(R2.id.btn_delete)
    public void actionDestinationButtonClicked() {
        Bundle removeParams = paymentMethodViewModel.getDeleteBody();
        if (removeParams != null) {
            //add auth param
            String oAuthString = "Bearer " + SessionHandler.getAccessToken();
            removeParams.putString("Authorization", oAuthString);
        }

        ScroogePGUtil.openScroogePage(getActivity(), paymentMethodViewModel.getDeleteUrl(), false, removeParams);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ScroogePGUtil.RESULT_CODE_DELETE_CC_SUCCESS) {
            //show success message
            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

        } else if (requestCode == ScroogePGUtil.RESULT_CODE_DELETE_CC_FAIL) {
            //show success message
            Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_delete_credit_card;
    }
}