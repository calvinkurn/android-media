package com.tokopedia.transaction.purchase.detail.adapter;

import android.content.Context;
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
        return new OrderHistoryViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(OrderHistoryViewHolder holder, int position) {
        holder.orderHistoryTitle.setText(
                historyListDatas.get(position).getActionBy()
                        + " - "
                        + historyListDatas.get(position).getOrderHistoryDate()
        );
        setTitleColor(holder, position);

        holder.orderHistoryComment.setText(historyListDatas.get(position).getOrderHistoryComment());
        holder.orderHistoryComment.setVisibility(historyListDatas.get(position)
                .getOrderHistoryComment().equals("") ? View.GONE : View.VISIBLE);
        holder.orderHistoryDescription
                .setText(Html.fromHtml(historyListDatas.get(position).getOrderHistoryTitle()));
        holder.orderHistoryTime.setText(historyListDatas.get(position).getOrderHistoryTime());
        holder.dot.setColorFilter(Color.parseColor(historyListDatas.get(position).getColor()));
        if(position == historyListDatas.size() - 1) {
            holder.dotTrail.setVisibility(View.GONE);
        } else {
            holder.dotTrail.setVisibility(View.VISIBLE);
            holder.dotTrail.setBackgroundColor(
                    Color.parseColor(historyListDatas.get(position).getColor())
            );
        }
    }

    private void setTitleColor(OrderHistoryViewHolder holder, int position) {
        if(position == 0) {
            holder.orderHistoryTitle.setTextColor((Color.parseColor(
                    historyListDatas.get(position).getColor()
            )));
        } else holder.orderHistoryTitle.setTextColor(
                holder.context.getResources().getColor(R.color.black_70));
    }

    @Override
    public int getItemCount() {
        return historyListDatas.size();
    }

    class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        private TextView orderHistoryTitle;

        private TextView orderHistoryDescription;

        private TextView orderHistoryTime;

        private ImageView dot;

        private View dotTrail;

        private TextView orderHistoryComment;

        OrderHistoryViewHolder(Context context, View itemView) {
            super(itemView);

            this.context = context;

            orderHistoryTitle = itemView.findViewById(R.id.history_title);

            orderHistoryDescription = itemView.findViewById(R.id.history_description);

            orderHistoryTime = itemView.findViewById(R.id.history_date);

            dot = itemView.findViewById(R.id.dot_image);

            dotTrail = itemView.findViewById(R.id.dot_trail);

            orderHistoryComment = itemView.findViewById(R.id.history_comment);

        }
    }
}
