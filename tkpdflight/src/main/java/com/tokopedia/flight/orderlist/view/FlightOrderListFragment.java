package com.tokopedia.flight.orderlist.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.dashboard.view.activity.FlightDashboardActivity;
import com.tokopedia.flight.detail.view.activity.FlightDetailOrderActivity;
import com.tokopedia.flight.orderlist.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.presenter.FlightOrderListPresenter;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapter;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapterTypeFactory;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderTypeFactory;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderBaseViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;

import java.util.List;

import javax.inject.Inject;


/**
 * @author by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListFragment extends BaseListFragment<Visitable, FlightOrderTypeFactory>
        implements FlightOrderListContract.View,
        QuickSingleFilterView.ActionListener,
        FlightOrderAdapter.OnAdapterInteractionListener {
    public static final int PER_PAGE = 10;
    @Inject
    FlightOrderListPresenter presenter;
    private QuickSingleFilterView quickSingleFilterView;

    private String selectedFilter;

    public static FlightOrderListFragment createInstance() {
        return new FlightOrderListFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightOrderComponent.class)
                .inject(this);
    }

    @NonNull
    @Override
    protected BaseListAdapter<Visitable, FlightOrderTypeFactory> createAdapterInstance() {
        return new BaseListAdapter<>(getAdapterTypeFactory());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_order_list, container, false);
        quickSingleFilterView = view.findViewById(R.id.quick_filter);
        quickSingleFilterView.setListener(this);
        return view;
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_orders);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

    @Override
    public void loadData(int page) {
        presenter.attachView(this);
        presenter.loadData(selectedFilter, page, PER_PAGE);
    }

    @Override
    protected FlightOrderTypeFactory getAdapterTypeFactory() {
        return new FlightOrderAdapterTypeFactory(this);
    }

    @Override
    public void renderOrderStatus(List<QuickFilterItem> filterItems) {
        quickSingleFilterView.setDefaultItem(filterItems.get(0));
        quickSingleFilterView.renderFilter(filterItems);
    }

    @Override
    public String getSelectedFilter() {
        return String.valueOf(selectedFilter);
    }

/*    @Override
    public void navigateToOpenBrowser(String urlPdf) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlPdf));
        startActivity(browserIntent);
    }*/

    @Override
    public void showInputEmailForm(String invoiceId, String userId) {

    }

    @Override
    public void selectFilter(String typeFilter) {
        selectedFilter = typeFilter;
        showSwipeLoading();
        loadInitialData();
    }

    @Override
    public void onDetailOrderClicked(FlightOrderDetailPassData viewModel) {
        startActivity(FlightDetailOrderActivity.createIntent(getActivity(), viewModel));
    }

    @Override
    public void onDetailOrderClicked(String orderId) {
        FlightOrderDetailPassData passData = new FlightOrderDetailPassData();
        passData.setOrderId(orderId);
        startActivity(FlightDetailOrderActivity.createIntent(getActivity(), passData));
    }

    @Override
    public void onHelpOptionClicked(String invoiceId, int status) {
        StringBuilder result = new StringBuilder(FlightUrl.CONTACT_US_FLIGHT_PREFIX_GLOBAL);
        result.append("&iv=" + invoiceId);
        result.append("&ostat=" + status);
        String url = result.toString();
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication())
                .getDefaultContactUsIntent(getActivity(), url) != null) {
            startActivity(((FlightModuleRouter) getActivity().getApplication())
                    .getDefaultContactUsIntent(getActivity(), url));
        }
    }

    @Override
    public void onItemClicked(Visitable visitable) {

    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onReBookingClicked(FlightOrderBaseViewModel item) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getActivity());
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication())
                .getHomeIntent(getActivity()) != null) {
            Intent intent = ((FlightModuleRouter) getActivity().getApplication())
                    .getHomeIntent(getActivity());
            taskStackBuilder.addNextIntent(intent);
        }
        taskStackBuilder.addNextIntent(FlightDashboardActivity.getCallingIntent(getActivity()));
        taskStackBuilder.startActivities();
    }

    @Override
    public void onDownloadETicket(String invoiceId) {
        presenter.onDownloadEticket(invoiceId);
    }

    @Override
    protected String getMessageFromThrowable(Context context, Throwable t) {
        return FlightErrorUtil.getMessageFromException(context, t);
    }
}
