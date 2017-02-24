package com.tokopedia.otp.phoneverification.activity;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.otp.phoneverification.fragment.PhoneVerificationFragment;
import com.tokopedia.session.R;
import com.tokopedia.session.R2;

import butterknife.BindView;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationActivationActivity extends BasePresenterActivity {

    @BindView(R2.id.protect_account_text)
    TextView protectAccountText;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_verification_activation;
    }

    @Override
    protected void initView() {
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


        PhoneVerificationFragment fragment = PhoneVerificationFragment.createInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
