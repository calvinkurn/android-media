package com.tokopedia.seller.selling.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.container.GTMContainer;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.opportunity.fragment.OpportunityListFragment;
import com.tokopedia.seller.selling.SellingService;
import com.tokopedia.seller.selling.constant.shopshippingdetail.ShopShippingDetailView;
import com.tokopedia.seller.selling.presenter.ShippingView;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingNewOrder;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingShipping;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingStatus;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingTransaction;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingTxCenter;

import java.util.ArrayList;
import java.util.List;

public class ActivitySellingTransaction extends TkpdActivity
        implements FragmentSellingTxCenter.OnCenterMenuClickListener,
        DownloadResultReceiver.Receiver {

    public static final String FROM_WIDGET_TAG = "from widget";

    public static final String EXTRA_STATE_TAB_POSITION = "tab";

    public static final int TAB_POSITION_SELLING_OPPORTUNITY = 1;
    public final static int TAB_POSITION_SELLING_NEW_ORDER = 2;
    public final static int TAB_POSITION_SELLING_CONFIRM_SHIPPING = 3;
    public final static int TAB_POSITION_SELLING_SHIPPING_STATUS = 4;
    public final static int TAB_POSITION_SELLING_TRANSACTION_LIST = 5;

    ViewPager mViewPager;
    private TabLayout indicator;
    private TextView sellerTickerView;

    private String[] CONTENT;
    private List<Fragment> fragmentList;
    DownloadResultReceiver mReceiver;

    FragmentManager fragmentManager;

    @DeepLink(Constants.Applinks.SELLER_NEW_ORDER)
    public static Intent getCallingIntentSellerNewOrder(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ActivitySellingTransaction.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_NEW_ORDER)
                .putExtras(extras);
    }

    @DeepLink(Constants.Applinks.SELLER_SHIPMENT)
    public static Intent getCallingIntentSellerShipment(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ActivitySellingTransaction.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_CONFIRM_SHIPPING)
                .putExtras(extras);
    }

    @DeepLink(Constants.Applinks.SELLER_STATUS)
    public static Intent getCallingIntentSellerStatus(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ActivitySellingTransaction.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_SHIPPING_STATUS)
                .putExtras(extras);
    }

    @DeepLink(Constants.Applinks.SELLER_HISTORY)
    public static Intent getCallingIntentSellerHistory(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ActivitySellingTransaction.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_TRANSACTION_LIST)
                .putExtras(extras);
    }

    @DeepLink(Constants.Applinks.SellerApp.SALES)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            Intent intent = new Intent(context, ActivitySellingTransaction.class);
            return intent
                    .setData(uri.build())
                    .putExtras(extras);
        } else {
            return ApplinkUtils.getSellerAppApplinkIntent(context, extras);
        }
    }

    public static Intent createIntent(Context context, int tab) {
        return new Intent(context, ActivitySellingTransaction.class)
                .putExtra(EXTRA_STATE_TAB_POSITION, tab);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_TX_SHOP_TRANSACTION_SELLING_LIST;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLogin();
    }

    private void checkLogin() {
        if (getApplication() instanceof TkpdCoreRouter) {
            if (!SessionHandler.isV4Login(this)) {
                startActivity(((TkpdCoreRouter) getApplication()).getLoginIntent(this));
                AppWidgetUtil.sendBroadcastToAppWidget(this);
                finish();
            } else if (!SessionHandler.isUserHasShop(this)) {
                startActivity(((TkpdCoreRouter) getApplication()).getHomeIntent(this));
                AppWidgetUtil.sendBroadcastToAppWidget(this);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearNotif();
        inflateView(R.layout.activity_shop_transaction_v2);
        setView();
        initVariable();
        setAdapter();
        openTab();

        setTrackerWidget();

        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        fragmentManager = getFragmentManager();
    }

    private void setTrackerWidget() {
        boolean fromWidget = getIntent().getBooleanExtra(FROM_WIDGET_TAG, false);
        if (fromWidget) {
            UnifyTracking.eventAccessAppViewWidget();
        }

        TrackingUtils.sendMoEngageOpenSellerScreen();
    }

    @Override
    public int getDrawerPosition() {
        return 0;
    }

    private void setView() {
        sellerTickerView = (TextView) findViewById(R.id.seller_ticker);
        sellerTickerView.setMovementMethod(new ScrollingMovementMethod());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabLayout) findViewById(R.id.indicator);
    }

    private void initSellerTicker() {
        GTMContainer gtmContainer = GTMContainer.newInstance(this);

        if (gtmContainer.getString("is_show_ticker_sales").equalsIgnoreCase("true")) {
            String message = gtmContainer.getString("ticker_text_sales_rich");
            showTickerGTM(message);
        } else {
            showTickerGTM(null);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initSellerTicker();
        setDrawerPosition(mViewPager.getCurrentItem());
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                if (span.getURL().equals("com.tokopedia.sellerapp")) {
                    startNewActivity(span.getURL());
                } else {
                    openLink(span.getURL());
                }
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    private void openLink(String url) {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void startNewActivity(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            startActivity(intent);
        }
    }

    private void showTickerGTM(String message) {
        if (message != null) {
            CharSequence sequence = MethodChecker.fromHtml(message);
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }
            sellerTickerView.setText(strBuilder);
            sellerTickerView.setMovementMethod(LinkMovementMethod.getInstance());
            sellerTickerView.setVisibility(View.VISIBLE);
            hideTickerOpportunity(getIntent().getExtras().getInt(EXTRA_STATE_TAB_POSITION));
        } else {
            sellerTickerView.setVisibility(View.GONE);
        }

    }


    private void initVariable() {
        CONTENT = new String[]{getString(R.string.title_dashboard_sell),
                getString(R.string.title_opportunity_list),
                getString(R.string.title_tab_new_order),
                getString(R.string.title_shipping_confirmation),
                getString(R.string.title_shipping_status),
                getString(R.string.title_transaction_list)
        };
        for (String aCONTENT : CONTENT) indicator.addTab(indicator.newTab().setText(aCONTENT));
        fragmentList = new ArrayList<>();
//        fragmentList.add(FragmentPeopleTxCenter.createInstance(FragmentPeopleTxCenter.SHOP));
//        fragmentList.add(FragmentShopNewOrderV2.createInstance()); //TODO UNCOMMENT
        fragmentList.add(FragmentSellingTxCenter.createInstance(FragmentSellingTxCenter.SHOP));
        fragmentList.add(OpportunityListFragment.newInstance());
        fragmentList.add(FragmentSellingNewOrder.createInstance());
        fragmentList.add(FragmentSellingShipping.createInstance());
        fragmentList.add(FragmentSellingStatus.newInstance());
        fragmentList.add(FragmentSellingTransaction.newInstance());
        mViewPager.setOffscreenPageLimit(fragmentList.size());

//        fragmentList.add(FragmentShopTxStatusV2.createInstanceStatus(R.layout.fragment_shipping_status, FragmentShopTxStatusV2.INSTANCE_STATUS));
//        fragmentList.add(FragmentShopTxStatusV2.createInstanceTransaction(R.layout.fragment_shop_transaction_list, FragmentShopTxStatusV2.INSTANCE_TX));
    }

    private void setAdapter() {
        mViewPager.setAdapter(new SectionsPagerAdapter(getFragmentManager()));
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
                initSellerTicker();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(mViewPager));
    }

    public void hideTickerOpportunity(int position) {
        if (position == 5) {
            sellerTickerView.setVisibility(View.GONE);
        }
    }

    private void openTab() {
        try {
            mViewPager.setCurrentItem(getIntent().getExtras().getInt(EXTRA_STATE_TAB_POSITION));
            setDrawerPosition(getIntent().getExtras().getInt(EXTRA_STATE_TAB_POSITION));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDrawerPosition(int position) {
        switch (position) {
            case TAB_POSITION_SELLING_NEW_ORDER:
                drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.SHOP_NEW_ORDER);
                break;
            case TAB_POSITION_SELLING_CONFIRM_SHIPPING:
                drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING);
                break;
            case TAB_POSITION_SELLING_SHIPPING_STATUS:
                drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS);
                break;
            case TAB_POSITION_SELLING_TRANSACTION_LIST:
                drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST);
                break;
            case TAB_POSITION_SELLING_OPPORTUNITY:
                drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.SHOP_OPPORTUNITY_LIST);
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
        if (!GlobalConfig.isSellerApp()) {
            getMenuInflater().inflate(R.menu.cart_and_search, menu);
            LocalCacheHandler Cache = new LocalCacheHandler(getBaseContext(), DrawerHelper.DRAWER_CACHE);
            int CartCache = Cache.getInt(DrawerNotification.IS_HAS_CART);
            if (CartCache > 0) {
                menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_new_action_cart_active);
            } else {
                menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_new_action_cart);
            }
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

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

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent homeIntent = null;
            if (GlobalConfig.isSellerApp()) {
                homeIntent = SellerAppRouter.getSellerHomeActivity(this);
            } else {
                homeIntent = HomeRouter.getHomeActivity(this);
            }
            startActivity(homeIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }
}
