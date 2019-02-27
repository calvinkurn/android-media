package com.tokopedia.seller.selling.view.activity;

import android.annotation.SuppressLint;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
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
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.abstraction.common.utils.toolargetool.TooLargeTool;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.container.GTMContainer;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.fragment.OpportunityListFragment;
import com.tokopedia.seller.selling.SellingService;
import com.tokopedia.seller.selling.constant.shopshippingdetail.ShopShippingDetailView;
import com.tokopedia.seller.selling.presenter.ShippingView;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingDelivered;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingNewOrder;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingShipped;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingShipping;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingTransaction;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingTxCenter;
import com.tokopedia.seller.selling.view.listener.SellingTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 8/1/18.
 */
public class CustomerAppSellerTransactionActivity extends BaseTabActivity
        implements FragmentSellingTxCenter.OnCenterMenuClickListener,
        DownloadResultReceiver.Receiver,
        SellingTransaction {
    public static final String FROM_WIDGET_TAG = "from widget";

    public static final String EXTRA_STATE_TAB_POSITION = "tab";
    public static final String EXTRA_QUERY = "query";

    public static final int TAB_POSITION_SELLING_OPPORTUNITY = 0;
    public final static int TAB_POSITION_SELLING_NEW_ORDER = 1;
    public final static int TAB_POSITION_READY_TO_SHIP = 2;
    public final static int TAB_POSITION_SHIPPED = 3;
    public final static int TAB_POSITION_DELIVERED = 4;
    public final static int TAB_POSITION_SELLING_TRANSACTION_LIST = 5;

    ViewPager mViewPager;
    DownloadResultReceiver mReceiver;
    FragmentManager fragmentManager;
    private TabLayout indicator;
    private TextView sellerTickerView;
    private String[] CONTENT;
    private List<Fragment> fragmentList;

    @DeepLink(ApplinkConst.SELLER_TRANSACTION)
    public static Intent getApplinkIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, CustomerAppSellerTransactionActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    public static Intent getIntentOpportunity(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, CustomerAppSellerTransactionActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_OPPORTUNITY)
                .putExtras(extras);
    }

    public static Intent getIntentNewOrder(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, CustomerAppSellerTransactionActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_NEW_ORDER)
                .putExtras(extras);
    }

    @DeepLink(ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP)
    public static Intent getIntentReadyToShip(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, CustomerAppSellerTransactionActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_READY_TO_SHIP)
                .putExtras(extras);
    }

    @DeepLink(ApplinkConst.SELLER_PURCHASE_SHIPPED)
    public static Intent getIntentShipped(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, CustomerAppSellerTransactionActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SHIPPED)
                .putExtras(extras);
    }

    @DeepLink(ApplinkConst.SELLER_PURCHASE_DELIVERED)
    public static Intent getIntentDelivered(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, CustomerAppSellerTransactionActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_DELIVERED)
                .putExtras(extras);
    }

    public static Intent getIntentAllTransaction(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, CustomerAppSellerTransactionActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_SELLING_TRANSACTION_LIST)
                .putExtras(extras);
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
    protected int getLayoutRes() {
        return R.layout.layout_tab_secondary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearNotif();
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
            UnifyTracking.eventAccessAppViewWidget(this);
        }
    }

    private void setView() {
        sellerTickerView = findViewById(com.tokopedia.design.R.id.ticker);
        sellerTickerView.setMovementMethod(new ScrollingMovementMethod());
        sellerTickerView.setVisibility(View.GONE);
        mViewPager = findViewById(com.tokopedia.design.R.id.pager);
        indicator = findViewById(com.tokopedia.design.R.id.indicator);

        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        CONTENT = new String[]{
                getString(com.tokopedia.core2.R.string.title_opportunity_list),
                getString(com.tokopedia.core2.R.string.title_tab_new_order),
                getString(R.string.title_seller_tx_ready_to_ship),
                getString(R.string.title_seller_tx_shipped),
                getString(R.string.title_seller_tx_delivered),
                getString(com.tokopedia.core2.R.string.title_transaction_list)
        };
        for (String aCONTENT : CONTENT) indicator.addTab(indicator.newTab().setText(aCONTENT));
        fragmentList = new ArrayList<>();
        String query = "";
        if (getIntent().hasExtra(EXTRA_QUERY)) {
            query = getIntent().getStringExtra(EXTRA_QUERY);
        }
        fragmentList.add(OpportunityListFragment.newInstance(query));
        fragmentList.add(FragmentSellingNewOrder.createInstance());
        fragmentList.add(FragmentSellingShipping.createInstance());
        fragmentList.add(FragmentSellingShipped.newInstance());
        fragmentList.add(FragmentSellingDelivered.newInstance());
        fragmentList.add(FragmentSellingTransaction.newInstance());
        mViewPager.setOffscreenPageLimit(fragmentList.size());
    }

    private void setAdapter() {
        mViewPager.setAdapter(new CustomerAppSellerTransactionActivity.SectionsPagerAdapter(getFragmentManager()));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (indicator.getTabAt(position) != null) {
                    UnifyTracking.eventShopTabSelected(CustomerAppSellerTransactionActivity.this,
                            indicator.getTabAt(position).getText().toString());
                }
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
        } catch (Exception e) {
            e.printStackTrace();
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
        return super.onCreateOptionsMenu(menu);
    }

    public Fragment getFragmentMainPager(int position) {
        CustomerAppSellerTransactionActivity.SectionsPagerAdapter fragmentStatePagerAdapter = (CustomerAppSellerTransactionActivity.SectionsPagerAdapter) mViewPager.getAdapter();
        Fragment fragment = (Fragment) fragmentStatePagerAdapter.instantiateItem(mViewPager, position);
        return fragment;
    }

    @Override
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

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        return null;
    }

    @Override
    protected int getPageLimit() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            startActivity(HomeRouter.getHomeActivityInterfaceRouter(this));
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Do not put super, avoid crash transactionTooLarge
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
}
