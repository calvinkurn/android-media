package com.tokopedia.seller.shop.open.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tokopedia.seller.R;
import com.tokopedia.seller.logistic.model.CourierServiceModel;

import java.util.ArrayList;
import java.util.List;


public class ShopOpenCourierAdapter extends RecyclerView.Adapter<ShopOpenCourierAdapter.ShopServiceCourierViewHolder> {
    private Context context;
    private List<CourierServiceModel> courierServiceModelList;
    private List<String> selectedIds;

    private OnShopServiceCourierAdapterListener onShopServiceCourierAdapterListener;
    public interface OnShopServiceCourierAdapterListener{
        void checkGroupFromChild();
        void unCheckGroupFromChild();
        void onInfoIconClicked(String title, String description);
    }

    public ShopOpenCourierAdapter(Context context, List<CourierServiceModel> courierServiceModelList,
                                  List<String> selectedIds, OnShopServiceCourierAdapterListener listener) {
        this.context = context;
        setCourierServiceModelList(courierServiceModelList);
        setSelectedIds(selectedIds);
        this.onShopServiceCourierAdapterListener = listener;
    }

    public void setCourierServiceModelList(List<CourierServiceModel> courierServiceModelList) {
        this.courierServiceModelList = (courierServiceModelList == null ? new ArrayList<CourierServiceModel>() : courierServiceModelList);
    }

    public void setSelectedIds(List<String> selectedIds) {
        this.selectedIds = (selectedIds == null ? new ArrayList<String>() : selectedIds);
    }

    public List<String> getSelectedIds(){
        return this.selectedIds;
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

    class ShopServiceCourierViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener,
    View.OnClickListener{
        private CheckBox checkBox;
        private View vIconInfo;

        public ShopServiceCourierViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_box);
            checkBox.setOnCheckedChangeListener(this);

            vIconInfo = itemView.findViewById(R.id.iv_info);
            vIconInfo.setOnClickListener(this);
        }

        public void bindView(CourierServiceModel courierServiceModel) {
            checkBox.setText(courierServiceModel.getName());
            if (selectedIds.contains(String.valueOf(courierServiceModel.getId()))){
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            String description = courierServiceModel.getDescription();
            if (TextUtils.isEmpty(description)) {
                vIconInfo.setVisibility(View.GONE);
            } else {
                vIconInfo.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = getAdapterPosition();
            CourierServiceModel courierServiceModel = courierServiceModelList.get(position);
            String checkedId = String.valueOf(courierServiceModel.getId());
            if (isChecked) {
                if (selectedIds.size() == 0) {
                    onShopServiceCourierAdapterListener.checkGroupFromChild();
                }
                if (!selectedIds.contains(checkedId)) {
                    selectedIds.add(checkedId);
                }
            } else {
                selectedIds.remove(checkedId);
                if (selectedIds.size() == 0) {
                    onShopServiceCourierAdapterListener.unCheckGroupFromChild();
                }
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CourierServiceModel courierServiceModel = courierServiceModelList.get(position);
            onShopServiceCourierAdapterListener.onInfoIconClicked(courierServiceModel.getName(),
                    courierServiceModel.getDescription());
        }
    }

    public void checkAll() {
        selectedIds = new ArrayList<>();
        for (int i = 0, sizei = courierServiceModelList.size(); i < sizei; i++) {
            selectedIds.add(String.valueOf(courierServiceModelList.get(i).getId()));
        }
        notifyDataSetChanged();
    }

    public void unCheckAll() {
        setSelectedIds(null);
        notifyDataSetChanged();
    }
}
