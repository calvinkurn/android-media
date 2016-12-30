package com.tokopedia.transaction.purchase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Angga.Prasetiyo on 02/05/2016.
 */
public class HistoryListAdapter extends ArrayAdapter<OrderHistory> {
    private final LayoutInflater inflater;
    private final Context context;

    public HistoryListAdapter(Context context) {
        super(context, R.layout.holder_item_history_transaction_tx_module,
                new ArrayList<OrderHistory>());
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @SuppressWarnings("deprecation")
    @NonNull
    @SuppressLint({"InflateParams", "NewApi"})
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.holder_item_history_transaction_tx_module, null
            );
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderHistory item = getItem(position);
        if (item == null) return convertView;
        if (item.getHistoryActionBy().equalsIgnoreCase(
                context.getString(R.string.label_history_transaction_actor_tokopedia)
        ) || item.getHistoryActionBy().equalsIgnoreCase(
                context.getString(R.string.label_history_transaction_actor_system_tracker)
        ))
            holder.tvActor.setBackgroundColor(
                    context.getResources().getColor(R.color.tkpd_dark_gray)
            );
        else if (item.getHistoryActionBy().equalsIgnoreCase(
                context.getString(R.string.label_history_transaction_actor_buyer)
        ))
            holder.tvActor.setBackgroundColor(
                    context.getResources().getColor(R.color.tkpd_dark_orange)
            );
        else if (item.getHistoryActionBy().equalsIgnoreCase(
                context.getString(R.string.label_history_transaction_actor_seller)
        ))
            holder.tvActor.setBackgroundColor(
                    context.getResources().getColor(R.color.tkpd_dark_green)
            );

        holder.tvComment.setText(item.getHistoryComments().replaceAll("<br/>\\p{Space}+", "\n"));
        holder.tvComment.setVisibility(item.getHistoryComments().equals("0")
                ? View.GONE : View.VISIBLE);
        holder.tvActor.setText(MethodChecker.fromHtml(item.getHistoryActionBy()));
        holder.tvDate.setText(MethodChecker.fromHtml(item.getHistoryStatusDateFull()));
        holder.tvStatus.setText(MethodChecker.fromHtml(item.getHistoryBuyerStatus()));

        return convertView;
    }

    class ViewHolder {
        @BindView(R2.id.actor)
        TextView tvActor;
        @BindView(R2.id.date)
        TextView tvDate;
        @BindView(R2.id.state)
        TextView tvStatus;
        @BindView(R2.id.comment)
        TextView tvComment;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
