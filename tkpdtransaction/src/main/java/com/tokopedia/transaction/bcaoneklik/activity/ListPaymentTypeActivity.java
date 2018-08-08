package com.tokopedia.transaction.bcaoneklik.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.BcaOneClickActivity;
import com.tokopedia.transaction.bcaoneklik.BcaOneClickEditActivity;
import com.tokopedia.transaction.bcaoneklik.adapter.PaymentSettingMainAdapter;
import com.tokopedia.transaction.bcaoneklik.di.DaggerPaymentOptionComponent;
import com.tokopedia.transaction.bcaoneklik.di.PaymentOptionComponent;
import com.tokopedia.transaction.bcaoneklik.dialog.BcaOneClickDeleteDialog;
import com.tokopedia.transaction.bcaoneklik.dialog.DeleteCreditCardDialog;
import com.tokopedia.transaction.bcaoneklik.listener.BcaOneClickDeleteListener;
import com.tokopedia.transaction.bcaoneklik.listener.CreditCardAuthenticationView;
import com.tokopedia.transaction.bcaoneklik.listener.ListPaymentTypeView;
import com.tokopedia.transaction.bcaoneklik.model.PaymentSettingModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;
import com.tokopedia.transaction.bcaoneklik.presenter.ListPaymentTypePresenterImpl;
import com.tokopedia.transaction.exception.ResponseRuntimeException;

import javax.inject.Inject;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class ListPaymentTypeActivity extends TActivity implements ListPaymentTypeView, BcaOneClickDeleteListener {

    private RelativeLayout rootView;
    private TkpdProgressDialog progressDialog;
    private TkpdProgressDialog mainProgressDialog;
    private RefreshHandler refreshHandler;
    private PaymentSettingMainAdapter paymentSettingsAdapter;

    @Inject ListPaymentTypePresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.payment_list_layout);
        initInjector();
        presenter.setViewListener(this);
        initView();
    }

    private void initInjector() {
        PaymentOptionComponent component = DaggerPaymentOptionComponent
                .builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
    }

    protected void initView() {
        rootView = findViewById(R.id.payment_list_root_view);
        RecyclerView paymentOptionMainRecyclerView = findViewById(R.id.payment_option_main_recycler_view);

        paymentSettingsAdapter = new PaymentSettingMainAdapter(presenter, this);
        paymentOptionMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentOptionMainRecyclerView.setAdapter(paymentSettingsAdapter);

        progressDialog = new TkpdProgressDialog(ListPaymentTypeActivity.this,
                TkpdProgressDialog.NORMAL_PROGRESS);
        mainProgressDialog = new TkpdProgressDialog(ListPaymentTypeActivity.this,
                TkpdProgressDialog.MAIN_PROGRESS);
        refreshHandler = new RefreshHandler(this, rootView, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                fetchData();
            }
        });

        showMainDialog();
        fetchData();
    }

    private void fetchData() {
        presenter.onGetAllPaymentList(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showMainDialog() {
        mainProgressDialog.showDialog();
    }

    @Override
    public void dismissMainDialog() {
        mainProgressDialog.dismiss();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void successDeleteCreditCard(String message) {
        NetworkErrorHelper.showSnackbar(this, message);
        fetchData();
    }

    @Override
    public void onLoadCreditCardError(String errorMessage) {
        //TODO Later if Payment Team made Quick Payment Menu make Credit Card spesific error
        NetworkErrorHelper.showSnackbar(this, errorMessage);
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(ListPaymentTypeActivity.this, rootView,
                            errorMessage,
                            onLoadListRetryListener());
        mainProgressDialog.dismiss();
    }

    @Override
    public void onLoadAllError(String errorMessage) {
        rootView.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(ListPaymentTypeActivity.this, rootView,
                errorMessage,
                onLoadListRetryListener());
    }

    @Override
    public void onDeleteCreditCardError(String errorMessage) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.createSnackbarWithAction(ListPaymentTypeActivity.this,
                errorMessage,
                onLoadListRetryListener());
    }

    @Override
    public void showError(String errorMessage) {
        NetworkErrorHelper.showSnackbar(this, errorMessage);
    }

    @Override
    public void onShowDeleteBcaOneClickDialog(String tokenId,
                                              String name,
                                              String credentialNumber) {
        BcaOneClickDeleteDialog bcaOneClickDeleteDialog =
                BcaOneClickDeleteDialog.createDialog(tokenId, name, credentialNumber);
        bcaOneClickDeleteDialog.show(getFragmentManager(), "delete_dialog");
    }

    @Override
    public void showDeleteBcaOneClickError() {
        NetworkErrorHelper.showSnackbar(this, ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
    }

    @Override
    public void onDeleteCreditCardClicked(String tokenId, String cardId) {
        DeleteCreditCardDialog creditCardDialog = DeleteCreditCardDialog.newInstance(tokenId,
                cardId);
        creditCardDialog.show(getFragmentManager(), "delete_credit_card_dialog");
    }

    @Override
    public void onBcaOneClickSuccessGetToken(Bundle bundle) {
        Intent intent = new Intent(ListPaymentTypeActivity.this,
                BcaOneClickEditActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_BCA_ONE_CLICK_REQUEST_CODE);
        progressDialog.dismiss();
    }

    @Override
    public void onBcaOneClickSuccessGetRegisterToken(Bundle bundle) {
        Intent intent = new Intent(ListPaymentTypeActivity.this,
                BcaOneClickActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, REGISTER_BCA_ONE_CLICK_REQUEST_CODE);
        progressDialog.dismiss();
    }

    @Override
    public void onFetchDataComplete(PaymentSettingModel model) {
        rootView.setVisibility(View.VISIBLE);
        refreshHandler.finishRefresh();
        paymentSettingsAdapter.updateData(model);
    }

    @Override
    public void openAuthenticatorPage(AuthenticatorPageModel data) {
        Intent intent = new Intent(this, CreditCardAuthenticationActivity.class);
        intent.putExtra(CreditCardAuthenticationView.CREDIT_CARD_STATUS_KEY, data);
        startActivityForResult(intent, EDIT_AUTHENTICATION_PAGE);
    }

    @Override
    public void refreshList() {
        refreshHandler.startRefresh();
    }

    @Override
    public void onErrorGetBcaOneClickToken(Throwable e) {
        showErrorSnackbar(e);
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyed();
    }

    private void showErrorSnackbar(Throwable e) {
        if (e instanceof ResponseRuntimeException) {
            NetworkErrorHelper.showSnackbar(ListPaymentTypeActivity.this, e.getMessage());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REGISTER_BCA_ONE_CLICK_REQUEST_CODE:
                refreshHandler.startRefresh();
                break;
            case EDIT_BCA_ONE_CLICK_REQUEST_CODE:
                refreshHandler.startRefresh();
                break;
            case EDIT_AUTHENTICATION_PAGE:
                refreshHandler.startRefresh();
                break;
            case CREDIT_CARD_DETAIL_REQUEST_CODE:
                recreate();
                break;
        }
    }

    @Override
    public void onDelete(String tokenId) {
        presenter.onDeleteBcaOneClick(tokenId);
    }

    private NetworkErrorHelper.RetryClickedListener onLoadListRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                refreshHandler.startRefresh();
            }
        };
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
