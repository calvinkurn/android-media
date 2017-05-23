package com.tokopedia.tkpdpdp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.product.model.productdetail.ShopShipment;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HenryPri on 12/05/17.
 */

public class CourierAdapter extends RecyclerView.Adapter<CourierAdapter.CourierViewHolder> {

    private Context context;

    public CourierAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<ShopShipment> shopShipments = new ArrayList<>();

    @Override
    public CourierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourierViewHolder(LayoutInflater.from(context).inflate(
                R.layout.courier_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CourierViewHolder holder, int position) {
        if (isDataAvailable(position)) {
            holder.bindData(shopShipments.get(position));
        }
    }

    private boolean isDataAvailable(int position) {
        return position < shopShipments.size();
    }

    @Override
    public int getItemCount() {
        return shopShipments.size();
    }

    public void setData(ArrayList<ShopShipment> shopShipments) {
        this.shopShipments = shopShipments;
        notifyDataSetChanged();
    }

    static class CourierViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.courier_item_name)
        TextView name;

        @BindView(R2.id.courier_item_info)
        TextView info;

        @BindView(R2.id.courier_item_image)
        ImageView image;

        public CourierViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(ShopShipment shopShipment) {
            name.setText(shopShipment.getShippingName());
            info.setText(shopShipment.getPackageNames().toString());
            ImageHandler.LoadImage(image, shopShipment.getLogo());
        }
    }
}
