package com.tokopedia.seller.goldmerchant.statistic.view.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.model.GMStatisticTransactionTableModel;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemTransactionTableLeft;
    private final TextView itemTransactionTableRight;

    public GMStatisticTransactionTableViewHolder(View itemView) {
        super(itemView);
        itemTransactionTableLeft = (TextView) itemView.findViewById(R.id.item_transaction_table_left);
        itemTransactionTableRight = (TextView) itemView.findViewById(R.id.item_transaction_table_right);
    }

    public void bindData(GMStatisticTransactionTableModel gmStatisticTransactionTableModel, @GMTransactionTableSortBy int sortBy) {
        final int productId = gmStatisticTransactionTableModel.getProductId();
        itemTransactionTableLeft.setText(gmStatisticTransactionTableModel.productName);
        switch (sortBy){
            case GMTransactionTableSortBy.DELIVERED_AMT:
                itemTransactionTableRight.setText(KMNumbers.formatRupiahString(itemTransactionTableRight.getContext(),
                        gmStatisticTransactionTableModel.getDeliveredAmount()));
                break;
            case GMTransactionTableSortBy.ORDER_SUM:
                itemTransactionTableRight.setText(String.valueOf( gmStatisticTransactionTableModel.getOrderSum()));
                break;
            case GMTransactionTableSortBy.TRANS_SUM:
                itemTransactionTableRight.setText(String.valueOf(gmStatisticTransactionTableModel.getTransSum()));
                break;
        }

        final Context context = itemView.getContext();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductPass productPass = ProductPass.Builder.aProductPass()
                        .setProductId(productId)
                        .build();
                if (context != null && context instanceof PdpRouter) {
                    ((PdpRouter) context).goToProductDetail(context, productPass);
                }

            }
        });

    }
}
