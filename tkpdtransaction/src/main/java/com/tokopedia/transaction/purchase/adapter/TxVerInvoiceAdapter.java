package com.tokopedia.transaction.purchase.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.model.response.txverinvoice.Detail;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Angga.Prasetiyo on 13/06/2016.
 */
public class TxVerInvoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_VIEW_INVOICE
            = R.layout.holder_item_transaction_verification_invoice_tx_module;

    private List<Detail> dataList = new ArrayList<>();
    private final ActionListener actionListener;

    public TxVerInvoiceAdapter(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == TYPE_VIEW_INVOICE) {
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            final Detail detail = dataList.get(position);
            viewHolder.tvTitle.setText(detail.getInvoice());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionListener != null) actionListener.onInvoiceItemClicked(detail);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_VIEW_INVOICE;
    }

    public void addAllInvoiceList(List<Detail> detail) {
        this.dataList.addAll(detail);
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.text)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ActionListener {
        void onInvoiceItemClicked(Detail detailInvoice);
    }
}
