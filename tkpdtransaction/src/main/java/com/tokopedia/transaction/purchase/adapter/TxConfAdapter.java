package com.tokopedia.transaction.purchase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.transaction.purchase.model.response.txconfirmation.TxConfData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TxConfAdapter
 * Created by Angga.Prasetiyo on 12/05/2016.
 */
public class TxConfAdapter extends ArrayAdapter<TxConfData> {
    private final LayoutInflater inflater;
    private final Context context;
    private List<TxConfData> txConfDataList = new ArrayList<>();

    @Override
    public void clear() {
        txConfDataList.clear();
        super.clear();
    }

    public void clearStateSelected() {
        for (int i = 0; i < txConfDataList.size(); i++) {
            txConfDataList.get(i).setChecked(false);
        }
        notifyDataSetChanged();
    }

    public TxConfAdapter(Context context) {
        super(context, R.layout.listview_payment_confirm);
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public void addAll(Collection<? extends TxConfData> collection) {
        txConfDataList.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public void add(TxConfData object) {
        txConfDataList.add(object);
        notifyDataSetChanged();
    }

    @Override
    public TxConfData getItem(int position) {
        return txConfDataList.get(position);
    }

    @Override
    public int getCount() {
        return txConfDataList.size();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_payment_confirm, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TxConfData item = getItem(position);
        holder.tvShopName.setText(MethodChecker.fromHtml(item.getConfirmation().getShopList()));
        holder.tvCreateDate.setText(item.getConfirmation().getCreateTime());
        holder.tvDueDate.setText(item.getConfirmation().getPayDueDate());
        holder.tvTotalLeftAmount.setText(item.getConfirmation().getLeftAmount());

        if (item.isChecked()) {
            holder.holderMain.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.cards_ui_select));
        } else {
            holder.holderMain.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.cards_ui_selected));
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R2.id.shop_name)
        TextView tvShopName;
        @BindView(R2.id.tx_date)
        TextView tvCreateDate;
        @BindView(R2.id.due_date)
        TextView tvDueDate;
        @BindView(R2.id.total_invoice_order)
        TextView tvTotalLeftAmount;
        @BindView(R2.id.main_view)
        View holderMain;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
