package com.tokopedia.transaction.purchase.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.customadapter.LazyListView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.transaction.opportunity.domain.interactor.CancelReplacementUseCase;
import com.tokopedia.transaction.opportunity.domain.repository.ReplacementRepository;
import com.tokopedia.transaction.opportunity.domain.repository.ReplacementRepositoryImpl;
import com.tokopedia.transaction.purchase.adapter.TxListAdapter;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.transaction.purchase.listener.TxListViewListener;
import com.tokopedia.transaction.purchase.model.AllTxFilter;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderData;
import com.tokopedia.transaction.purchase.presenter.TxListPresenter;
import com.tokopedia.transaction.purchase.presenter.TxListPresenterImpl;
import com.tokopedia.transaction.purchase.receiver.TxListUIReceiver;
import com.tokopedia.transaction.purchase.utils.FilterUtils;

import java.util.List;

import butterknife.BindView;

/**
 * @author by Angga.Prasetiyo on 21/04/2016.
 *         Modified by Kulomady on 26/06/2016
 */
public class TxListFragment extends BasePresenterFragment<TxListPresenter> implements
        TxListViewListener, TxListAdapter.ActionListener,
        RefreshHandler.OnRefreshHandlerListener, LazyListView.LazyLoadListener,
        View.OnClickListener, TxBottomSheetFilterDialog.OnFilterListener,
        TxListUIReceiver.ActionListener {

    public static final int INSTANCE_STATUS = 1;
    public static final int INSTANCE_RECEIVE = 2;

    @BindView(R2.id.order_list)
    LazyListView lvTXList;
    @BindView(R2.id.fab_filter)
    FloatingActionButton fabFilter;
    private View loadMoreView;

    private TkpdProgressDialog progressDialog;
    private AllTxFilter allTxFilter;
    private TxListAdapter txListAdapter;

    private PagingHandler pagingHandler = new PagingHandler();
    private RefreshHandler refreshHandler;
    private StateFilterListener stateFilterListener;

    private boolean isLoadMoreTerminated = true;
    private int typeInstance;
    private boolean isLoading = false;
    private String txFilterID;

    private TxBottomSheetFilterDialog bottomSheetFilterDialog;
    private boolean instanceFromNotification;
    private TxListUIReceiver txUIReceiver;


    public static TxListFragment instanceStatusOrder() {
        TxListFragment fragment = new TxListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TransactionPurchaseRouter.ARG_PARAM_EXTRA_INSTANCE_TYPE, INSTANCE_STATUS);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TxListFragment instanceDeliverOrder() {
        TxListFragment fragment = new TxListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TransactionPurchaseRouter.ARG_PARAM_EXTRA_INSTANCE_TYPE, INSTANCE_RECEIVE);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public static TxListFragment instanceAllOrder(String txFilterID) {
        TxListFragment fragment = new TxListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TransactionPurchaseRouter.ARG_PARAM_EXTRA_INSTANCE_TYPE,
                TransactionPurchaseRouter.INSTANCE_ALL);
        bundle.putString(TransactionPurchaseRouter.ARG_PARAM_EXTRA_INSTANCE_FILTER, txFilterID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

        ActionReplacementSourceFactory actionReplacementSourceFactory =
                new ActionReplacementSourceFactory(context);

        ReplacementRepository replacementRepository =
                new ReplacementRepositoryImpl(actionReplacementSourceFactory);
        CancelReplacementUseCase cancelReplacementUseCase =
                new CancelReplacementUseCase(new JobExecutor(),
                        new UIThread(),
                        replacementRepository);

        SessionHandler sessionHandler = new SessionHandler(context);

        presenter = new TxListPresenterImpl(this, cancelReplacementUseCase, sessionHandler);
    }

    @Override
    protected void initialListener(Activity activity) {
        try {
            stateFilterListener = (StateFilterListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        this.typeInstance = arguments.getInt(
                TransactionPurchaseRouter.ARG_PARAM_EXTRA_INSTANCE_TYPE
        );
        this.txFilterID = arguments.getString(
                TransactionPurchaseRouter.ARG_PARAM_EXTRA_INSTANCE_FILTER,
                TransactionPurchaseRouter.ALL_STATUS_FILTER_ID
        );
        this.instanceFromNotification = arguments.getBoolean(
                TransactionPurchaseRouter.ARG_PARAM_EXTRA_INSTANCE_FROM_NOTIFICATION, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && !isLoading && getActivity() != null
                && (txListAdapter == null || txListAdapter.getCount() == 0)) {
            if (stateFilterListener != null) txFilterID = stateFilterListener.getStateTxFilterID();
            allTxFilter.setFilter(txFilterID);
            refreshHandler.startRefresh();
            if (getView() != null) NetworkErrorHelper.hideEmptyState(getView());
        } else if (isVisibleToUser && !isLoading && getActivity() != null) {
            String txFilterBefore = allTxFilter.getFilter();
            if (stateFilterListener != null) txFilterID = stateFilterListener.getStateTxFilterID();
            allTxFilter.setFilter(txFilterID);
            if (FilterUtils.isChangedFilter(txFilterBefore, txFilterID)) {
                resetData();
            }
        }
        if (bottomSheetFilterDialog != null)
            bottomSheetFilterDialog.setStateFilterSelection(txFilterID);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_transaction_list_tx_module;
    }

    @SuppressLint("InflateParams")
    @Override
    protected void initView(View view) {
        loadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_list_view, null);
        progressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        refreshHandler = new RefreshHandler(getActivity(), getView(), this);
        refreshHandler.setPullEnabled(true);
        lvTXList.setAdapter(txListAdapter);
        lvTXList.setOnLazyLoadListener(this);
        fabFilter.setVisibility(typeInstance == TransactionPurchaseRouter.INSTANCE_ALL
                && !instanceFromNotification
                ? View.VISIBLE : View.GONE);
        fabFilter.setOnClickListener(this);
    }

    @Override
    protected void initialVar() {
        txListAdapter = new TxListAdapter(getActivity(), typeInstance, this);
        allTxFilter = AllTxFilter.instanceFirst(txFilterID);
        bottomSheetFilterDialog = new TxBottomSheetFilterDialog(getActivity(), allTxFilter, this);
        txUIReceiver = new TxListUIReceiver(this);
        IntentFilter intentFilter = new IntentFilter(TxListUIReceiver.FILTER_ACTION);
        getActivity().registerReceiver(txUIReceiver, intentFilter);
    }

    @Override
    protected void setActionVar() {
        initialData();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        if (progressDialog != null)
            progressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return null;
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public void renderDataList(List<OrderData> orderDataList, boolean hasNext, int typeRequest) {
        if (refreshHandler.isRefreshing()) {
            refreshHandler.finishRefresh();
            refreshHandler.setPullEnabled(true);
        }
        if (typeRequest == TxOrderNetInteractor.TypeRequest.PULL_REFRESH) {
            pagingHandler.resetPage();
            txListAdapter.clear();
            txListAdapter.notifyDataSetChanged();
        }
        if (hasNext) pagingHandler.nextPage();
        pagingHandler.setHasNext(hasNext);
        txListAdapter.addAll(orderDataList);
        txListAdapter.notifyDataSetChanged();
        isLoading = false;
        isLoadMoreTerminated = false;
        refreshHandler.setPullEnabled(true);
        lvTXList.removeFooterView(loadMoreView);
        if (typeInstance == TransactionPurchaseRouter.INSTANCE_ALL && !instanceFromNotification)
            fabFilter.show();
    }

    @Override
    public void showFailedLoadMoreData(String message) {
        isLoading = false;
        lvTXList.removeFooterView(loadMoreView);
        isLoadMoreTerminated = true;
        View rootView = getView();
        if (rootView != null)
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getData(TxOrderNetInteractor.TypeRequest.LOAD_MORE);
                        }
                    }).showRetrySnackbar();
        if (typeInstance == TransactionPurchaseRouter.INSTANCE_ALL && !instanceFromNotification)
            fabFilter.hide();
    }

    @Override
    public void showFailedPullRefresh(String message) {
        isLoading = false;
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        View rootView = getView();
        if (rootView != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        if (typeInstance == TransactionPurchaseRouter.INSTANCE_ALL && !instanceFromNotification)
            fabFilter.hide();
    }

    @Override
    public void showFailedResetData(String message) {
        isLoading = false;
        lvTXList.removeFooterView(loadMoreView);
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(false);
        if (typeInstance == TransactionPurchaseRouter.INSTANCE_ALL && !instanceFromNotification)
            fabFilter.hide();
        View rootView = getView();
        if (rootView != null)
            NetworkErrorHelper.showEmptyState(
                    getActivity(), getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            refreshHandler.startRefresh();
                            refreshHandler.setPullEnabled(true);
                        }
                    }
            );

    }

    @Override
    public void showNoConnectionLoadMoreData(String message) {
        isLoading = false;
        lvTXList.removeFooterView(loadMoreView);
        isLoadMoreTerminated = true;
        View rootView = getView();
        if (rootView != null)
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getData(TxOrderNetInteractor.TypeRequest.LOAD_MORE);
                        }
                    }).showRetrySnackbar();
        if (typeInstance == TransactionPurchaseRouter.INSTANCE_ALL && !instanceFromNotification)
            fabFilter.hide();
    }

    @Override
    public void showNoConnectionPullRefresh(String message) {
        isLoading = false;
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        View rootView = getView();
        if (rootView != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        if (typeInstance == TransactionPurchaseRouter.INSTANCE_ALL && !instanceFromNotification)
            fabFilter.hide();
    }

    @Override
    public void showNoConnectionResetData(String message) {
        isLoading = false;
        lvTXList.removeFooterView(loadMoreView);
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(false);
        if (typeInstance == TransactionPurchaseRouter.INSTANCE_ALL && !instanceFromNotification)
            fabFilter.hide();
        View rootView = getView();
        if (rootView != null)
            NetworkErrorHelper.showEmptyState(
                    getActivity(), getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            refreshHandler.startRefresh();
                            refreshHandler.setPullEnabled(true);
                        }
                    }
            );
    }

    @Override
    public void showEmptyData(int typeRequest) {
        isLoading = false;
        isLoadMoreTerminated = true;
        lvTXList.removeFooterView(loadMoreView);
        if (refreshHandler.isRefreshing()) refreshHandler.finishRefresh();
        if (typeInstance == TransactionPurchaseRouter.INSTANCE_ALL && !instanceFromNotification)
            fabFilter.show();
        switch (typeRequest) {
            case TxOrderNetInteractor.TypeRequest.INITIAL:
                switch (typeInstance) {
                    case TransactionPurchaseRouter.INSTANCE_ALL:
                        lvTXList.addCustomNoResult(getActivity().getResources()
                                .getString(R.string.error_no_result_date_range)
                                .replace("start", allTxFilter.getDateStart())
                                .replace("end", allTxFilter.getDateEnd()));
                        break;
                    default:
                        lvTXList.addNoResult();
                        break;
                }
                break;
        }
    }

    @Override
    public void showProcessGetData(int typeRequest) {
        lvTXList.removeNoResult();
        switch (typeRequest) {
            case TxOrderNetInteractor.TypeRequest.INITIAL:
                if (!refreshHandler.isRefreshing()) {
                    refreshHandler.setRefreshing(true);
                    refreshHandler.setPullEnabled(false);
                }
                break;
            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                lvTXList.addFooterView(loadMoreView);
                break;
            case TxOrderNetInteractor.TypeRequest.PULL_REFRESH:
                if (!refreshHandler.isRefreshing()) {
                    refreshHandler.setRefreshing(true);
                    refreshHandler.setPullEnabled(false);
                }
                break;
        }
    }

    @Override
    public void resetData() {
        txListAdapter.clear();
        pagingHandler.resetPage();
        txListAdapter.notifyDataSetChanged();
        refreshHandler.startRefresh();
    }

    @Override
    public void actionToDetail(OrderData data) {
        presenter.processToDetailOrder(getActivity(), data, typeInstance);
    }

    @Override
    public void actionInvoice(OrderData data) {
        presenter.processToInvoice(getActivity(), data);
    }

    @Override
    public void actionConfirmDeliver(OrderData data) {
        presenter.processConfirmDeliver(getActivity(), data, typeInstance);
    }

    @Override
    public void actionTrackOrder(OrderData data) {
        presenter.processTrackOrder(getActivity(), data);
    }

    @Override
    public void actionReject(OrderData data) {
        presenter.processRejectOrder(getActivity(), data);
    }

    @Override
    public void actionDispute(OrderData orderData, int state) {
        presenter.processOpenDispute(getActivity(), orderData, state);
    }

    @Override
    public void actionShowComplain(OrderData orderData) {
        presenter.processShowComplain(getActivity(), orderData);
    }

    @Override
    public void actionCopyResiNumber(String resiNumber) {
        if (resiNumber != null) showToastMessage(getString(R.string.copied_to_clipboard));
    }

    @Override
    public void actionCancelReplacement(OrderData orderData) {
        presenter.cancelReplacement(getActivity(), orderData);
    }

    @Override
    public void actionComplain(OrderData orderData) {
        presenter.processComplain(getActivity(), orderData);
    }

    @Override
    public void actionComplainConfirmDeliver(OrderData orderData) {
        presenter.processComplainConfirmDeliver(getActivity(), orderData);
    }

    @Override
    public void onRefresh(View view) {
        if (!isLoading) {
            pagingHandler.resetPage();
            getData(txListAdapter.getCount() == 0 ? TxOrderNetInteractor.TypeRequest.INITIAL
                    : TxOrderNetInteractor.TypeRequest.PULL_REFRESH);
        }
    }

    @Override
    public void onLazyLoad(View view) {
        if (pagingHandler.CheckNextPage() && !isLoading && !isLoadMoreTerminated)
            getData(TxOrderNetInteractor.TypeRequest.LOAD_MORE);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fab_filter)
            bottomSheetFilterDialog.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        bottomSheetFilterDialog.unbindDialog();
        getActivity().unregisterReceiver(txUIReceiver);
    }

    @Override
    public void onFilterSearchButtonClicked(AllTxFilter resultAllTxFilterUpdated) {
        allTxFilter = resultAllTxFilterUpdated;
        bottomSheetFilterDialog.dismiss();
        resetData();
    }

    @Override
    public void forceRefreshListData() {
        resetData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(getActivity(), requestCode, resultCode, data);
        resetData();
        onRefresh(getView());
    }

    private void initialData() {
        if (getUserVisibleHint() && !isLoading && getActivity() != null
                && (txListAdapter == null || txListAdapter.getCount() == 0)) {
            refreshHandler.startRefresh();
        }
    }

    private void getData(int typeRequest) {
        fabFilter.hide();
        if (getView() != null) NetworkErrorHelper.hideEmptyState(getView());
        switch (typeInstance) {
            case TransactionPurchaseRouter.INSTANCE_ALL:
                isLoading = true;
                presenter.getAllOrderData(
                        getActivity(), pagingHandler.getPage(), allTxFilter, typeRequest
                );
                break;
            case INSTANCE_RECEIVE:
                isLoading = true;
                presenter.getDeliverOrderData(getActivity(), pagingHandler.getPage(), typeRequest);
                break;
            case INSTANCE_STATUS:
                isLoading = true;
                presenter.getStatusOrderData(getActivity(), pagingHandler.getPage(), typeRequest);
                break;
        }
    }

    public interface StateFilterListener {
        String getStateTxFilterID();
    }

    @Override
    public void onErrorCancelReplacement(String errorMessage) {
        hideProgressLoading();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessCancelReplacement() {
        if (getView() != null) {
            Snackbar.make(getView(), getString(R.string.success_cancel_replacement), Snackbar
                    .LENGTH_SHORT).show();
            hideProgressLoading();
            onRefresh(getView());
        }
    }

    @Override
    public void showToastSuccessMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}

