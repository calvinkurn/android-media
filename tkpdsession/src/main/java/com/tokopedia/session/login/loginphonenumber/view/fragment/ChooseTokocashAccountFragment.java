package com.tokopedia.session.login.loginphonenumber.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.otp.securityquestion.view.activity.SecurityQuestionActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.login.loginphonenumber.view.activity.ChooseTokocashAccountActivity;
import com.tokopedia.session.login.loginphonenumber.view.adapter.TokocashAccountAdapter;
import com.tokopedia.session.login.loginphonenumber.view.presenter.ChooseTokocashAccountPresenter;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.ChooseTokocashAccount;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.ChooseTokoCashAccountViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 12/4/17.
 */

public class ChooseTokocashAccountFragment extends BaseDaggerFragment implements
        ChooseTokocashAccount.View {
    private int REQUEST_SECURITY_QUESTION = 101;

    TextView message;
    RecyclerView listAccount;
    TokocashAccountAdapter adapter;

    ChooseTokoCashAccountViewModel viewModel;
    TkpdProgressDialog progressDialog;

    @Inject
    ChooseTokocashAccountPresenter presenter;


    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChooseTokocashAccountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_CHOOSE_TOKOCASH_ACCOUNT;
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
        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(ChooseTokocashAccountActivity
                    .ARGS_DATA);
        } else if (getArguments() != null) {
            viewModel = getArguments().getParcelable(ChooseTokocashAccountActivity
                    .ARGS_DATA);
        } else {
            getActivity().finish();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_tokocash_account, parent, false);
        message = view.findViewById(R.id.message);
        listAccount = view.findViewById(R.id.list_account);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        adapter = TokocashAccountAdapter.createInstance(this, viewModel.getListAccount());
        listAccount.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        listAccount.setAdapter(adapter);
    }


    @Override
    public void onSelectedTokocashAccount(AccountTokocash accountTokocash) {
        presenter.loginWithTokocash(viewModel.getKey(),
                accountTokocash);
    }

    @Override
    public void onSuccessLogin() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        progressDialog.showDialog();
    }

    @Override
    public void dismissLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void onErrorLoginTokoCash(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void goToSecurityQuestion(MakeLoginDomain makeLoginDomain) {
        Intent intent = SecurityQuestionActivity.getCallingIntent(getActivity(),
                makeLoginDomain.getSecurityDomain(),
                makeLoginDomain.getFullName(),
                "",
                viewModel.getPhoneNumber());
        startActivityForResult(intent, REQUEST_SECURITY_QUESTION);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        message.setText(getPromptText());
    }

    private Spanned getPromptText() {
        return MethodChecker.fromHtml(getString(R.string.prompt_choose_tokocash_account,
                viewModel.getListAccount().size(),
                viewModel.getPhoneNumber()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
            onSuccessLogin();
        } else {
            presenter.clearUserData();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
