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

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * @author by HenryPri on 12/05/17.
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

        private TextView name;

        private TextView info;

        private ImageView image;

        CourierViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            name = (TextView) itemView.findViewById(R.id.courier_item_name);
            info = (TextView) itemView.findViewById(R.id.courier_item_info);
            image = (ImageView) itemView.findViewById(R.id.courier_item_image);
        }

        public void bindData(ShopShipment shopShipment) {
            name.setText(shopShipment.getShippingName());
            info.setText(shopShipment.getPackageNames()!=null ? shopShipment.getPackageNames().toString() : "");
            ImageHandler.LoadImage(image, shopShipment.getLogo());
        }
    }
}
