package com.tokopedia.transaction.purchase.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.transaction.purchase.adapter.TxSummaryAdapter;
import com.tokopedia.transaction.purchase.listener.TxSummaryViewListener;
import com.tokopedia.transaction.purchase.model.TxSummaryItem;
import com.tokopedia.transaction.purchase.presenter.TxSummaryPresenter;
import com.tokopedia.transaction.purchase.presenter.TxSummaryPresenterImpl;

import java.util.List;

import butterknife.BindView;

/**
 * TxSummaryFragment
 * Created by Angga.Prasetiyo on 07/04/2016.
 */
public class TxSummaryFragment extends BasePresenterFragment<TxSummaryPresenter> implements
        AdapterView.OnItemClickListener, TxSummaryViewListener,
        RefreshHandler.OnRefreshHandlerListener {
    private static final String EXTRA_INSTANCE_TYPE = "EXTRA_INSTANCE_TYPE";
    private OnCenterMenuClickListener listener;
    public static int INSTANCE_TYPE_PURCHASE = 1;
    public static int INSTANCE_TYPE_SALES = 2;

    @BindView(R2.id.menu_list)
    ListView lvSummary;
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
    public void onRefresh(View view) {
        presenter.getNotificationFromNetwork(getActivity());
        refreshHandler.setPullEnabled(false);
    }


    public interface OnCenterMenuClickListener {
        void OnMenuClick(int position, String stateTxFilter);
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
        return R.layout.fragment_people_tx_center;
    }

    @Override
    protected void initView(View view) {
        summaryAdapter = new TxSummaryAdapter(getActivity());
        refreshHandler = new RefreshHandler(getActivity(), getView(), this);
    }

    @Override
    protected void setViewListener() {
        lvSummary.setAdapter(summaryAdapter);
        lvSummary.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (instanceType == INSTANCE_TYPE_PURCHASE) {
            switch (position) {
                case 0:
                    listener.OnMenuClick(TransactionPurchaseRouter.TAB_TX_CONFIRMATION,
                            TransactionPurchaseRouter.ALL_STATUS_FILTER_ID);
                    break;
                case 1:
                    listener.OnMenuClick(TransactionPurchaseRouter.TAB_TX_VERIFICATION,
                            TransactionPurchaseRouter.ALL_STATUS_FILTER_ID);
                    break;
                case 2:
                    listener.OnMenuClick(TransactionPurchaseRouter.TAB_TX_STATUS,
                            TransactionPurchaseRouter.ALL_STATUS_FILTER_ID);
                    break;
                case 3:
                    listener.OnMenuClick(TransactionPurchaseRouter.TAB_TX_DELIVER,
                            TransactionPurchaseRouter.ALL_STATUS_FILTER_ID);
                    break;
                case 4:
                    listener.OnMenuClick(TransactionPurchaseRouter.TAB_TX_ALL,
                            TransactionPurchaseRouter.TRANSACTION_CANCELED_FILTER_ID);
                    break;
            }
        }
    }
}
