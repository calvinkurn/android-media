package com.tokopedia.ride.bookingride.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.view.AddCreditCardContract;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tokopedia.ride.bookingride.view.activity.EditDeleteCreditCardActivity.CARD_DETIALS_KEY;

/**
 * Created by sandeepgoyal on 28/09/17.
 */

public class EditDeleteCreditCardFragment extends BaseFragment implements AddCreditCardContract.View {


    @BindView(R2.id.txt_card_number)
    TextView txtCardNumber;
    @BindView(R2.id.txt_card_expiration)
    TextView txtCardExpiration;

    public static EditDeleteCreditCardFragment newInstance(PaymentMethodViewModel paymentMethodViewModel) {
        Bundle bundle = new Bundle();
        EditDeleteCreditCardFragment fragment = new EditDeleteCreditCardFragment();
        bundle.putParcelable(CARD_DETIALS_KEY, paymentMethodViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    public interface OnFragmentInteractionListener {
        void openWebView(String url);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PaymentMethodViewModel paymentMethodViewModel = getArguments().getParcelable(CARD_DETIALS_KEY);

        //TODO Need to add other details as PaymentMethodViewModel Class will update
        txtCardNumber.setText(paymentMethodViewModel.getName());

    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_delete_credit_card;
    }


}