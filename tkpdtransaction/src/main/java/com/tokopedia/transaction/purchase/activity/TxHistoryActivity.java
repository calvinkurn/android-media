package com.tokopedia.transaction.purchase.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.adapter.TxHistoryListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Angga.Prasetiyo on 02/05/2016.
 */
public class TxHistoryActivity extends TActivity {
    private static final String EXTRA_ORDER_HISTORIES = "EXTRA_ORDER_HISTORIES";

    public static Intent createInstance(Context context, List<OrderHistory> orderHistoryList) {
        Intent intent = new Intent(context, TxHistoryActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_ORDER_HISTORIES,
                new ArrayList<>(orderHistoryList));
        return intent;
    }

    @BindView(R2.id.order_status)
    RecyclerView rvHistory;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_ORDER_HISTORY_DETAIL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_transaction_history_tx_module);
        ButterKnife.bind(this);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        TxHistoryListAdapter adapter = new TxHistoryListAdapter(this);
        rvHistory.setAdapter(adapter);
        List<OrderHistory> orderHistories =
                getIntent().getParcelableArrayListExtra(EXTRA_ORDER_HISTORIES);
        adapter.addAllDataList(orderHistories);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
