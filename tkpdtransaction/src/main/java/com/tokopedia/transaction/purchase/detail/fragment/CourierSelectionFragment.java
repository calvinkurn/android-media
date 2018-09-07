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

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.OrderCourierAdapter;
import com.tokopedia.transaction.purchase.detail.model.detail.editmodel.CourierSelectionModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.CourierViewModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;
import com.tokopedia.transaction.purchase.listener.ToolbarChangeListener;

import static com.tokopedia.transaction.purchase.detail.activity.ConfirmShippingActivity.SELECT_SERVICE_FRAGMENT_TAG;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public class CourierSelectionFragment extends TkpdFragment implements OrderCourierAdapter.OrderCourierAdapterListener{

    private static final String ORDER_COURIER_EXTRAS = "ORDER_COURIER_EXTRAS";

    private OrderCourierFragmentListener listener;

    private ToolbarChangeListener toolbarListener;

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
        toolbarListener.onChangeTitle(getString(R.string.label_select_courier));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OrderCourierFragmentListener) context;
        toolbarListener = (ToolbarChangeListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OrderCourierFragmentListener) activity;
        toolbarListener = (ToolbarChangeListener) activity;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCourierSelected(CourierViewModel courierViewModel) {
        if(courierViewModel.getCourierServiceList().size() > 1) {
            ServiceSelectionFragment serviceSelectionFragment = ServiceSelectionFragment
                    .createFragment(courierViewModel);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_left)
                    .add(R.id.main_view, serviceSelectionFragment, SELECT_SERVICE_FRAGMENT_TAG)
                    .commit();
        } else {
            toolbarListener.onChangeTitle(getString(R.string.title_confirm_shipment));
            CourierSelectionModel model = new CourierSelectionModel();
            model.setCourierName(courierViewModel.getCourierName());
            model.setCourierId(courierViewModel.getCourierId());
            model.setServiceId(courierViewModel.getCourierServiceList().get(0).getServiceId());
            model.setServiceName(courierViewModel.getCourierServiceList().get(0).getServiceName());
            listener.onCourierAdapterSelected(model);
        }
    }

    public interface OrderCourierFragmentListener {

        void onCourierAdapterSelected(CourierSelectionModel model);

    }
}
