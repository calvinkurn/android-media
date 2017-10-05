package com.tokopedia.tkpdpdp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.product.model.productdetail.ProductWholesalePrice;
import com.tokopedia.tkpdpdp.R;

import java.util.ArrayList;

/**
 * @author by alifa on 5/16/17.
 */

public class WholesaleAdapter extends RecyclerView.Adapter<WholesaleAdapter.WholeSaleViewHolder> {

    private Context context;

    public WholesaleAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<ProductWholesalePrice> wholesalePrices = new ArrayList<>();

    @Override
    public WholesaleAdapter.WholeSaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WholesaleAdapter.WholeSaleViewHolder(LayoutInflater.from(context).inflate(
                R.layout.wholesale_item, parent, false));
    }

    @Override
    public void onBindViewHolder(WholesaleAdapter.WholeSaleViewHolder holder, int position) {
        if (isDataAvailable(position)) {
            holder.bindData(wholesalePrices.get(position));
        }
    }

    private boolean isDataAvailable(int position) {
        return position < wholesalePrices.size();
    }

    @Override
    public int getItemCount() {
        return wholesalePrices.size();
    }

    public void setData(ArrayList<ProductWholesalePrice> shopShipments) {
        this.wholesalePrices = shopShipments;
        notifyDataSetChanged();
    }

    static class WholeSaleViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView info;

        WholeSaleViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.courier_item_name);
            info = (TextView) itemView.findViewById(R.id.courier_item_info);
        }

        public void bindData(ProductWholesalePrice productWholesalePrice) {
            name.setText(productWholesalePrice.getWholesalePrice());
            info.setText("Kisaran Produk " + productWholesalePrice.getWholesaleMin()
                    + "-" + productWholesalePrice.getWholesaleMax());
        }
    }
}

