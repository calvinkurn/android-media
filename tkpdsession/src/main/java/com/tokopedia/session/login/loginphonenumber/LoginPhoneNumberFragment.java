package com.tokopedia.session.login.loginphonenumber;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.presenter.LoginPhoneNumberPresenter;
import com.tokopedia.session.login.loginphonenumber.viewlistener.LoginPhoneNumber;

import javax.inject.Inject;

/**
 * @author by nisie on 11/23/17.
 */

class LoginPhoneNumberFragment extends BaseDaggerFragment
        implements LoginPhoneNumber.View {

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

//        DaggerSessionComponent daggerSessionComponent = (DaggerSessionComponent)
//                DaggerSessionComponent.builder()
//                        .appComponent(appComponent)
//                        .build();
//
//        daggerSessionComponent.inject(this);

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

        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
