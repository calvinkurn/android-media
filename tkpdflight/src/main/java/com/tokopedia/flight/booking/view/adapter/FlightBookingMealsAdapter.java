package com.tokopedia.flight.booking.view.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;

import java.util.HashSet;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsAdapter extends BaseListCheckableAdapter<FlightBookingMealViewModel> {
    public FlightBookingMealsAdapter(OnBaseListV2AdapterListener<FlightBookingMealViewModel> onBaseListV2AdapterListener) {
        super(onBaseListV2AdapterListener);
    }

    public FlightBookingMealsAdapter(OnBaseListV2AdapterListener<FlightBookingMealViewModel> onBaseListV2AdapterListener, OnCheckableAdapterListener<FlightBookingMealViewModel> onCheckableAdapterListener) {
        super(onBaseListV2AdapterListener, onCheckableAdapterListener);
    }

    public FlightBookingMealsAdapter(@Nullable List<FlightBookingMealViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<FlightBookingMealViewModel> onBaseListV2AdapterListener, OnCheckableAdapterListener<FlightBookingMealViewModel> onCheckableAdapterListener) {
        super(data, rowPerPage, onBaseListV2AdapterListener, onCheckableAdapterListener);
    }

    @Override
    public CheckableBaseViewHolder<FlightBookingMealViewModel> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightBookingMealsViewHolder(getLayoutView(parent, R.layout.item_flight_booking_meals), this);
    }

    public List<FlightBookingMealViewModel> getListChecked() {
        return getCheckedDataList();
    }

    public void setListChecked(List<FlightBookingMealViewModel> list) {
        List<FlightBookingMealViewModel> checkedDataList = getCheckedDataList();
        if (checkedDataList == null || checkedDataList.size() == 0) {
            return;
        }
        HashSet<Integer> checkedPositionList = new HashSet<>();
        for (int i = 0, sizei = checkedDataList.size(); i<sizei; i++) {
            if (list.contains(checkedDataList.get(i))) {
                checkedPositionList.add(i);
            }
        }
        setCheckedPositionList(checkedPositionList);

    }
}
