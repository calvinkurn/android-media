package com.tokopedia.seller.selling.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.container.GTMContainer;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.seller.selling.SellingService;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingNewOrder;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingShipping;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingStatus;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingTransaction;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingTxCenter;
import com.tokopedia.seller.selling.presenter.ShippingView;
import com.tokopedia.seller.selling.constant.shopshippingdetail.ShopShippingDetailView;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

public class ActivitySellingTransaction extends TkpdActivity implements FragmentSellingTxCenter.OnCenterMenuClickListener, DownloadResultReceiver.Receiver {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private TabLayout indicator;
    private TextView sellerTickerView;

    private String[] CONTENT;
    private List<Fragment> fragmentList;
    DownloadResultReceiver mReceiver;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearNotif();
        inflateView(R.layout.activity_shop_transaction_v2);
        initView();
        initVariable();
        setAdapter();
        openTab();

        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        fragmentManager = getFragmentManager();
    }

    private void initView() {
        sellerTickerView = (TextView) findViewById(R.id.seller_ticker);
        initSellerTicker();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(4);
        indicator = (TabLayout) findViewById(R.id.indicator);
    }

    private void initSellerTicker() {
        GTMContainer gtmContainer = GTMContainer.newInstance(this);

        if (gtmContainer.getString("is_show_ticker_sales").equalsIgnoreCase("true")) {
            String message = gtmContainer.getString("ticker_text_sales");
            showTickerGTM(message);
        } else {
            showTickerGTM(null);
        }

    }

    private void showTickerGTM(String message) {
        if (message != null) {
            sellerTickerView.setText(Html.fromHtml(message));
            sellerTickerView.setVisibility(View.VISIBLE);
            sellerTickerView.setAutoLinkMask(0);
            Linkify.addLinks(sellerTickerView, Linkify.WEB_URLS);
        } else {
            sellerTickerView.setVisibility(View.GONE);
        }
    }


    private void initVariable() {
        CONTENT = new String[]{getString(R.string.title_dashboard_sell),
                getString(R.string.title_tab_new_order),
                getString(R.string.title_shipping_confirmation),
                getString(R.string.title_shipping_status),
                getString(R.string.title_transaction_list)};
        for (String aCONTENT : CONTENT) indicator.addTab(indicator.newTab().setText(aCONTENT));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        fragmentList = new ArrayList<>();
//        fragmentList.add(FragmentPeopleTxCenter.createInstance(FragmentPeopleTxCenter.SHOP));
//        fragmentList.add(FragmentShopNewOrderV2.createInstance()); //TODO UNCOMMENT
        fragmentList.add(FragmentSellingTxCenter.createInstance(FragmentSellingTxCenter.SHOP));
        fragmentList.add(FragmentSellingNewOrder.createInstance());
        fragmentList.add(FragmentSellingShipping.createInstance());
        fragmentList.add(FragmentSellingStatus.newInstance());
        fragmentList.add(FragmentSellingTransaction.newInstance());
//        fragmentList.add(FragmentShopTxStatusV2.createInstanceStatus(R.layout.fragment_shipping_status, FragmentShopTxStatusV2.INSTANCE_STATUS));
//        fragmentList.add(FragmentShopTxStatusV2.createInstanceTransaction(R.layout.fragment_shop_transaction_list, FragmentShopTxStatusV2.INSTANCE_TX));
    }

    private void setAdapter() {
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setDrawerPosition(position);
                if (indicator.getTabAt(position) != null) {
                    UnifyTracking.eventShopTabSelected(indicator.getTabAt(position).getText().toString());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(mViewPager));
    }

    private void openTab() {
        try {
            mViewPager.setCurrentItem(getIntent().getExtras().getInt("tab"));
            setDrawerPosition(getIntent().getExtras().getInt("tab"));
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void setDrawerPosition(int position) {
        switch (position) {
            case 1:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.SHOP_NEW_ORDER);
                break;
            case 2:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING);
                break;
            case 3:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS);
                break;
            case 4:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST);
                break;
            default:
                break;
        }
    }

    /**
     * clear push notif when activity open from notification
     */
    private void clearNotif() {
        if (getIntent().getBooleanExtra("from_notif", false)) {
            new NotificationModHandler(this).cancelNotif();
        }
    }

    @Override
    public void OnMenuClick(int position) {
        mViewPager.setCurrentItem(position);
    }

    /**
     * receive result when hit to ws
     *
     * @param resultCode code of result (running, finish, error)
     * @param resultData bundle of data which send by sender (service)
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(SellingService.TYPE, SellingService.INVALID_TYPE);
        int position = resultData.getInt(ShopShippingDetailView.POSITION);
        Fragment fragment = null;
        switch (type) {
            case SellingService.CONFIRM_SHIPPING:
            case SellingService.CANCEL_SHIPPING:
            case SellingService.CONFIRM_NEW_ORDER:
            case SellingService.REJECT_NEW_ORDER:
            case SellingService.PARTIAL_NEW_ORDER:
//                fragment = getFragment(position);
//                break;
            case SellingService.CONFIRM_MULTI_SHIPPING:
                fragment = getFragmentMainPager(mViewPager.getCurrentItem());
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }

        //check if Fragment implement necessary interface
        if (fragment != null && type != SellingService.INVALID_TYPE) {
            switch (resultCode) {
                case SellingService.STATUS_RUNNING:
                    switch (type) {
                        case SellingService.CONFIRM_MULTI_SHIPPING:
                        case SellingService.CONFIRM_SHIPPING:
                        case SellingService.CANCEL_SHIPPING:
                        case SellingService.CONFIRM_NEW_ORDER:
                        case SellingService.REJECT_NEW_ORDER:
                        case SellingService.PARTIAL_NEW_ORDER:
                            //[START] show progress bar
                            if (fragment instanceof ShippingView) {
                                ((ShippingView) fragment).showProgress();
                            }
                            break;
                    }
                    break;
                case SellingService.STATUS_FINISHED:
                    switch (type) {
                        case SellingService.CONFIRM_MULTI_SHIPPING:
                        case SellingService.CONFIRM_SHIPPING:
                        case SellingService.CANCEL_SHIPPING:
                        case SellingService.CONFIRM_NEW_ORDER:
                        case SellingService.REJECT_NEW_ORDER:
                        case SellingService.PARTIAL_NEW_ORDER:
                            ((BaseView) fragment).setData(type, resultData);
                            break;
                    }
                    break;
                case SellingService.STATUS_ERROR:
                    switch (resultData.getInt(SellingService.NETWORK_ERROR_FLAG, SellingService.INVALID_NETWORK_ERROR_FLAG)) {
                        case NetworkConfig.BAD_REQUEST_NETWORK_ERROR:
                            ((BaseView) fragment).onNetworkError(type, " BAD_REQUEST_NETWORK_ERROR !!!");
                            break;
                        case NetworkConfig.INTERNAL_SERVER_ERROR:
                            ((BaseView) fragment).onNetworkError(type, " INTERNAL_SERVER_ERROR !!!");
                            break;
                        case NetworkConfig.FORBIDDEN_NETWORK_ERROR:
                            ((BaseView) fragment).onNetworkError(type, " FORBIDDEN_NETWORK_ERROR !!!");
                            break;
                        case SellingService.INVALID_NETWORK_ERROR_FLAG:
                        default:
                            String messageError = resultData.getString(SellingService.MESSAGE_ERROR_FLAG, SellingService.INVALID_MESSAGE_ERROR);
                            if (!messageError.equals(SellingService.INVALID_MESSAGE_ERROR)) {
                                ((BaseView) fragment).onMessageError(type, messageError);
                            }
                    }
                    break;
            }// end of status download service
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_and_search, menu);
        LocalCacheHandler Cache = new LocalCacheHandler(getBaseContext(), "NOTIFICATION_DATA");
        int CartCache = Cache.getInt("is_has_cart");
        if (CartCache > 0) {
            menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_new_action_cart_active);
        } else {
            menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_new_action_cart);
        }
        return true;
    }

    public Fragment getFragmentMainPager(int position) {
        SectionsPagerAdapter fragmentStatePagerAdapter = (SectionsPagerAdapter) mViewPager.getAdapter();
        Fragment fragment = (Fragment) fragmentStatePagerAdapter.instantiateItem(mViewPager, position);
        return fragment;
    }

//    public void notifyChangeListNewOrder() {
//        Fragment fragment = getFragmentMainPager(1); // get fragment new order
//        if (fragment != null) {
//            if (fragment instanceof FragmentShopNewOrderV2) {
//                ((FragmentShopNewOrderV2) fragment).getOrderListRefreshAll();
//            }
//        }
//    }
//
//    public void notifyChangeShipping() {
//        Fragment fragment = getFragmentMainPager(2); // get fragment shipping
//        if (fragment != null) {
//            if (fragment instanceof FragmentShopShippingV2) {
//                ((FragmentShopShippingV2) fragment).getShippingListRefreshAll();
//            }
//        }
//    }

    public void SellingAction(int type, Bundle data) {
        switch (type) {
            case SellingService.CONFIRM_MULTI_SHIPPING:
            case SellingService.CONFIRM_SHIPPING:
            case SellingService.CANCEL_SHIPPING:
            case SellingService.CONFIRM_NEW_ORDER:
            case SellingService.REJECT_NEW_ORDER:
            case SellingService.PARTIAL_NEW_ORDER:
                SellingService.startSellingService(this, mReceiver, data, type);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
