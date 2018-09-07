package com.tokopedia.transaction.purchase.detail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.detail.editmodel.CourierSelectionModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.CourierServiceModel;

import java.util.List;

/**
 * Created by kris on 1/5/18. Tokopedia
 */

public class OrderServiceAdapter extends RecyclerView.Adapter<OrderServiceAdapter.OrderServiceViewHolder> {

    private List<CourierServiceModel> modelList;

    private String courierId;

    private String courierName;

    private OrderServiceAdapterListener listener;

    public OrderServiceAdapter(
            List<CourierServiceModel> modelList,
            String courierId,
            String courierName,
            OrderServiceAdapterListener listener) {
        this.modelList = modelList;
        this.courierId = courierId;
        this.courierName = courierName;
        this.listener = listener;
    }

    @Override
    public OrderServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_service_adapter, parent, false);
        return new OrderServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderServiceViewHolder holder, int position) {
        final CourierServiceModel courierViewModel = modelList.get(position);
        holder.serviceName.setText(courierViewModel.getServiceName());
        holder.servicePlaceHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourierSelectionModel model = new CourierSelectionModel();
                model.setCourierId(courierId);
                model.setCourierName(courierName);
                model.setServiceId(courierViewModel.getServiceId());
                model.setServiceName(courierViewModel.getServiceName());
                listener.onServiceSelected(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class OrderServiceViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup servicePlaceHolder;

        private TextView serviceName;

        OrderServiceViewHolder(View itemView) {
            super(itemView);
            servicePlaceHolder = itemView.findViewById(R.id.main_place_holder);
            serviceName = itemView.findViewById(R.id.service_name);
        }
    }

    public interface OrderServiceAdapterListener {

        void onServiceSelected(CourierSelectionModel model);

    }

}
