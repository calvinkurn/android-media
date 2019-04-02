package com.tokopedia.transaction.orders.orderlist.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.OrderLabelList;
import com.tokopedia.transaction.orders.orderlist.di.DaggerOrderListComponent;
import com.tokopedia.transaction.orders.orderlist.di.OrderListComponent;
import com.tokopedia.transaction.orders.orderlist.view.adapter.OrderTabAdapter;
import com.tokopedia.transaction.orders.orderlist.view.listener.GlobalMainTabSelectedListener;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListInitContract;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListInitPresenterImpl;
import com.tokopedia.user.session.UserSession;

import java.util.List;

public class OrderListActivity extends BaseSimpleActivity
        implements HasComponent<OrderListComponent>, OrderListInitContract.View, OrderTabAdapter.Listener {
    private static final String ORDER_CATEGORY = OrderCategory.KEY_LABEL;
    private static final int REQUEST_CODE = 100;
    private String orderCategory = "ALL";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OrderTabAdapter adapter;
    private OrderListComponent orderListComponent;
    private OrderListInitContract.Presenter presenter;

    @DeepLink({ApplinkConst.DEALS_ORDER, ApplinkConst.DIGITAL_ORDER,
            ApplinkConst.EVENTS_ORDER})
    public static Intent getOrderListIntent(Context context, Bundle bundle) {

        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        String link = bundle.getString(DeepLink.URI);
        String category = link.substring(link.indexOf("//") + 2, link.lastIndexOf("/")).toUpperCase();
        bundle.putString(ORDER_CATEGORY, category);
        return new Intent(context, OrderListActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_order_list_module;
    }

    @DeepLink(ApplinkConst.FLIGHT_ORDER)
    public static Intent getFlightOrderListIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        bundle.putString(ORDER_CATEGORY, OrderCategory.FLIGHTS);
        return new Intent(context, OrderListActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }
    @DeepLink({ApplinkConst.MARKETPLACE_ORDER,ApplinkConst.MARKETPLACE_ORDER_FILTER})
    public static Intent getMarketPlaceOrderListIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        bundle.putString(ORDER_CATEGORY, OrderCategory.MARKETPLACE);
        Intent intent = new Intent(context,OrderListActivity.class);
        if(uri!=null) {
            intent.setData(uri.build());
        }
        return intent.putExtras(bundle);
    }
    public static Intent getInstance(Context context) {
        return new Intent(context, OrderListActivity.class);
    }

    protected void initVar() {
        tabLayout = findViewById(R.id.indicator);
        viewPager = findViewById(R.id.pager);
        presenter = new OrderListInitPresenterImpl(this, new GraphqlUseCase());
    }

    @Override
    public OrderListComponent getComponent() {
        if (orderListComponent == null) initInjector();
        return orderListComponent;
    }

    private void initInjector() {
        orderListComponent = DaggerOrderListComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderCategory = bundle.getString(ORDER_CATEGORY);
        }
        UserSession userSession = new UserSession(this);
        if (!userSession.isLoggedIn()) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE);
        } else {
            presenter.getInitData();
        }

    }

    @Override
    public Context getAppContext() {
        return this.getApplicationContext();
    }

    @Override
    public Bundle getBundle() {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return new Bundle();
        } else  {
            return bundle;
        }
    }

    @Override
    public void removeProgressBarView() {
    }

    @Override
    public void showErrorNetwork(String message) {
    }

    @Override
    public void renderTabs(List<OrderLabelList> orderLabelList) {
        int position = 0;
        for (int i = 0; i < orderLabelList.size(); i++) {
            if (orderCategory.equals(orderLabelList.get(i).getOrderCategory())) {
                position = i;
            }
            tabLayout.addTab(tabLayout.newTab().setText(orderLabelList.get(i).getLabelBhasa()));
        }
        adapter = new OrderTabAdapter(getSupportFragmentManager(), this, orderLabelList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        viewPager.setCurrentItem(position);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    private class OnTabPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        OnTabPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            KeyboardHandler.hideSoftKeyboard(OrderListActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE ) {
            if(resultCode == RESULT_OK) {
                presenter.getInitData();
            } else {
                finish();
            }
        }

    }

    @Override
    protected void onDestroy() {
        presenter.destroyView();
        super.onDestroy();
    }
}
