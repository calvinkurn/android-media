package com.tokopedia.session.changephonenumber.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.deposit.activity.WithdrawActivity;
import com.tokopedia.core.deposit.presenter.DepositFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.di.SessionComponent;
import com.tokopedia.di.SessionModule;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.activity.ChangePhoneNumberEmailActivity;
import com.tokopedia.session.changephonenumber.view.activity.ChangePhoneNumberInputActivity;
import com.tokopedia.session.changephonenumber.view.adapter.WarningListAdapter;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel.ACTION_EMAIL;
import static com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel.ACTION_OTP;
import static com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel.EMPTY_BALANCE;

/**
 * Created by milhamj on 18/12/17.
 */

public class ChangePhoneNumberWarningFragment extends BaseDaggerFragment implements ChangePhoneNumberWarningFragmentListener.View {
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PHONE_NUMBER = "phone_number";
    public static final int REQUEST_WITHDRAW_CODE = 1;

    @Inject
    WarningListAdapter adapter;
    @Inject
    ChangePhoneNumberWarningFragmentListener.Presenter presenter;
    private RelativeLayout tokopediaBalanceLayout;
    private RelativeLayout tokocashLayout;
    private TextView tokopediaBalanceValue;
    private TextView tokocashValue;
    private RecyclerView warningRecyclerView;
    private TextView nextButton;
    private TextView withdrawButton;
    private WarningViewModel viewModel;
    private String email;
    private String phoneNumber;
    private View mainView;
    private View loadingView;
    private Unbinder unbinder;

    public static ChangePhoneNumberWarningFragment newInstance(String email, String phoneNumber) {
        ChangePhoneNumberWarningFragment fragment = new ChangePhoneNumberWarningFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_EMAIL, email);
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_change_phone_number_warning, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        presenter.attachView(this);
        initView(parentView);
        setViewListener();
        initVar();
        return parentView;
    }

    private void initView(View view) {
        tokopediaBalanceLayout = view.findViewById(R.id.tokopedia_balance_layout);
        tokocashLayout = view.findViewById(R.id.tokocash_layout);
        tokopediaBalanceValue = view.findViewById(R.id.tokopedia_balance_value);
        tokocashValue = view.findViewById(R.id.tokocash_value);
        warningRecyclerView = view.findViewById(R.id.warning_rv);
        nextButton = view.findViewById(R.id.next_button);
        withdrawButton = view.findViewById(R.id.withdraw_button);
        mainView = view.findViewById(R.id.main_view);
        loadingView = view.findViewById(R.id.loading_view);

        warningRecyclerView.setFocusable(false);

        presenter.initView();
    }

    private void setViewListener() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextActivity();
            }
        });

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WithdrawActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(DepositFragmentPresenterImpl.BUNDLE_TOTAL_BALANCE,
                        viewModel.getTokopediaBalance());
                bundle.putString(DepositFragmentPresenterImpl.BUNDLE_TOTAL_BALANCE_INT,
                        viewModel.getTokopediaBalance().replaceAll("[^\\d]", ""));
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_WITHDRAW_CODE);
            }
        });
    }

    private void initVar() {
        email = getArguments().getString(PARAM_EMAIL);
        phoneNumber = getArguments().getString(PARAM_PHONE_NUMBER);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
    public void onGetWarningSuccess(WarningViewModel warningViewModel) {
        this.viewModel = warningViewModel;
        if (isNullOrEmpty(viewModel.getTokocash()) && isNullOrEmpty(viewModel.getTokopediaBalance())) {
            goToNextActivity();
            getActivity().finish();
        } else {
            loadDataToView();
            showOrHideWithdrawButton();
            dismissLoading();
        }
    }

    private void showOrHideWithdrawButton() {
        if (viewModel.getAction().equalsIgnoreCase(ACTION_OTP) && !isNullOrEmpty(viewModel.getTokopediaBalance())) {
            withdrawButton.setVisibility(View.VISIBLE);
        } else {
            withdrawButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetWarningError(String message) {
        showEmptyState(message);
        dismissLoading();
    }

    @Override
    public void onGetWarningFailed() {
        showEmptyState(null);
        dismissLoading();
    }

    private void loadDataToView() {
        if (viewModel != null) {
            if (isNullOrEmpty(viewModel.getTokopediaBalance())) {
                tokopediaBalanceLayout.setVisibility(View.GONE);
            } else {
                tokopediaBalanceLayout.setVisibility(View.VISIBLE);
                tokopediaBalanceValue.setText(viewModel.getTokopediaBalance());
            }

            if (isNullOrEmpty(viewModel.getTokocash())) {
                tokocashLayout.setVisibility(View.GONE);
            } else {
                tokocashLayout.setVisibility(View.VISIBLE);
                tokocashValue.setText(viewModel.getTokocash());
            }

            populateRecyclerView();
        }
    }

    private void goToNextActivity() {
        if (viewModel.getAction().equalsIgnoreCase(ACTION_EMAIL)) {
            startActivity(
                    ChangePhoneNumberEmailActivity.newInstance(getContext(), email)
            );
        } else if (viewModel.getAction().equalsIgnoreCase(ACTION_OTP)) {
            startActivity(
                    ChangePhoneNumberInputActivity.newInstance(
                            getContext(),
                            phoneNumber,
                            new ArrayList<>(viewModel.getWarningList())
                    )
            );
        }
    }

    private boolean isNullOrEmpty(String string) {
        return (string == null || string.equalsIgnoreCase("null") || string.isEmpty() || string.equalsIgnoreCase(EMPTY_BALANCE));
    }

    private void populateRecyclerView() {
        if (viewModel != null) {
            if (viewModel.getWarningList() != null && viewModel.getWarningList().size() > 0) {
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                warningRecyclerView.setLayoutManager(mLayoutManager);
                adapter.addData(viewModel.getWarningList());
                warningRecyclerView.setAdapter(adapter);
            }
        }
    }

    private void showEmptyState(String message) {
        if (message == null || message.isEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getWarning();
                        }
                    });
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    message,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getWarning();
                        }
                    });
        }
    }
}
