package com.tokopedia.tkpdpdp.estimasiongkir.presentation.viewholder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.estimasiongkir.ShippingServiceModel;

public class RatesEstimationServiceViewHolder extends RecyclerView.ViewHolder{
    private TextView serviceTitle;
    private RecyclerView serviceProductList;

    public RatesEstimationServiceViewHolder(View itemView) {
        super(itemView);
        serviceTitle = itemView.findViewById(R.id.service_title);
        serviceProductList = itemView.findViewById(R.id.service_product_list);
        serviceProductList.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void bind(ShippingServiceModel shippingServiceModel){
        serviceTitle.setText(itemView.getContext().getString(R.string.service_title_format,
                shippingServiceModel.getName(), shippingServiceModel.getEtd()));
    }
}
