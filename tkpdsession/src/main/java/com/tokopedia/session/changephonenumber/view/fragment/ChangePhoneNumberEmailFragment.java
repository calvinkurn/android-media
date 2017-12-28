package com.tokopedia.session.changephonenumber.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.di.SessionComponent;
import com.tokopedia.di.SessionModule;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailFragmentListener;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;

import org.w3c.dom.Text;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberEmailFragment extends BaseDaggerFragment implements ChangePhoneNumberEmailFragmentListener.View {
    public static final String PARAM_EMAIL = "email";

    @Inject
    ChangePhoneNumberEmailFragmentListener.Presenter presenter;
    private String email;
    private Unbinder unbinder;
    private View mainView;
    private View loadingView;
    private TextView backButton;
    private TextView emailTV;

    public static ChangePhoneNumberEmailFragment newInstance(String email) {
        ChangePhoneNumberEmailFragment fragment = new ChangePhoneNumberEmailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_EMAIL, email);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_change_phone_number_email, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        presenter.attachView(this);
        initView(parentView);
        setViewListener();
        initVar();
        return parentView;
    }

    private void initView(View view) {
        mainView = view.findViewById(R.id.main_view);
        loadingView = view.findViewById(R.id.loading_view);
        backButton = view.findViewById(R.id.back_button);
        emailTV = view.findViewById(R.id.email_value);

        presenter.initView();
    }

    private void setViewListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void initVar() {
        email = getArguments().getString(PARAM_EMAIL);

        emailTV.setText(email);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        SessionComponent sessionComponent =
                DaggerSessionComponent.builder()
                        .appComponent(appComponent)
                        .sessionModule(new SessionModule())
                        .build();
        sessionComponent.inject(this);
    }

    @Override
    public void showLoading() {
        mainView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        loadingView.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSendEmailSuccess(Boolean isSuccess) {
        if (isSuccess)
            showEmptyState(null);
        dismissLoading();
    }

    @Override
    public void onSendEmailError(String message) {
        showEmptyState(message);
        dismissLoading();
    }

    @Override
    public void onSendEmailFailed() {
        showEmptyState(null);
        dismissLoading();
    }

    private void showEmptyState(String message) {
        if (message == null || message.isEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.sendEmail();
                        }
                    });
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    message,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.sendEmail();
                        }
                    });
        }
    }
}
