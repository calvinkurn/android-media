package com.tokopedia.transaction.purchase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.model.TxSummaryItem;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Angga.Prasetiyo on 07/04/2016.
 */
public class TxSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM_SUMMARY = R.layout.holder_item_transaction_summary_tx_module;

    public final LayoutInflater inflater;
    private List<TxSummaryItem> dataList = new ArrayList<>();
    private Context context;
    private ActionListener actionListener;

    public TxSummaryAdapter(Context context, ActionListener actionListener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = new ArrayList<>();
        this.actionListener = actionListener;
    }

    public void setDataList(List<TxSummaryItem> dataList) {
        this.dataList.clear();
        this.dataList = dataList;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        if (viewType == TYPE_ITEM_SUMMARY) {
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            final TxSummaryItem item = dataList.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionListener != null) actionListener.onItemClicked(item);
                }
            });
            if (item != null) {
                viewHolder.tvName.setText(item.getName());
                viewHolder.tvCount.setText(MessageFormat.format("{0}", item.getCount()));
                viewHolder.tvDesc.setText(item.getDesc());
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM_SUMMARY;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.menu_title)
        TextView tvName;
        @BindView(R2.id.menu_count)
        TextView tvCount;
        @BindView(R2.id.menu_desc)
        TextView tvDesc;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface ActionListener {
        void onItemClicked(TxSummaryItem txSummaryItem);
    }
}
