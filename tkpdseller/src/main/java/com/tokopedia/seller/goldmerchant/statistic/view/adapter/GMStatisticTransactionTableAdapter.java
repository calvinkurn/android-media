package com.tokopedia.seller.goldmerchant.statistic.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.model.GMStatisticTransactionTableModel;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.viewholder.GMStatisticTransactionTableViewHolder;

/**
 * @author normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableAdapter extends BaseListAdapter<GMStatisticTransactionTableModel> {
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        switch (getItemViewType(position)) {
            case GMStatisticTransactionTableModel.TYPE:
                ((GMStatisticTransactionTableViewHolder) holder).bindData(data.get(position));
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GMStatisticTransactionTableModel.TYPE:
                return new GMStatisticTransactionTableViewHolder(getLayoutView(parent, R.layout.item_transaction_table));
        }
        return super.onCreateViewHolder(parent, viewType);
    }
}
