package com.tokopedia.otp.phoneverification.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.session.R;

/**
 * Created by nisie on 2/27/17.
 */

public class PhoneVerificationActivationFragment extends TkpdBaseV4Fragment {
    TextView protectAccountText;

    public static PhoneVerificationActivationFragment createInstance() {
        return new PhoneVerificationActivationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_verification_activation, parent, false);
        protectAccountText = (TextView) view.findViewById(R.id.protect_account_text);
        prepareView();
        return view;

    }

    private void prepareView() {
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
    protected String getScreenName() {
        return AppScreen.SCREEN_PHONE_VERIFICATION;
    }
}
