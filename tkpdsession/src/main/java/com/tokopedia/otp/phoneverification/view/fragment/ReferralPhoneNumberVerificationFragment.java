package com.tokopedia.otp.phoneverification.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.view.activity.ChangePhoneNumberActivity;
import com.tokopedia.session.R;

/**
 * Created by ashwanityagi on 12/12/17.
 */

public class ReferralPhoneNumberVerificationFragment extends BasePresenterFragment {

    public interface ReferralPhoneNumberVerificationFragmentListener {
        void onSkipVerification();

        void onClickVerification(String phoneNumber);
    }


    EditText tvPhoneNumber;
    TextView btnActivation;
    private ReferralPhoneNumberVerificationFragmentListener listener;
    private SessionHandler sessionHandler;

    public static ReferralPhoneNumberVerificationFragment newInstance() {
        ReferralPhoneNumberVerificationFragment fragment = new ReferralPhoneNumberVerificationFragment();
        return fragment;
    }

    public static ReferralPhoneNumberVerificationFragment createInstance(ReferralPhoneNumberVerificationFragmentListener listener) {
        ReferralPhoneNumberVerificationFragment fragment = new ReferralPhoneNumberVerificationFragment();
        fragment.setReferralPhoneVerificationListener(listener);
        return fragment;
    }

    public void setReferralPhoneVerificationListener(ReferralPhoneNumberVerificationFragmentListener listener) {
        this.listener = listener;
    }

    public ReferralPhoneNumberVerificationFragmentListener getListener() {
        return listener;
    }


    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void onFirstTimeLaunched() {
        if(sessionHandler == null){
            sessionHandler = new SessionHandler(getActivity());
        }
        tvPhoneNumber.setText(CustomPhoneNumberUtil.transform(
                sessionHandler.getPhoneNumber()));
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_referral_phone_number_verification;
    }


    @Override
    protected void initView(View view) {
        if(sessionHandler == null){
            sessionHandler = new SessionHandler(getActivity());
        }
        tvPhoneNumber = (EditText) view.findViewById(R.id.tv_phone_number);
        tvPhoneNumber.setText(CustomPhoneNumberUtil.transform(
                sessionHandler.getPhoneNumber()));
        btnActivation = (TextView) view.findViewById(R.id.btn_activation);
        setViewListener();
        tvPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        ChangePhoneNumberActivity.getChangePhoneNumberIntent(
                                getActivity(),
                                tvPhoneNumber.getText().toString()),
                        ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER);
            }
        });
    }

    @Override
    protected void setViewListener() {
        btnActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_VERIFY_NUMBER,tvPhoneNumber.getText().toString().replace("-", ""));

                    listener.onClickVerification(tvPhoneNumber.getText().toString());
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER &&
                resultCode == Activity.RESULT_OK) {
            tvPhoneNumber.setText(data.getStringExtra(ChangePhoneNumberFragment.EXTRA_PHONE_NUMBER));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

}