package com.tokopedia.transaction.purchase.detail.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryListData;

import java.util.List;

/**
 * Created by kris on 11/8/17. Tokopedia
 */

public class OrderHistoryAdapter extends RecyclerView
        .Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{

    private List<OrderHistoryListData> historyListDatas;

    public OrderHistoryAdapter(List<OrderHistoryListData> historyListDatas) {
        this.historyListDatas = historyListDatas;
    }

    @Override
    public OrderHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_history_adapter, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHistoryViewHolder holder, int position) {
        holder.orderHistoryTitle.setText(
                historyListDatas.get(position).getActionBy()
                        + " - "
                        + historyListDatas.get(position).getOrderHistoryDate()
        );
        holder.orderHistoryComment.setText(historyListDatas.get(position).getOrderHistoryComment());
        holder.orderHistoryComment.setVisibility(historyListDatas.get(position)
                .getOrderHistoryComment().equals("") ? View.GONE : View.VISIBLE);
        holder.orderHistoryDescription
                .setText(Html.fromHtml(historyListDatas.get(position).getOrderHistoryTitle()));
        holder.orderHistoryTime.setText(historyListDatas.get(position).getOrderHistoryTime());
        holder.dot.setColorFilter(Color.parseColor(historyListDatas.get(position).getColor()));
        holder.dotTrail
                .setBackgroundColor(Color.parseColor(historyListDatas.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return historyListDatas.size();
    }

    class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView orderHistoryTitle;

        private TextView orderHistoryDescription;

        private TextView orderHistoryTime;

        private ImageView dot;

        private View dotTrail;

        private TextView orderHistoryComment;

        OrderHistoryViewHolder(View itemView) {
            super(itemView);

            orderHistoryTitle = (TextView) itemView.findViewById(R.id.history_title);

            orderHistoryDescription = (TextView) itemView.findViewById(R.id.history_description);

            orderHistoryTime = (TextView) itemView.findViewById(R.id.history_date);

            dot = (ImageView) itemView.findViewById(R.id.dot_image);

            dotTrail = itemView.findViewById(R.id.dot_trail);

            orderHistoryComment = itemView.findViewById(R.id.history_comment);

        }
    }
}
