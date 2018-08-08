package com.tokopedia.tkpdpdp.estimasiongkir.presentation.viewholder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.estimasiongkir.ShippingServiceModel;
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.adapter.ServiceProductAdapter;

public class RatesEstimationServiceViewHolder extends RecyclerView.ViewHolder{
    private TextView serviceTitle;
    private ServiceProductAdapter adapter;

    public RatesEstimationServiceViewHolder(View itemView) {
        super(itemView);
        serviceTitle = itemView.findViewById(R.id.service_title);
        RecyclerView serviceProductList = itemView.findViewById(R.id.service_product_list);
        serviceProductList.setNestedScrollingEnabled(false);
        serviceProductList.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new ServiceProductAdapter();
        serviceProductList.setAdapter(adapter);
    }

    public void bind(ShippingServiceModel shippingServiceModel){
        serviceTitle.setText(itemView.getContext().getString(R.string.service_title_format,
                shippingServiceModel.getName(), shippingServiceModel.getEtd()));
        adapter.replaceProducts(shippingServiceModel.getProducts());
    }
}
