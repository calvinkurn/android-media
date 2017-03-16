package com.tokopedia.otp.phoneverification.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.session.R;

/**
 * Created by nisie on 2/27/17.
 */

public class PhoneVerificationActivationFragment extends BasePresenterFragment {
    TextView protectAccountText;

    public static PhoneVerificationActivationFragment createInstance() {
        return new PhoneVerificationActivationFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
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
        return R.layout.fragment_phone_verification_activation;
    }

    @Override
    protected void initView(View view) {
        protectAccountText = (TextView) view.findViewById(R.id.protect_account_text);

        Spannable spannable = new SpannableString(getString(R.string.protect_your_account_with_phone_verification));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setFakeBoldText(true);
                              }
                          }
                , getString(R.string.protect_your_account_with_phone_verification).indexOf("melakukan verifikasi nomor ponsel")
                , getString(R.string.protect_your_account_with_phone_verification).length()
                , 0);

        protectAccountText.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
