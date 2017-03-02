package com.tokopedia.transaction.purchase.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.adapter.TxSummaryAdapter;
import com.tokopedia.transaction.purchase.listener.TxSummaryViewListener;
import com.tokopedia.transaction.purchase.model.TxSummaryItem;
import com.tokopedia.transaction.purchase.presenter.TxSummaryPresenter;
import com.tokopedia.transaction.purchase.presenter.TxSummaryPresenterImpl;

import java.util.List;

import butterknife.BindView;

/**
 * @author Angga.Prasetiyo on 07/04/2016.
 */
public class TxSummaryFragment extends BasePresenterFragment<TxSummaryPresenter> implements
        TxSummaryViewListener, RefreshHandler.OnRefreshHandlerListener,
        TxSummaryAdapter.ActionListener {
    private static final String EXTRA_INSTANCE_TYPE = "EXTRA_INSTANCE_TYPE";
    private OnCenterMenuClickListener listener;
    public static int INSTANCE_TYPE_PURCHASE = 1;
    public static int INSTANCE_TYPE_SALES = 2;

    @BindView(R2.id.menu_list)
    RecyclerView rvSummary;
    private TxSummaryAdapter summaryAdapter;
    private int instanceType;
    private RefreshHandler refreshHandler;

    @Override
    public void renderPurchaseSummary(List<TxSummaryItem> summaryItemList) {
        summaryAdapter.setDataList(summaryItemList);
        summaryAdapter.notifyDataSetChanged();
        refreshHandler.setPullEnabled(true);
        refreshHandler.finishRefresh();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onRefresh(View view) {
        presenter.getNotificationFromNetwork(getActivity());
        refreshHandler.setPullEnabled(false);
    }

    public static TxSummaryFragment createInstancePurchase() {
        TxSummaryFragment fragment = new TxSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_INSTANCE_TYPE, INSTANCE_TYPE_PURCHASE);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressWarnings("unused")
    public static TxSummaryFragment createInstanceSales() {
        TxSummaryFragment fragment = new TxSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_INSTANCE_TYPE, INSTANCE_TYPE_SALES);
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
        if (presenter == null) presenter = new TxSummaryPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        listener = (OnCenterMenuClickListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        instanceType = arguments.getInt(EXTRA_INSTANCE_TYPE, 1);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_transaction_summary_tx_module;
    }

    @Override
    protected void initView(View view) {
        summaryAdapter = new TxSummaryAdapter(getActivity(), this);
        refreshHandler = new RefreshHandler(getActivity(), getView(), this);
        rvSummary.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void setViewListener() {
        rvSummary.setAdapter(summaryAdapter);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        presenter.getNotificationFromNetwork(context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (presenter == null) {
            initialPresenter();
        }
        if (isVisibleToUser && getActivity() != null)
            presenter.getNotificationPurcase(getActivity());
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onItemClicked(TxSummaryItem txSummaryItem) {
        switch (txSummaryItem.getIndex()) {
            case TransactionPurchaseRouter.TAB_POSITION_PURCHASE_VERIFICATION:
                listener.OnMenuClick(
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_VERIFICATION,
                        TransactionPurchaseRouter.ALL_STATUS_FILTER_ID
                );
                break;
            case TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER:
                listener.OnMenuClick(
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER,
                        TransactionPurchaseRouter.ALL_STATUS_FILTER_ID
                );
                break;
            case TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVER_ORDER:
                listener.OnMenuClick(
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVER_ORDER,
                        TransactionPurchaseRouter.ALL_STATUS_FILTER_ID
                );
                break;
            case TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER:
                listener.OnMenuClick(
                        TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER,
                        TransactionPurchaseRouter.TRANSACTION_CANCELED_FILTER_ID
                );
                break;
        }
    }

    public interface OnCenterMenuClickListener {
        void OnMenuClick(int position, String stateTxFilter);
    }
}
