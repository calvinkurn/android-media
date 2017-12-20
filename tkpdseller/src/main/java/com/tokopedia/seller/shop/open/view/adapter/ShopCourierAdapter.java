package com.tokopedia.seller.shop.open.view.adapter;

import android.view.ViewGroup;

import com.tokopedia.seller.shop.open.data.model.Courier;
import com.tokopedia.seller.shop.open.data.model.CourierService;
import com.tokopedia.seller.shop.open.view.adapter.expandableadapter.ExpandableRecyclerViewAdapter;

import java.util.List;

/**
 * Created by nakama on 19/12/17.
 */

public class ShopCourierAdapter extends ExpandableRecyclerViewAdapter<Courier, CourierTitleViewHolder, CourierItemViewHolder> {
    private List<Courier> courierList;

    public ShopCourierAdapter(List<Courier> courierList) {
        super(courierList);
    }

    @Override
    public int getChildCount(Courier group) {
        List<CourierService> courierServices = group.getServices();
        return courierServices == null? 0: courierServices.size();
    }

    public void setData(List<Courier> courierList){
        this.courierList = courierList;
    }

    @Override
    public CourierTitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public CourierItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindGroupViewHolder(CourierTitleViewHolder holder,
                                      int flatPosition, Courier group) {

    }

    @Override
    public void onBindChildViewHolder(CourierItemViewHolder holder,
                                      int flatPosition,
                                      Courier group, int childIndex) {

    }

}
