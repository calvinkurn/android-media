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

public class OrderListActivity extends DrawerPresenterActivity<OrderListInitContract.Presenter> implements HasComponent<OrderListComponent>, OrderListInitContract.View, OrderTabAdapter.Listener{
    private int drawerPosition;
    private String orderCategory = "ALL";
    private ProgressBar progressBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout mainLayout;
    private OrderTabAdapter adapter;

    @DeepLink(TransactionAppLink.ORDER_HISTORY)
    public static Intent getOrderListIntent(Context context, Bundle bundle){
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, OrderListActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
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
            if (orderCategory.equals(OrderCategory.DIGITAL)) {
                drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_DIGITAL_TRANSACTION_LIST);
            } else {
                drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_OMS_TRANSACTION_LIST);
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
    protected void onCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);
        super.onCreate(savedInstanceState);
        presenter.getInitData(orderCategory, 1, 10);

//        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
//        }
////        toolbar.setTitleTextAppearance(this, R.style.ToolbarText_SansSerifMedium);
//        if (orderCategory.equals("DIGITAL")) {
//            toolbar.setTitle("DIGITAL");
//        } else {
//            toolbar.setTitle("Entertainment");
//        }
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
        adapter = new OrderTabAdapter(getSupportFragmentManager(), orderLabelList,this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        viewPager.setCurrentItem(0);
    }

    @Override
    public String getFilterCaseAllTransaction() {
        return null;
    }

    @Override
    public OrderListComponent getComponent() {
        return DaggerOrderListComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
    }

    private class OnTabPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        OnTabPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            hideKeyboard();
            drawerPosition = position;
        }

        private void hideKeyboard() {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
        }
    }

}
