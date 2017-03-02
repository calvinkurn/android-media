package com.tokopedia.transaction.purchase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Angga.Prasetiyo on 02/05/2016.
 */
public class TxHistoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM_HISTORY = R.layout.holder_item_history_transaction_tx_module;

    private final Context context;
    private List<Object> dataList = new ArrayList<>();

    public TxHistoryListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        if (viewType == TYPE_ITEM_HISTORY) {
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final OrderHistory item = (OrderHistory) dataList.get(position);
            final ViewHolder holderView = (ViewHolder) holder;
            if (item.getHistoryActionBy().equalsIgnoreCase(
                    context.getString(R.string.label_history_transaction_actor_tokopedia)
            ) || item.getHistoryActionBy().equalsIgnoreCase(
                    context.getString(R.string.label_history_transaction_actor_system_tracker)
            ))
                holderView.tvActor.setBackgroundColor(
                        context.getResources().getColor(R.color.tkpd_dark_gray)
                );
            else if (item.getHistoryActionBy().equalsIgnoreCase(
                    context.getString(R.string.label_history_transaction_actor_buyer)
            ))
                holderView.tvActor.setBackgroundColor(
                        context.getResources().getColor(R.color.tkpd_dark_orange)
                );
            else if (item.getHistoryActionBy().equalsIgnoreCase(
                    context.getString(R.string.label_history_transaction_actor_seller)
            ))
                holderView.tvActor.setBackgroundColor(
                        context.getResources().getColor(R.color.tkpd_dark_green)
                );

            holderView.tvComment.setText(
                    item.getHistoryComments().replaceAll("<br/>\\p{Space}+", "\n")
            );
            holderView.tvComment.setVisibility(item.getHistoryComments().equals("0")
                    ? View.GONE : View.VISIBLE);
            holderView.tvActor.setText(MethodChecker.fromHtml(item.getHistoryActionBy()));
            holderView.tvDate.setText(MethodChecker.fromHtml(item.getHistoryStatusDateFull()));
            holderView.tvStatus.setText(MethodChecker.fromHtml(item.getHistoryBuyerStatus()));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position) instanceof OrderHistory
                ? TYPE_ITEM_HISTORY : super.getItemViewType(position);
    }

    public void addAllDataList(List<OrderHistory> dataList) {
        this.dataList.addAll(dataList);
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.actor)
        TextView tvActor;
        @BindView(R2.id.date)
        TextView tvDate;
        @BindView(R2.id.state)
        TextView tvStatus;
        @BindView(R2.id.comment)
        TextView tvComment;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
