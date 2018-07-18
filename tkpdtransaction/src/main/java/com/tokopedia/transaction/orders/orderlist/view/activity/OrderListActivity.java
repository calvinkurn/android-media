package com.tokopedia.transaction.orders.orderlist.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.applink.TransactionAppLink;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.OrderLabelList;
import com.tokopedia.transaction.orders.orderlist.di.DaggerOrderListComponent;
import com.tokopedia.transaction.orders.orderlist.di.OrderListComponent;
import com.tokopedia.transaction.orders.orderlist.view.adapter.OrderTabAdapter;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListInitContract;
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListInitPresenterImpl;

import java.util.List;

import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION;

public class OrderListActivity extends DrawerPresenterActivity<OrderListInitContract.Presenter>
        implements HasComponent<OrderListComponent>, OrderListInitContract.View, OrderTabAdapter.Listener{
    private static final String ORDER_CATEGORY = "orderCategory";
    private int drawerPosition;
    private String orderCategory = "ALL";
    private ProgressBar progressBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout mainLayout;
    private OrderTabAdapter adapter;
    private OrderListComponent orderListComponent;

    @DeepLink({TransactionAppLink.ORDER_LIST_DEALS, TransactionAppLink.ORDER_LIST_DIGITAL,
            TransactionAppLink.ORDER_LIST_EVENTS})
    public static Intent getOrderListIntent(Context context, Bundle bundle){

        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        String link = bundle.getString(DeepLink.URI);
        String category = link.substring(link.indexOf("//")+2, link.lastIndexOf("/")).toUpperCase();
        bundle.putString(ORDER_CATEGORY, category);
        return new Intent(context, OrderListActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @DeepLink(TransactionAppLink.ORDER_LIST_FLIGHTS)
    public static Intent getFlightOrderListIntent(Context context, Bundle bundle){

        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        bundle.putString(ORDER_CATEGORY, OrderCategory.FLIGHTS);
        return new Intent(context, OrderListActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    public static Intent getInstance(Context context){
        return new Intent(context, OrderListActivity.class);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        drawerPosition = extras.getInt(EXTRA_STATE_TAB_POSITION,
                TransactionPurchaseRouter.TAB_POSITION_PURCHASE_SUMMARY);
    }

    @Override
    protected void initialPresenter() {
        presenter = new OrderListInitPresenterImpl(this,new GraphqlUseCase());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_list_module;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(drawerHelper != null) {
            switch (orderCategory){
                case OrderCategory.DIGITAL:
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_DIGITAL_TRANSACTION_LIST);
                    break;
                case OrderCategory.FLIGHTS:
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_FLIGHT_TRANSACTION_LIST);
                    break;
                    case OrderCategory.DEALS:
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_DEALS_TRANSACTION_LIST);
                    break;
                case OrderCategory.EVENTS:
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_EVENTS_TRANSACTION_LIST);
                    break;
            }
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        progressBar = findViewById(R.id.progress);
        tabLayout = findViewById(R.id.indicator);
        viewPager = findViewById(R.id.pager);
        mainLayout = findViewById(R.id.main_content);
    }

    @Override
    protected void setActionVar() {
        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    @Override
    protected int setDrawerPosition() {
        return drawerPosition;
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
        GraphqlClient.init(this);
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            orderCategory = bundle.getString(ORDER_CATEGORY);
        }
        initTabs();
    }

    private void initTabs() {
        removeProgressBarView();
        int position = 0;
        for(int i = 0; i < OrderCategory.TABS_CATEGORY.length; i++) {
            if(orderCategory.equals(OrderCategory.TABS_CATEGORY[i])){
                position = i;
            }
            tabLayout.addTab(tabLayout.newTab().setText(OrderCategory.TABS_LABEL[i]));
        }
        adapter = new OrderTabAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        viewPager.setCurrentItem(position);
    }

    @Override
    public Context getAppContext() {
        return this.getApplicationContext();
    }

    @Override
    public void removeProgressBarView() {

        progressBar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorNetwork(String message) {

    }

    @Override
    public void renderTabs(List<OrderLabelList> orderLabelList) {
        for(OrderLabelList tabContent: orderLabelList)
            tabLayout.addTab(tabLayout.newTab().setText(tabContent.getLabel()));
        adapter = new OrderTabAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
    }

    private class OnTabPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        OnTabPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            KeyboardHandler.hideSoftKeyboard(getActivity());
            drawerPosition = position;
            switch (orderCategory){
                case OrderCategory.DIGITAL:
                    drawerPosition = TkpdState.DrawerPosition.PEOPLE_DIGITAL_TRANSACTION_LIST;
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_DIGITAL_TRANSACTION_LIST);
                    break;
                case OrderCategory.FLIGHTS:
                    drawerPosition = TkpdState.DrawerPosition.PEOPLE_FLIGHT_TRANSACTION_LIST;
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_FLIGHT_TRANSACTION_LIST);
                    break;
                case OrderCategory.EVENTS:
                    drawerPosition = TkpdState.DrawerPosition.PEOPLE_EVENTS_TRANSACTION_LIST;
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_EVENTS_TRANSACTION_LIST);
                    break;
                case OrderCategory.DEALS:
                    drawerPosition = TkpdState.DrawerPosition.PEOPLE_DEALS_TRANSACTION_LIST;
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_DEALS_TRANSACTION_LIST);
                    break;

            }
        }
    }

}
