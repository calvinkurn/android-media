package com.tokopedia.transaction.purchase.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.transaction.purchase.model.TxSummaryItem;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TxSummaryAdapter
 * Created by Angga.Prasetiyo on 07/04/2016.
 */
public class TxSummaryAdapter extends ArrayAdapter<TxSummaryItem> {
    public final LayoutInflater inflater;
    private List<TxSummaryItem> dataList = new ArrayList<>();

    public TxSummaryAdapter(Context context) {
        super(context, R.layout.gridview_tx_center);
        this.inflater = LayoutInflater.from(context);
        this.dataList = new ArrayList<>();
    }

    public void setDataList(List<TxSummaryItem> dataList) {
        this.dataList.clear();
        this.dataList = dataList;
    }

    @Override
    public TxSummaryItem getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_tx_center, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(getItem(position).getName());
        holder.tvCount.setText(MessageFormat.format("{0}", getItem(position).getCount()));
        holder.tvDesc.setText(getItem(position).getDesc());
        return convertView;
    }


    class ViewHolder {
        @BindView(R2.id.menu_title)
        TextView tvName;
        @BindView(R2.id.menu_count)
        TextView tvCount;
        @BindView(R2.id.menu_desc)
        TextView tvDesc;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
