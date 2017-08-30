package com.tokopedia.transaction.bcaoneklik.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.BcaOneClickActivity;
import com.tokopedia.transaction.bcaoneklik.BcaOneClickEditActivity;
import com.tokopedia.transaction.bcaoneklik.adapter.BcaOneClickRecyclerAdapter;
import com.tokopedia.transaction.bcaoneklik.adapter.CreditCardRecyclerViewAdapter;
import com.tokopedia.transaction.bcaoneklik.di.DaggerPaymentOptionComponent;
import com.tokopedia.transaction.bcaoneklik.di.PaymentOptionComponent;
import com.tokopedia.transaction.bcaoneklik.dialog.BcaOneClickDeleteDialog;
import com.tokopedia.transaction.bcaoneklik.dialog.DeleteCreditCardDialog;
import com.tokopedia.transaction.bcaoneklik.listener.BcaOneClickDeleteListener;
import com.tokopedia.transaction.bcaoneklik.listener.ListPaymentTypeView;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.presenter.ListPaymentTypePresenterImpl;
import com.tokopedia.transaction.exception.ResponseRuntimeException;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACCESS_TOKEN_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.ACCESS_XCOID_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.CREDENTIAL_NUMBER_EXTRAS;
import static com.tokopedia.transaction.bcaoneklik.utils.BcaOneClickConstants.CREDENTIAL_TYPE_EXTRAS;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class ListPaymentTypeActivity extends TActivity
        implements ListPaymentTypeView, BcaOneClickDeleteListener, CreditCardRecyclerViewAdapter
        .CreditCardItemListener, DeleteCreditCardDialog.DeleteCreditCardDialogListener{
    public static final int REGISTER_BCA_ONE_CLICK_REQUEST_CODE = 1;
    public static final int EDIT_BCA_ONE_CLICK_REQUEST_CODE = 2;

    private RelativeLayout rootView;

    private RecyclerView bcaOneClickRecyclerView;

    private BcaOneClickRecyclerAdapter bcaOneClickRecyclerAdapter;

    private CreditCardRecyclerViewAdapter creditCardRecyclerViewAdapter;

    private RecyclerView creditCardVaultRecyclerView;

    private LinearLayout bcaOneClickRegisterLayout;

    private TextView quickPaymentTitle;

    private TextView bcaOneClickRegistrationButton;

    private TextView creditCardTitle;

    private View addCreditCardLayout;

    private TkpdProgressDialog progressDialog;

    private TkpdProgressDialog mainProgressDialog;

    private PaymentListModel paymentModels;

    private RefreshHandler refreshHandler;

    @Inject
    ListPaymentTypePresenterImpl presenter;

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
        rootView = (RelativeLayout) findViewById(R.id.payment_list_root_view);
        creditCardVaultRecyclerView = (RecyclerView) findViewById(R.id.credit_card_recycler_view);
        creditCardVaultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bcaOneClickRecyclerView = (RecyclerView) findViewById(R.id.bca_one_click_recycler_view);
        bcaOneClickRegisterLayout = (LinearLayout) findViewById(R.id.bca_one_click_register_layout);
        bcaOneClickRegistrationButton = (TextView) findViewById(R.id.bca_one_click_register_button);
        creditCardTitle = (TextView) findViewById(R.id.credit_card_title);
        quickPaymentTitle = (TextView) findViewById(R.id.quick_payment_title);
        addCreditCardLayout = findViewById(R.id.add_credit_card_layout);
        bcaOneClickRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bcaOneClickRecyclerView.setNestedScrollingEnabled(false);
        creditCardVaultRecyclerView.setNestedScrollingEnabled(false);
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
        bcaOneClickRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.showDialog();
                presenter.onRegisterOneClickBcaChosen(new Subscriber<BcaOneClickData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorSnackbar(e);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(BcaOneClickData bcaOneClickData) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ACCESS_TOKEN_EXTRAS,
                                bcaOneClickData.getToken().getAccessToken());
                        Intent intent = new Intent(ListPaymentTypeActivity.this, BcaOneClickActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REGISTER_BCA_ONE_CLICK_REQUEST_CODE);
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    private void fetchData() {
        presenter.onGetCreditCardList(this);
        presenter.onGetPaymentList(paymentListModelSubscriber());
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
    public void receivedCreditCardList(CreditCardModel creditCardModel) {
        mainProgressDialog.dismiss();
        //TODO once there is option to add credit card change the condition
        if(creditCardModel.getCreditCardList().size()> 0) {
            addCreditCardLayout.setVisibility(View.GONE);
        } else {
            addCreditCardLayout.setVisibility(View.VISIBLE);
        }
        refreshHandler.finishRefresh();
        creditCardVaultRecyclerView.setVisibility(View.VISIBLE);

        creditCardRecyclerViewAdapter = new CreditCardRecyclerViewAdapter(
                creditCardModel.getCreditCardList(), ListPaymentTypeActivity.this
        );
        creditCardVaultRecyclerView.setAdapter(creditCardRecyclerViewAdapter);
        creditCardRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void successDeleteCreditCard(String message) {
        NetworkErrorHelper.showSnackbar(this, message);
        refreshHandler.finishRefresh();
        presenter.onGetCreditCardList(this);
    }

    @Override
    public void onLoadCreditCardError(String errorMessage) {
        NetworkErrorHelper.showSnackbar(this, errorMessage);
        rootView.setVisibility(View.VISIBLE);
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(ListPaymentTypeActivity.this, rootView,
                            errorMessage,
                            onLoadListRetryListener());
        mainProgressDialog.dismiss();
    }

    @Override
    public void onDeleteCreditCardError(String errorMessage) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.createSnackbarWithAction(ListPaymentTypeActivity.this,
                errorMessage,
                onLoadListRetryListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyed();
    }

    private Subscriber<PaymentListModel> paymentListModelSubscriber() {
        return new Subscriber<PaymentListModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                rootView.setVisibility(View.VISIBLE);
                if(e instanceof ResponseRuntimeException) {
                    NetworkErrorHelper.createSnackbarWithAction(ListPaymentTypeActivity.this,
                            e.getMessage(),
                           onLoadListRetryListener());
                } else {
                    NetworkErrorHelper.createSnackbarWithAction(ListPaymentTypeActivity.this,
                            onLoadListRetryListener());
                }
                mainProgressDialog.dismiss();
                refreshHandler.finishRefresh();
            }

            @Override
            public void onNext(PaymentListModel paymentListModel) {
                rootView.setVisibility(View.VISIBLE);
                mainProgressDialog.dismiss();
                refreshHandler.finishRefresh();
                if(paymentListModel.getBcaOneClickUserModels() == null) {
                    quickPaymentTitle.setVisibility(View.GONE);
                    bcaOneClickRecyclerView.setVisibility(View.GONE);
                    bcaOneClickRegisterLayout.setVisibility(View.GONE);
                } else {
                    paymentModels = paymentListModel;
                    bcaOneClickRecyclerAdapter = new BcaOneClickRecyclerAdapter(
                            paymentListModel.getBcaOneClickUserModels(),
                            actionListener()
                    );
                    bcaOneClickRecyclerView.setAdapter(bcaOneClickRecyclerAdapter);
                    bcaOneClickRecyclerAdapter.notifyDataSetChanged();
                    if(paymentListModel.getBcaOneClickUserModels().size() < 3) {
                        bcaOneClickRegisterLayout.setVisibility(View.VISIBLE);
                    } else bcaOneClickRegisterLayout.setVisibility(View.GONE);
                }
            }
        };
    }

    private Subscriber<PaymentListModel> deleteUserModelSubsriber() {
        return new Subscriber<PaymentListModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                showErrorSnackbar(e);
            }

            @Override
            public void onNext(PaymentListModel paymentListModel) {
                progressDialog.dismiss();
                refreshHandler.startRefresh();
            }
        };
    }

    private void showErrorSnackbar(Throwable e) {
        if(e instanceof ResponseRuntimeException) {
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
        }
    }

    private BcaOneClickRecyclerAdapter.ActionListener actionListener() {
        return new BcaOneClickRecyclerAdapter.ActionListener() {
            @Override
            public void onDelete(String tokenId, String name, String credentialNumber) {
                BcaOneClickDeleteDialog bcaOneClickDeleteDialog =
                        BcaOneClickDeleteDialog.createDialog(tokenId, name, credentialNumber);
                bcaOneClickDeleteDialog.show(getFragmentManager(), "delete_dialog");
            }

            @Override
            public void onEdit(final String tokenId,
                               final String credentialType,
                               final String credentialNumber) {
                progressDialog.showDialog();
                presenter.onRegisterOneClickBcaChosen(new Subscriber<BcaOneClickData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorSnackbar(e);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(BcaOneClickData bcaOneClickData) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ACCESS_TOKEN_EXTRAS,
                                bcaOneClickData.getToken().getAccessToken());
                        bundle.putString(ACCESS_XCOID_EXTRAS, tokenId);
                        bundle.putString(CREDENTIAL_TYPE_EXTRAS, credentialType);
                        bundle.putString(CREDENTIAL_NUMBER_EXTRAS, credentialNumber);
                        Intent intent = new Intent(ListPaymentTypeActivity.this,
                                BcaOneClickEditActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, EDIT_BCA_ONE_CLICK_REQUEST_CODE);
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public String getUserLoginAccountName() {
                return new SessionHandler(ListPaymentTypeActivity.this).getLoginName();
            }
        };
    }

    @Override
    public void onDelete(String tokenId) {
        progressDialog.showDialog();
        presenter.onDeletePaymentList(deleteUserModelSubsriber(), tokenId);
    }

    private NetworkErrorHelper.RetryClickedListener onLoadListRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                rootView.setVisibility(View.GONE);
                refreshHandler.startRefresh();
            }
        };
    }

    @Override
    public void onDeleteButtonClicked(String tokenId, String cardId) {
        DeleteCreditCardDialog creditCardDialog = DeleteCreditCardDialog.createDialog(tokenId,
                cardId);
        creditCardDialog.show(getFragmentManager(), "delete_credit_card_dialog");
    }


    @Override
    public void onConfirmDelete(String tokenId) {
        presenter.onCreditCardDeleted(this, tokenId);
    }
}
