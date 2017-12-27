package com.tokopedia.seller.shop.setting.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.open.data.model.CourierServiceModel;

import java.util.ArrayList;
import java.util.List;


public class ShopServiceCourierAdapter extends RecyclerView.Adapter<ShopServiceCourierAdapter.ShopServiceCourierViewHolder> {
    private Context context;
    private List<CourierServiceModel> courierServiceModelList;

    public ShopServiceCourierAdapter(Context context, List<CourierServiceModel> courierServiceModelList){
        this.context = context;
        setCourierServiceModelList(courierServiceModelList);
    }

    public void setCourierServiceModelList(List<CourierServiceModel> courierServiceModelList) {
        this.courierServiceModelList = (courierServiceModelList == null? new ArrayList<CourierServiceModel>(): courierServiceModelList);
    }

    @Override
    public ShopServiceCourierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_courier_service, parent, false);
        return new ShopServiceCourierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopServiceCourierViewHolder holder, int position) {
        CourierServiceModel courierServiceModel = courierServiceModelList.get(position);
        holder.bindView(courierServiceModel);
    }

    @Override
    public int getItemCount() {
        return courierServiceModelList.size();
    }

    class ShopServiceCourierViewHolder extends RecyclerView.ViewHolder{
        private CheckBox checkBox;
        public ShopServiceCourierViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_box);
        }

        public void bindView(CourierServiceModel courierServiceModel){
            checkBox.setText(courierServiceModel.getName());

        }
    }
}
