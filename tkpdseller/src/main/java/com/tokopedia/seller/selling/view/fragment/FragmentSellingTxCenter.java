package com.tokopedia.core.selling.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.customadapter.ListViewPeopleTransactionSummary;
import com.tokopedia.core.selling.presenter.PeopleTxCenter;
import com.tokopedia.core.selling.presenter.PeopleTxCenterImpl;
import com.tokopedia.core.selling.presenter.PeopleTxCenterView;
import com.tokopedia.core.session.baseFragment.BaseFragment;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.var.NotificationVariable;
import com.tokopedia.core.var.TkpdCache;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Toped10 on 7/28/2016.
 */
public class FragmentSellingTxCenter extends BaseFragment<PeopleTxCenter> implements PeopleTxCenterView {
    @Bind(R2.id.menu_list)
    ListView TitleMenuListView;

    private RefreshHandler Refresh;
    private LocalCacheHandler cache;
    private ListViewPeopleTransactionSummary ListViewPeopleTransactionSummaryAdapter;
    private OnCenterMenuClickListener listener;
    private NotificationVariable notif;

    private String state = "";
    ArrayList<String> MenuName = new ArrayList<>();
    ArrayList<String> MenuDesc = new ArrayList<>();
    ArrayList<Integer> MenuCount = new ArrayList<>();

    public static FragmentSellingTxCenter createInstance(String type) {
        FragmentSellingTxCenter fragment = new FragmentSellingTxCenter();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
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
        cache = new LocalCacheHandler(getActivity(), TkpdCache.NOTIFICATION_DATA);
    }

    @Override
    public void initView() {
        Refresh = new RefreshHandler(getActivity(), getView(), refreshListener());
        Refresh.setPullEnabled(false);
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
        notif = MainApplication.getNotifInstance();
        notif.setContext(activity);
        notif.getNotifV4();
        notif.SetOnNotifRefresh(new NotificationVariable.OnNotifRefreshListener() {

            @Override
            public void OnNotifRefresh() {
                MenuCount.clear();
                ListViewPeopleTransactionSummaryAdapter.notifyDataSetChanged();
                loadData();
            }

            @Override
            public void OnNotifRefreshStart() {

            }
        });
    }

    @Override
    public boolean getVisibleUserHint() {
        return getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        initPresenter();
        presenter.fetchArguments(getArguments());
        presenter.checkValidationToSendGoogleAnalytic(isVisibleToUser, getActivity());
        presenter.setLocalyticFlow(getActivity());
        loadData();
        super.setUserVisibleHint(isVisibleToUser);
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
                if (!getArguments().getString("type").equals("people")) {
                    if (position == 0) {
                        listener.OnMenuClick(position + 1);
                    } else if (position == 2) {
                        listener.OnMenuClick(3);
                    } else {
                        listener.OnMenuClick(2);
                    }
                } else {
                    switch (position) {
                        case 0:
                            listener.OnMenuClick(PEOPLE_CONFIRM);
                            break;
                        case 1:
                            listener.OnMenuClick(PEOPLE_VERIFICATION);
                            break;
                        case 2:
                            listener.OnMenuClick(PEOPLE_STATUS);
                            break;
                        case 3:
                            listener.OnMenuClick(PEOPLE_CANCEL);
                            break;
                        case 4:
                            listener.OnMenuClick(PEOPLE_ACCEPT);
                            break;
                    }
                }
            }
        };
    }

    private RefreshHandler.OnRefreshHandlerListener refreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                loadData();
            }
        };
    }

    @Override
    public void loadData() {
        try {
            MenuCount.clear();
            if (getArguments().getString("type").equals("people")) {
                MenuName.clear();
                MenuCount.addAll(cache.getArrayListInteger(TkpdCache.Key.PURCHASE_COUNT));
                MenuName.addAll(cache.getArrayListString(TkpdCache.Key.PURCHASE));
            } else {
                MenuCount.addAll(cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT));
            }
            while (MenuCount.size() < 3) {
                MenuCount.add(0);
            }
            Refresh.finishRefresh();
            ListViewPeopleTransactionSummaryAdapter.notifyDataSetChanged();
            Refresh.setPullEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
