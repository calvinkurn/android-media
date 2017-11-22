package com.tokopedia.flight.booking.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsAdapter extends BaseListCheckableAdapter<FlightBookingMealViewModel> {
    public FlightBookingMealsAdapter(Context context, OnBaseListV2AdapterListener<FlightBookingMealViewModel> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    public FlightBookingMealsAdapter(Context context, OnBaseListV2AdapterListener<FlightBookingMealViewModel> onBaseListV2AdapterListener, OnCheckableAdapterListener<FlightBookingMealViewModel> onCheckableAdapterListener) {
        super(context, onBaseListV2AdapterListener, onCheckableAdapterListener);
    }

    public FlightBookingMealsAdapter(Context context, @Nullable List<FlightBookingMealViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<FlightBookingMealViewModel> onBaseListV2AdapterListener, OnCheckableAdapterListener<FlightBookingMealViewModel> onCheckableAdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener, onCheckableAdapterListener);
    }

    @Override
    public CheckableBaseViewHolder<FlightBookingMealViewModel> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightBookingMealsViewHolder(getLayoutView(parent, R.layout.item_flight_booking_meals), this);
    }

    public ArrayList<String> getListChecked() {
        List<FlightBookingMealViewModel> list = getCheckedDataList();
        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        }
        ArrayList<String> idCheckedList = new ArrayList<>();
        for (int i = 0, sizei = list.size(); i < sizei; i++) {
            idCheckedList.add(list.get(i).getId());
        }
        return idCheckedList;
    }

    public void setListChecked(List<String> list) {
        List<FlightBookingMealViewModel> checkedDataList = getCheckedDataList();
        if (checkedDataList == null || checkedDataList.size() == 0) {
            return;
        }
        HashSet<Integer> checkedPositionList = new HashSet<>();
        for (int i = 0, sizei = checkedDataList.size(); i < sizei; i++) {
            if (list.contains(checkedDataList.get(i).getId())) {
                checkedPositionList.add(i);
            }
        }
        setCheckedPositionList(checkedPositionList);

    }
}
