package com.tokopedia.session.login.loginphonenumber.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity;
import com.tokopedia.otp.tokocashotp.view.viewmodel.MethodItem;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.view.activity.NotConnectedTokocashActivity;
import com.tokopedia.session.session.activity.Login;

import java.util.ArrayList;

/**
 * @author by nisie on 12/4/17.
 */

public class NotConnectedTokocashFragment extends BaseDaggerFragment {

    private static final int REQUEST_VERIFY_PHONE_NUMBER = 101;

    ImageView image;
    TextView title;
    TextView message;
    TextView actionButton;
    TextView loginOtherButton;

    String phoneNumber;
    int type;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new NotConnectedTokocashFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_NOT_CONNECTED_TO_TOKOCASH;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerSessionComponent daggerSessionComponent = (DaggerSessionComponent)
                DaggerSessionComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerSessionComponent.inject(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !TextUtils.isEmpty(savedInstanceState.getString
                (NotConnectedTokocashActivity.PARAM_PHONE_NUMBER, ""))) {
            phoneNumber = savedInstanceState.getString
                    (NotConnectedTokocashActivity.PARAM_PHONE_NUMBER);
        } else if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString
                (NotConnectedTokocashActivity.PARAM_PHONE_NUMBER, ""))) {
            phoneNumber = getArguments().getString
                    (NotConnectedTokocashActivity.PARAM_PHONE_NUMBER);
        } else {
            getActivity().finish();
        }

        if (savedInstanceState != null && savedInstanceState.getInt(
                NotConnectedTokocashActivity.PARAM_TYPE, -1) != -1) {
            type = savedInstanceState.getInt(
                    (NotConnectedTokocashActivity.PARAM_TYPE));
        } else if (getArguments() != null && getArguments().getInt(
                NotConnectedTokocashActivity.PARAM_TYPE, -1) != -1) {
            type = getArguments().getInt
                    (NotConnectedTokocashActivity.PARAM_TYPE);
        } else {
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_connected_tokocash, parent, false);
        actionButton = view.findViewById(R.id.action_button);
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        image = view.findViewById(R.id.icon);
        loginOtherButton = view.findViewById(R.id.login_other_button);
        prepareView();
        return view;
    }

    private void prepareView() {
        if (type == NotConnectedTokocashActivity.TYPE_PHONE_NOT_CONNECTED) {
            setViewPhoneNotconnected();
        } else {
            setViewNoTokocashAccount();
        }
    }

    private void setViewNoTokocashAccount() {

        ImageHandler.loadImageWithIdWithoutPlaceholder(image, R.drawable.ic_tokocash_no_account);
        title.setText(R.string.no_tokocash_account);
        message.setText(R.string.no_tokocash_account_message);
        actionButton.setText(R.string.login_with_other_method_2);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginPage();
            }
        });

        loginOtherButton.setVisibility(View.GONE);
    }

    private void setViewPhoneNotconnected() {
        ImageHandler.loadImageWithIdWithoutPlaceholder(image, R.drawable.ic_tokocash_phone_not_connected);
        title.setText(R.string.phone_number_not_connected_to_tokocash);
        message.setText(R.string.not_connected_tokocash_message);
        actionButton.setText(R.string.verify_phone_number);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToVerifyPhoneNumberPage();
            }
        });

        loginOtherButton.setVisibility(View.VISIBLE);
        loginOtherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginPage();
            }
        });
    }

    private void goToLoginPage() {
        if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
            Intent intentLogin = Login.getCallingIntent(getActivity());
            Intent intentHome = ((TkpdCoreRouter) MainApplication.getAppContext()).getHomeIntent
                    (getActivity());
            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivities(new Intent[]
                    {
                            intentHome,
                            intentLogin
                    });
            getActivity().finish();

        }
    }

    private void goToVerifyPhoneNumberPage() {
        startActivityForResult(VerificationActivity.getSmsVerificationIntent(
                getActivity(),
                phoneNumber,
                getListAvailableMethod()
        ), REQUEST_VERIFY_PHONE_NUMBER);
    }

    private ArrayList<MethodItem> getListAvailableMethod() {
        ArrayList<MethodItem> list = new ArrayList<>();
        list.add(new MethodItem(
                VerificationActivity.TYPE_SMS,
                R.drawable.ic_verification_sms,
                MethodItem.getSmsMethodText(phoneNumber)
        ));
        list.add(new MethodItem(
                VerificationActivity.TYPE_PHONE_CALL,
                R.drawable.ic_verification_call,
                MethodItem.getCallMethodText(phoneNumber)
        ));
        return list;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VERIFY_PHONE_NUMBER && resultCode == Activity.RESULT_OK) {

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NotConnectedTokocashActivity.PARAM_PHONE_NUMBER, phoneNumber);
        outState.putInt(NotConnectedTokocashActivity.PARAM_TYPE, type);
    }
}
