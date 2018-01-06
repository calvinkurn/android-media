package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.OrderCourierAdapter;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.CourierViewModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public class CourierSelectionFragment extends Fragment implements OrderCourierAdapter.OrderCourierAdapterListener{

    private static final String ORDER_COURIER_EXTRAS = "ORDER_COURIER_EXTRAS";

    public static CourierSelectionFragment createInstance(ListCourierViewModel model) {
        CourierSelectionFragment fragment = new CourierSelectionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDER_COURIER_EXTRAS, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courier_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.courier_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new OrderCourierAdapter((ListCourierViewModel)getArguments()
                .getParcelable(ORDER_COURIER_EXTRAS), this));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCourierSelected(CourierViewModel courierViewModel) {
        ServiceSelectionFragment serviceSelectionFragment = ServiceSelectionFragment
                .createFragment(courierViewModel);
    }
}
