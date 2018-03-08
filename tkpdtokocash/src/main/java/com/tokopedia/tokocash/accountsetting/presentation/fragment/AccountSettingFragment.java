package com.tokopedia.tokocash.accountsetting.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.accountsetting.presentation.adapter.LinkedAccountAdapter;
import com.tokopedia.tokocash.accountsetting.presentation.contract.AccountSettingContract;
import com.tokopedia.tokocash.accountsetting.presentation.dialog.DeleteTokoCashAccountDialog;
import com.tokopedia.tokocash.accountsetting.presentation.model.AccountWalletItem;
import com.tokopedia.tokocash.accountsetting.presentation.model.OAuthInfo;
import com.tokopedia.tokocash.accountsetting.presentation.presenter.AccountSettingPresenter;
import com.tokopedia.tokocash.di.TokoCashComponent;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/27/18.
 */

public class AccountSettingFragment extends BaseDaggerFragment implements AccountSettingContract.View {

    private NestedScrollView mainContainer;
    private TextView nameAccount;
    private TextView emailAccount;
    private TextView phoneAccount;
    private RecyclerView rvConnectedUser;

    private TkpdProgressDialog progressDialog;
    private RefreshHandler refreshHandler;
    private LinkedAccountAdapter accountListAdapter;
    private ActionListener listener;

    @Inject
    AccountSettingPresenter presenter;

    public static AccountSettingFragment newInstance() {
        AccountSettingFragment fragment = new AccountSettingFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        presenter.attachView(this);
        View view = inflater.inflate(R.layout.fragment_account_setting_tokocash, container, false);
        mainContainer = view.findViewById(R.id.main_container);
        nameAccount = view.findViewById(R.id.name_account);
        emailAccount = view.findViewById(R.id.email_account);
        phoneAccount = view.findViewById(R.id.phone_account);
        rvConnectedUser = view.findViewById(R.id.rv_account_list);
        return view;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof ActionListener) {
            listener = (ActionListener) context;
        } else {
            throw new RuntimeException("Activity isn't exist");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshHandler = new RefreshHandler(getActivity(), view, getRefreshHandlerListener());
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        initialVar();
        refreshHandler.startRefresh();
    }

    private RefreshHandler.OnRefreshHandlerListener getRefreshHandlerListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                if (refreshHandler.isRefreshing()) {
                    presenter.processGetWalletAccountData();
                }
            }
        };
    }

    private void initialVar() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        accountListAdapter = new LinkedAccountAdapter(new ArrayList<AccountWalletItem>());
        accountListAdapter.setActionListener(getActionListener());
        rvConnectedUser.setLayoutManager(linearLayoutManager);
        rvConnectedUser.setNestedScrollingEnabled(false);
        rvConnectedUser.setAdapter(accountListAdapter);
    }

    private LinkedAccountAdapter.ActionListener getActionListener() {
        return new LinkedAccountAdapter.ActionListener() {
            @Override
            public void onDeleteAccessClicked(AccountWalletItem accountTokoCash) {
                DeleteTokoCashAccountDialog dialog = DeleteTokoCashAccountDialog.createDialog(
                        accountTokoCash.getRefreshToken(), accountTokoCash.getIdentifier(),
                        accountTokoCash.getIdentifierType());
                dialog.setListener(getDialogListener());
                dialog.show(getActivity().getFragmentManager(), "delete_tokocash_dialog");
            }
        };
    }

    private DeleteTokoCashAccountDialog.DeleteAccessAccountListener getDialogListener() {
        return new DeleteTokoCashAccountDialog.DeleteAccessAccountListener() {
            @Override
            public void onDeleteAccess(String revokeToken, String identifier, String identifierType) {
                showProgressDialog();
                presenter.processDeleteConnectedUser(revokeToken, identifier, identifierType);
            }
        };
    }

    @Override
    public void renderWalletOAuthInfoData(OAuthInfo oAuthInfo) {
        if (mainContainer != null) {
            mainContainer.setVisibility(View.VISIBLE);
            refreshHandler.finishRefresh();
            nameAccount.setText(oAuthInfo.getName());
            emailAccount.setText(oAuthInfo.getEmail());
            phoneAccount.setText(oAuthInfo.getMobile());
            if (oAuthInfo.getAccountList() != null)
                accountListAdapter.addAccountList(oAuthInfo.getAccountList());
        }
    }

    @Override
    public void renderErrorGetWalletAccountSettingData(Throwable e) {
        String message = ErrorHandler.getErrorMessage(getActivity(), e);
        renderEmptyPage(message);
    }

    @Override
    public void renderSuccessUnlinkAccount() {
        hideProgressDialog();
        refreshHandler.startRefresh();
    }

    @Override
    public void renderSuccessUnlinkToHome() {
        hideProgressDialog();
        presenter.deleteCacheBalanceAndTokenTokoCash();
        listener.directPageToHome();
    }

    @Override
    public String getUserEmail() {
        return ((TokoCashRouter) getActivity().getApplication()).getUserEmailProfil();
    }

    @Override
    public void renderErrorUnlinkAccount(Throwable e) {
        String message = ErrorHandler.getErrorMessage(getActivity(), e);
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    private void renderEmptyPage(String message) {
        refreshHandler.finishRefresh();
        View rootView = getView();
        if (rootView != null)
            NetworkErrorHelper.showEmptyState(
                    getActivity(), rootView, message, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            refreshHandler.startRefresh();
                        }
                    }
            );
        else
            NetworkErrorHelper.createSnackbarWithAction(
                    getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            refreshHandler.startRefresh();
                        }
                    }
            ).showRetrySnackbar();
    }

    private void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.showDialog();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoCashComponent.class).inject(this);
    }

    public interface ActionListener {
        void directPageToHome();
    }
}