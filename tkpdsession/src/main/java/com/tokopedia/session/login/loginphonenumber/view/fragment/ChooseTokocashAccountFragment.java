package com.tokopedia.session.login.loginphonenumber.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.viewmodel.InterruptVerificationViewModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.tokocashotp.view.viewmodel.LoginTokoCashViewModel;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.session.login.loginphonenumber.view.activity.ChooseTokocashAccountActivity;
import com.tokopedia.session.login.loginphonenumber.view.adapter.TokocashAccountAdapter;
import com.tokopedia.session.login.loginphonenumber.view.presenter.ChooseTokocashAccountPresenter;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.ChooseTokocashAccount;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.ChooseTokoCashAccountViewModel;

import javax.inject.Inject;

import static com.tokopedia.session.login.loginemail.view.fragment.LoginFragment.TYPE_SQ_PHONE;

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


    @Inject
    GlobalCacheManager cacheManager;


    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChooseTokocashAccountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_CHOOSE_TOKOCASH_ACCOUNT;
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
        setHasOptionsMenu(true);
        message = view.findViewById(R.id.message);
        listAccount = view.findViewById(R.id.list_account);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.id.action_logout, 0, "");
        MenuItem menuItem = menu.findItem(R.id.action_logout); // OR THIS
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(getDraw());
        super.onCreateOptionsMenu(menu, inflater);
    }

    private Drawable getDraw() {
        TextDrawable drawable = new TextDrawable(getActivity());
        drawable.setText(getResources().getString(R.string.action_logout));
        drawable.setTextColor(R.color.black_70b);
        return drawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            goToLoginPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLoginPage() {
        if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
            Intent intentLogin = LoginActivity.getCallingIntent(getActivity());
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
        UnifyTracking.eventTracking(LoginPhoneNumberAnalytics.getSuccessLoginTracking());

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
    public void goToSecurityQuestion(AccountTokocash accountTokocash, LoginTokoCashViewModel
            loginTokoCashViewModel) {

        InterruptVerificationViewModel interruptVerificationViewModel;
        if (loginTokoCashViewModel.getMakeLoginDomain().getSecurityDomain()
                .getUserCheckSecurity2() == TYPE_SQ_PHONE) {
            interruptVerificationViewModel = InterruptVerificationViewModel
                    .createDefaultSmsInterruptPage(loginTokoCashViewModel.getUserInfoDomain()
                            .getGetUserInfoDomainData().getPhone());
        } else {
            interruptVerificationViewModel = InterruptVerificationViewModel
                    .createDefaultEmailInterruptPage(loginTokoCashViewModel.getUserInfoDomain()
                            .getGetUserInfoDomainData().getEmail());
        }

        VerificationPassModel passModel = new VerificationPassModel(
                loginTokoCashViewModel.getUserInfoDomain().getGetUserInfoDomainData().getPhone(),
                accountTokocash.getEmail(),
                RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
                interruptVerificationViewModel,
                loginTokoCashViewModel.getMakeLoginDomain().getSecurityDomain()
                        .getUserCheckSecurity2() == TYPE_SQ_PHONE);
        cacheManager.setKey(VerificationActivity.PASS_MODEL);
        cacheManager.setValue(CacheUtil.convertModelToString(passModel,
                new TypeToken<VerificationPassModel>() {
                }.getType()));
        cacheManager.store();


        Intent intent = VerificationActivity.getSecurityQuestionVerificationIntent(getActivity(),
                loginTokoCashViewModel.getMakeLoginDomain().getSecurityDomain().getUserCheckSecurity2());
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        getActivity().finish();
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
            presenter.clearToken();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ChooseTokocashAccountActivity.ARGS_DATA, viewModel);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
