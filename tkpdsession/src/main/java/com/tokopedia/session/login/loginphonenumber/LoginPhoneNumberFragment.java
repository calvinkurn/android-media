package com.tokopedia.session.login.loginphonenumber;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.presenter.LoginPhoneNumberPresenter;
import com.tokopedia.session.login.loginphonenumber.viewlistener.LoginPhoneNumber;
import com.tokopedia.di.DaggerSessionComponent;
import javax.inject.Inject;

/**
 * @author by nisie on 11/23/17.
 */

public class LoginPhoneNumberFragment extends BaseDaggerFragment
        implements LoginPhoneNumber.View {

    EditText phoneNumber;
    TextView nextButton;
    TextView message;

    @Inject
    LoginPhoneNumberPresenter presenter;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new LoginPhoneNumberFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_LOGIN_PHONE_NUMBER;
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
//        if (savedInstanceState != null) {
//            model = savedInstanceState.getParcelable(LoginPhoneNumberActivity
//                    .ARGS_FORM_DATA);
//        } else if (getArguments() != null) {
//            model = getArguments().getParcelable(LoginPhoneNumberActivity
//                    .ARGS_FORM_DATA);
//        } else {
//            getActivity().finish();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_phone_number, parent, false);
        phoneNumber = view.findViewById(R.id.phone_number);
        message = view.findViewById(R.id.message);
        nextButton = view.findViewById(R.id.next_btn);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    presenter.loginWithPhoneNumber(phoneNumber.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginWithPhoneNumber(phoneNumber.getText().toString());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showErrorPhoneNumber(int resId) {
        showErrorPhoneNumber(getString(resId));
    }

    @Override
    public void goToVerifyAccountPage(String phoneNumber) {

    }

    private void showErrorPhoneNumber(String errorMessage) {
        message.setTextColor(MethodChecker.getColor(getActivity(), R.color.red_500));
        message.setText(errorMessage);
    }
}
