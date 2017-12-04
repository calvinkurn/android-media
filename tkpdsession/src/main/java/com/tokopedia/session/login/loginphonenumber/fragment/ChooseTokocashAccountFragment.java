package com.tokopedia.session.login.loginphonenumber.fragment;

import android.app.Activity;
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

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.adapter.TokocashAccountAdapter;
import com.tokopedia.session.login.loginphonenumber.presenter.ChooseTokocashAccountPresenter;
import com.tokopedia.session.login.loginphonenumber.viewlistener.ChooseTokocashAccount;
import com.tokopedia.session.login.loginphonenumber.viewmodel.AccountTokocash;
import com.tokopedia.di.DaggerSessionComponent;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 12/4/17.
 */

public class ChooseTokocashAccountFragment extends BaseDaggerFragment implements
        ChooseTokocashAccount.View {

    TextView message;
    RecyclerView listAccount;
    TokocashAccountAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_choose_tokocash_account, parent, false);
        message = view.findViewById(R.id.message);
        listAccount = view.findViewById(R.id.list_account);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        adapter = TokocashAccountAdapter.createInstance(this);
        listAccount.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        listAccount.setAdapter(adapter);
    }


    @Override
    public void onSelectedTokocashAccount(AccountTokocash accountTokocash) {
        presenter.loginWithTokocash(accountTokocash);
    }

    @Override
    public void onSuccessGetTokocashAccounts(ArrayList<AccountTokocash> listAccount) {
        listAccount.add(new AccountTokocash(
                "1001",
                "Nisie 1",
                "nisie@nisie.com",
                "https://ecs7.tokopedia.net/img/cache/100-square/usr-1/2015/3/30/2590134/pic_2590134_16109006-d6a4-11e4-a5e2-d9ac4908a8c2.jpg"
        ));
        listAccount.add(new AccountTokocash(
                "1002",
                "Nisie 2",
                "nisie@nisie.com",
                "https://ecs7.tokopedia.net/img/cache/100-square/usr-1/2015/3/30/2590134/pic_2590134_16109006-d6a4-11e4-a5e2-d9ac4908a8c2.jpg"
        ));
        listAccount.add(new AccountTokocash(
                "1003",
                "Nisie 3",
                "nisie@nisie.com",
                "https://ecs7.tokopedia.net/img/cache/100-square/usr-1/2015/3/30/2590134/pic_2590134_16109006-d6a4-11e4-a5e2-d9ac4908a8c2.jpg"
        ));

        adapter.setList(listAccount);
    }

    @Override
    public void onSuccessLogin() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        message.setText(getPromptText());
        presenter.getTokocashAccounts();
    }

    private Spanned getPromptText() {
        return MethodChecker.fromHtml("");
    }
}
