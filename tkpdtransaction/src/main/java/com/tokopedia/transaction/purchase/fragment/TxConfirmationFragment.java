package com.tokopedia.transaction.purchase.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customadapter.LazyListView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.activity.ConfirmPaymentActivity;
import com.tokopedia.transaction.purchase.adapter.TxConfAdapter;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.transaction.purchase.listener.TxConfViewListener;
import com.tokopedia.transaction.purchase.model.response.txconfirmation.TxConfData;
import com.tokopedia.transaction.purchase.presenter.TxConfirmationPresenter;
import com.tokopedia.transaction.purchase.presenter.TxConfirmationPresenterImpl;
import com.tokopedia.transaction.purchase.receiver.TxListUIReceiver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

/**
 * TxConfirmationFragment
 * Created by Angga.Prasetiyo on 13/05/2016.
 */
public class TxConfirmationFragment extends BasePresenterFragment<TxConfirmationPresenter>
        implements TxConfViewListener, LazyListView.LazyLoadListener,
        RefreshHandler.OnRefreshHandlerListener, AbsListView.MultiChoiceModeListener,
        AdapterView.OnItemClickListener, TxListUIReceiver.ActionListener {
    public static final int REQUEST_CONFIRMATION_DETAIL = 0;

    @BindView(R2.id.order_list)
    LazyListView lvTXConf;

    private View loadMoreView;
    private TkpdProgressDialog progressDialog;
    private TxConfAdapter txConfAdapter;

    private PagingHandler pagingHandler = new PagingHandler();
    private RefreshHandler refreshHandler;
    private boolean isLoadMoreTerminated = true;
    private boolean isLoading = false;
    private ActionMode actionMode;

    private Set<TxConfData> txConfDataSelected = new HashSet<>();
    private TxListUIReceiver txUIReceiver;

    @Override
    protected String getScreenName() {
        return null;
    }

    public static TxConfirmationFragment createInstance() {
        return new TxConfirmationFragment();
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
        presenter = new TxConfirmationPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_transaction_confirmation_tx_module;
    }

    @SuppressLint("InflateParams")
    @Override
    protected void initView(View view) {
        loadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_list_view, null);
        progressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        lvTXConf.setOnItemClickListener(this);
        refreshHandler = new RefreshHandler(getActivity(), getView(), this);
        refreshHandler.setPullEnabled(true);
        lvTXConf.setAdapter(txConfAdapter);
        lvTXConf.setOnLazyLoadListener(this);
        lvTXConf.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvTXConf.setMultiChoiceModeListener(this);
    }

    @Override
    protected void initialVar() {
        txConfAdapter = new TxConfAdapter(getActivity());
        txUIReceiver = new TxListUIReceiver(this);
        IntentFilter intentFilter = new IntentFilter(TxListUIReceiver.FILTER_ACTION);
        getActivity().registerReceiver(txUIReceiver, intentFilter);

    }

    @Override
    protected void setActionVar() {
        initialData();
    }

    private void initialData() {
        if (getUserVisibleHint() && !isLoading && getActivity() != null
                && (txConfAdapter == null || txConfAdapter.getCount() == 0)) {
            refreshHandler.startRefresh();
        }
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        if (getView() != null) Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
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
    public void closeView() {

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && !isLoading && getActivity() != null
                && (txConfAdapter == null || txConfAdapter.getCount() == 0)) {
            refreshHandler.startRefresh();
            if (getView() != null) NetworkErrorHelper.hideEmptyState(getView());
        } else {
            if (actionMode != null) {
                closeActionMode();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onLazyLoad(View view) {
        if (pagingHandler.CheckNextPage() && !isLoading && !isLoadMoreTerminated) {
            getData(TxOrderNetInteractor.TypeRequest.LOAD_MORE);
        }
    }

    private void getData(int typeRequest) {
        if (getView() != null) NetworkErrorHelper.hideEmptyState(getView());
        isLoading = true;
        presenter.getPaymentConfirmationData(getActivity(), pagingHandler.getPage(), typeRequest);
    }

    @Override
    public void onRefresh(View view) {
        if (!isLoading) {
            pagingHandler.resetPage();
            getData(txConfAdapter.getCount() == 0 ? TxOrderNetInteractor.TypeRequest.INITIAL
                    : TxOrderNetInteractor.TypeRequest.PULL_REFRESH);
        }
    }

    @Override
    public void renderDataList(List<TxConfData> orderDataList, boolean hasNext, int typeRequest) {
        if (refreshHandler.isRefreshing()) {
            refreshHandler.finishRefresh();
            refreshHandler.setPullEnabled(true);
        }
        if (typeRequest == TxOrderNetInteractor.TypeRequest.PULL_REFRESH) {
            pagingHandler.resetPage();
            txConfAdapter.clear();
            txConfAdapter.notifyDataSetChanged();
        }
        if (hasNext) pagingHandler.nextPage();
        pagingHandler.setHasNext(hasNext);
        txConfAdapter.addAll(orderDataList);
        isLoading = false;
        isLoadMoreTerminated = false;
        refreshHandler.setPullEnabled(true);
        lvTXConf.removeFooterView(loadMoreView);
    }

    @Override
    public void showFailedLoadMoreData(String message) {
        isLoading = false;
        lvTXConf.removeFooterView(loadMoreView);
        isLoadMoreTerminated = true;
        if (getView() != null) NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getData(TxOrderNetInteractor.TypeRequest.LOAD_MORE);
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void showFailedPullRefresh(String message) {
        isLoading = false;
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        if (getView() != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showFailedResetData(String message) {
        isLoading = false;
        lvTXConf.removeFooterView(loadMoreView);
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        lvTXConf.removeNoResult();
        if (getView() != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            refreshHandler.startRefresh();
                        }
                    });
        }
    }

    @Override
    public void showNoConnectionLoadMoreData(String message) {
        isLoading = false;
        lvTXConf.removeFooterView(loadMoreView);
        isLoadMoreTerminated = true;
        if (getView() != null) NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getData(TxOrderNetInteractor.TypeRequest.LOAD_MORE);
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void showNoConnectionPullRefresh(String message) {
        isLoading = false;
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        if (getView() != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showNoConnectionResetData(String message) {
        isLoading = false;
        lvTXConf.removeFooterView(loadMoreView);
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        lvTXConf.removeNoResult();
        if (getView() != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            refreshHandler.startRefresh();
                        }
                    });
        }
    }

    @Override
    public void showEmptyData(int typeRequest) {

        if (getView() != null) {
            NetworkErrorHelper.removeEmptyState(getView());
        }

        isLoading = false;
        lvTXConf.removeFooterView(loadMoreView);
        if (refreshHandler.isRefreshing()) refreshHandler.finishRefresh();
        switch (typeRequest) {
            case TxOrderNetInteractor.TypeRequest.INITIAL:
                lvTXConf.addNoResult();
                break;
        }
    }

    @Override
    public void showProcessGetData(int typeRequest) {
        lvTXConf.removeNoResult();
        switch (typeRequest) {
            case TxOrderNetInteractor.TypeRequest.INITIAL:
                if (!refreshHandler.isRefreshing()) {
                    refreshHandler.setRefreshing(true);
                    refreshHandler.setPullEnabled(false);
                }
                break;
            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                lvTXConf.addFooterView(loadMoreView);
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
        closeActionMode();
        txConfAdapter.clear();
        pagingHandler.resetPage();
        txConfAdapter.notifyDataSetChanged();
        refreshHandler.startRefresh();
    }

    @Override
    public void closeActionMode() {
        if (actionMode != null) actionMode.finish();
    }

    @Override
    public void resetTxConfDataSelected() {
        txConfDataSelected.clear();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) txConfDataSelected.add(txConfAdapter.getItem(position));
        else txConfDataSelected.remove(txConfAdapter.getItem(position));
        txConfAdapter.getItem(position).setChecked(checked);
        mode.setTitle(Integer.toString(txConfDataSelected.size()));
        txConfAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        refreshHandler.setPullEnabled(false);
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.payment_conf_context, menu);
        this.actionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONFIRMATION_DETAIL:
                if (resultCode == ConfirmPaymentActivity.RESULT_FORM_FAILED
                        && data.hasExtra(ConfirmPaymentActivity.EXTRA_MESSAGE_ERROR_GET_FORM)) {
                    NetworkErrorHelper.showSnackbar(getActivity(),
                            data.getStringExtra(ConfirmPaymentActivity.EXTRA_MESSAGE_ERROR_GET_FORM));
                }
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        getActivity().unregisterReceiver(txUIReceiver);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.action_confirm) {
            presenter.processMultiConfirmPayment(getActivity(), txConfDataSelected);
            return true;
        } else if (item.getItemId() == R.id.action_cancel) {
            presenter.processMultipleCancelPayment(getActivity(), txConfDataSelected);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        txConfDataSelected.clear();
        txConfAdapter.clearStateSelected();
        refreshHandler.setPullEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.processToTxConfirmationDetail(getActivity(), txConfAdapter.getItem(position));
    }

    @Override
    public void forceRefreshListData() {
        resetData();
    }
}
