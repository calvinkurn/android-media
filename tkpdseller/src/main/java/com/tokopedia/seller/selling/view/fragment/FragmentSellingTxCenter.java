package com.tokopedia.seller.selling.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.customadapter.ListViewPeopleTransactionSummary;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.selling.presenter.PeopleTxCenter;
import com.tokopedia.seller.selling.presenter.PeopleTxCenterImpl;
import com.tokopedia.seller.selling.presenter.PeopleTxCenterView;
import com.tokopedia.core.session.baseFragment.BaseFragment;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.var.TkpdCache;

import java.util.ArrayList;

/**
 * Created by Toped10 on 7/28/2016.
 */
public class FragmentSellingTxCenter extends BaseFragment<PeopleTxCenter> implements PeopleTxCenterView {
    public static final String TYPE = "type";
    private static final int TAB_OPPORTUNITY = 1;
    public static final int TAB_SELLING_NOTIFICATION = 1;
    ListView TitleMenuListView;

    private RefreshHandler Refresh;
    private LocalCacheHandler cache;
    private ListViewPeopleTransactionSummary ListViewPeopleTransactionSummaryAdapter;
    private OnCenterMenuClickListener listener;

    private String state = "";
    ArrayList<String> MenuName = new ArrayList<>();
    ArrayList<String> MenuDesc = new ArrayList<>();
    ArrayList<Integer> MenuCount = new ArrayList<>();

    public static FragmentSellingTxCenter createInstance(String type) {
        FragmentSellingTxCenter fragment = new FragmentSellingTxCenter();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragmentSellingTxCenter() {
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public void initHandlerAndAdapter() {
        ListViewPeopleTransactionSummaryAdapter = new ListViewPeopleTransactionSummary(getActivity(), MenuName, MenuCount, MenuDesc);
        cache = new LocalCacheHandler(getActivity(), DrawerHelper.DRAWER_CACHE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        TitleMenuListView = (ListView) view.findViewById(R.id.menu_list);
        return view;
    }

    @Override
    public void initView() {
        Refresh = new RefreshHandler(getActivity(), getView(), refreshListener());
        TitleMenuListView.setAdapter(ListViewPeopleTransactionSummaryAdapter);
        TitleMenuListView.setOnItemClickListener(gridListener());
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setCondition1() {
        MenuName.add(getString(R.string.payment_confirm));
        MenuName.add(getString(R.string.payment_verify));
        MenuName.add(getString(R.string.order_status));
        MenuName.add(getString(R.string.title_receive_confirmation_dashboard));
        MenuName.add(getString(R.string.title_transaction_list));

        MenuDesc.add(getString(R.string.payment_confirm_desc));
        MenuDesc.add(getString(R.string.payment_verification_desc));
        MenuDesc.add(getString(R.string.order_status_desc));
        MenuDesc.add(getString(R.string.title_receive_confirmation_dashboard_desc));
        MenuDesc.add(getString(R.string.title_transaction_list_desc));

        state = "people";
    }

    @Override
    public void setCondition2() {
        MenuName.add(getString(R.string.title_new_order));
        MenuName.add(getString(R.string.shipping_confirm));
        MenuName.add(getString(R.string.shipping_status));

        MenuDesc.add(getString(R.string.title_new_order_desc));
        MenuDesc.add(getString(R.string.shipping_confirm_desc));
        MenuDesc.add(getString(R.string.shipping_status_desc));

        state = "shop";
    }

    public interface OnCenterMenuClickListener {
        void OnMenuClick(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnCenterMenuClickListener) activity;
    }

    @Override
    public boolean getVisibleUserHint() {
        return getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(getActivity()!= null && presenter == null)
            initPresenter();

        if(getActivity()!= null) {
            presenter.fetchArguments(getArguments());
            ScreenTracking.screen(AppScreen.SCREEN_TX_SHOP_CENTER);
            super.setUserVisibleHint(isVisibleToUser);
            presenter.setLocalyticFlow(getActivity());
        }
    }

    @Override
    protected void initPresenter() {
        presenter = new PeopleTxCenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_people_tx_center;
    }

    private AdapterView.OnItemClickListener gridListener() {
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                listener.OnMenuClick(position + TAB_OPPORTUNITY + TAB_SELLING_NOTIFICATION);
            }
        };
    }

    private RefreshHandler.OnRefreshHandlerListener refreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.refreshData();
            }
        };
    }

    @Override
    public void loadData() {
        try {
            MenuCount.clear();
            MenuCount.add(cache.getInt(DrawerNotification.CACHE_SELLING_NEW_ORDER, 0));
            MenuCount.add(cache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION, 0));
            MenuCount.add(cache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS, 0));

            Refresh.finishRefresh();
            ListViewPeopleTransactionSummaryAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorRefresh(String errorMessage) {
        Refresh.finishRefresh();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRefresh() {
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
