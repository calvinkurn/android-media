package com.tokopedia.otp.cotp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.analytics.OTPAnalytics;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.adapter.VerificationMethodAdapter;
import com.tokopedia.otp.cotp.view.presenter.ChooseVerificationPresenter;
import com.tokopedia.otp.cotp.view.viewlistener.SelectVerification;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.activity.ChangePhoneNumberRequestActivity;

import javax.inject.Inject;

/**
 * @author by nisie on 11/29/17.
 */

public class ChooseVerificationMethodFragment extends BaseDaggerFragment implements
        SelectVerification.View {

    private RecyclerView methodListRecyclerView;
    TextView changePhoneNumberButton;

    @Inject
    GlobalCacheManager cacheManager;

    @Inject
    ChooseVerificationPresenter presenter;

    VerificationMethodAdapter adapter;
    VerificationPassModel passModel;

    View mainView;
    ProgressBar loadingView;

    @Override
    protected String getScreenName() {
        return OTPAnalytics.Screen.SCREEN_SELECT_VERIFICATION_METHOD;
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

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChooseVerificationMethodFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (cacheManager != null
                && cacheManager.getConvertObjData(VerificationActivity
                .PASS_MODEL, VerificationPassModel.class) != null) {
            passModel = cacheManager.getConvertObjData(VerificationActivity
                    .PASS_MODEL, VerificationPassModel.class);
        } else {
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_verification_method, parent, false);
        mainView = view.findViewById(R.id.main_view);
        loadingView = view.findViewById(R.id.progress_bar);
        methodListRecyclerView = view.findViewById(R.id.method_list);
        changePhoneNumberButton = view.findViewById(R.id.phone_inactive);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        adapter = VerificationMethodAdapter.createInstance(this);
        methodListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        methodListRecyclerView.setAdapter(adapter);
        changePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChangePhoneNumberRequestActivity.getCallingIntent(getActivity());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getMethodList(passModel.getPhoneNumber(),
                passModel.getOtpType());
    }

    @Override
    public void onMethodSelected(MethodItem methodItem) {
        if (getActivity() instanceof VerificationActivity) {
            ((VerificationActivity) getActivity()).goToVerificationPage(methodItem);
        }
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.GONE);
    }

    @Override
    public void dismissLoading() {
        loadingView.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessGetList(ListVerificationMethod listVerificationMethod) {
        adapter.setList(listVerificationMethod.getList());
    }

    @Override
    public void onErrorGetList(String errorMessage) {
        if (TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showEmptyState(getActivity(), mainView,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getMethodList(passModel.getPhoneNumber(), passModel.getOtpType());
                        }
                    });
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), mainView, errorMessage,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getMethodList(passModel.getPhoneNumber(), passModel.getOtpType());
                        }
                    });
        }
    }
}
