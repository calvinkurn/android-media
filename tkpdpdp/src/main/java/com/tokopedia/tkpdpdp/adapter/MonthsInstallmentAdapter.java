package com.tokopedia.tkpdpdp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.product.model.etalase.MonthsInstallmentItem;
import com.tokopedia.tkpdpdp.R;

import java.util.ArrayList;

/**
 * @author by alifa on 5/17/17.
 */

public class MonthsInstallmentAdapter extends RecyclerView.Adapter<MonthsInstallmentAdapter.MonthsInstallmentViewHolder> {

    private static final String MIN_PURCHASE = "Min Pembelanjaan ";

    private Context context;

    public MonthsInstallmentAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<MonthsInstallmentItem> monthsInstallmentItems = new ArrayList<>();

    @Override
    public MonthsInstallmentAdapter.MonthsInstallmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MonthsInstallmentAdapter.MonthsInstallmentViewHolder(LayoutInflater.from(context).inflate(
                R.layout.month_installment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MonthsInstallmentAdapter.MonthsInstallmentViewHolder holder, int position) {
        if (isDataAvailable(position)) {
            holder.bindData(monthsInstallmentItems.get(position));
        }
    }

    private boolean isDataAvailable(int position) {
        return position < monthsInstallmentItems.size();
    }

    @Override
    public int getItemCount() {
        return monthsInstallmentItems.size();
    }

    public void setData(ArrayList<MonthsInstallmentItem> monthsInstallmentItems) {
        this.monthsInstallmentItems = monthsInstallmentItems;
        notifyDataSetChanged();
    }

    static class MonthsInstallmentViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        private TextView info;

        private ImageView image;

        MonthsInstallmentViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.courier_item_name);
            info = (TextView) itemView.findViewById(R.id.courier_item_info);
            image = (ImageView) itemView.findViewById(R.id.courier_item_image);
        }

        public void bindData(MonthsInstallmentItem monthsInstallmentItem) {
            name.setText(monthsInstallmentItem.getValue());
            String minPurchaseValue = MIN_PURCHASE + monthsInstallmentItem.getInfo();
            info.setText(minPurchaseValue);
            ImageHandler.LoadImage(image, monthsInstallmentItem.getImageUrl());
        }
    }
}
