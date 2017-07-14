package com.tokopedia.seller.goldmerchant.statistic.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.model.GMStatisticTransactionTableModel;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemTransactionTableLeft;
    private final TextView itemTransactionTableRight;
    private GMStatisticTransactionTableModel gmStatisticTransactionTableModel;

    public GMStatisticTransactionTableViewHolder(View itemView) {
        super(itemView);

        itemTransactionTableLeft = (TextView) itemView.findViewById(R.id.item_transaction_table_left);
        itemTransactionTableRight = (TextView) itemView.findViewById(R.id.item_transaction_table_right);
    }

    public void bindData(GMStatisticTransactionTableModel gmStatisticTransactionTableModel) {
        this.gmStatisticTransactionTableModel = gmStatisticTransactionTableModel;

        itemTransactionTableLeft.setText(gmStatisticTransactionTableModel.leftText);
        itemTransactionTableRight.setText(gmStatisticTransactionTableModel.rightText);
    }
}
