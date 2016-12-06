package com.tokopedia.transaction.purchase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.model.response.txverinvoice.Detail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Angga.Prasetiyo on 13/06/2016.
 */
public class TxVerInvoiceAdapter extends ArrayAdapter<Detail> {

    private final LayoutInflater inflater;

    public TxVerInvoiceAdapter(Context context) {
        super(
                context, R.layout.holder_item_transaction_verification_invoice_tx_module,
                new ArrayList<Detail>()
        );
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.holder_item_transaction_verification_invoice_tx_module, null
            );
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Detail item = getItem(position);
        if (item == null) return convertView;
        holder.tvTitle.setText(item.getInvoice());
        return convertView;
    }

    class ViewHolder {
        @BindView(R2.id.text)
        TextView tvTitle;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
