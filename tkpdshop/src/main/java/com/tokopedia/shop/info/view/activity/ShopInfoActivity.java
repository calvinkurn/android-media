package com.tokopedia.shop.info.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTracking;
import com.tokopedia.shop.common.constant.ShopAppLink;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.view.adapter.ShopInfoPagerAdapter;
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment;
import com.tokopedia.shop.info.view.listener.ShopInfoView;
import com.tokopedia.shop.info.view.presenter.ShopInfoPresenter;
import com.tokopedia.shop.note.view.fragment.ShopNoteListFragment;

import javax.inject.Inject;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopInfoActivity extends BaseTabActivity implements ShopInfoView, HasComponent<ShopComponent> {

    private static final int VIEW_CONTENT = 1;
    private static final int VIEW_LOADING = 2;
    private static final int VIEW_ERROR = 3;
    private static final int PAGE_LIMIT = 2;
    private static final String EXTRA_STATE_TAB_POSITION = "extra_tab_position";
    private static final int TAB_POSITION_NOTE = 1;
    private static final int TAB_POSITION_INFO = 0;
    private String shopId;
    private ShopInfo shopInfo;
    private int tabPosition;
    private View loadingStateView;
    private View errorStateView;
    private View containerPager;
    private TextView textRetryError;
    private TextView buttonRetryError;

    @Inject
    ShopInfoPresenter shopInfoPresenter;

    @Inject
    ShopPageTracking shopPageTracking;

    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopInfoActivity.class);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        return intent;
    }

    @DeepLink(ShopAppLink.SHOP_NOTE)
    public static Intent getCallingIntentNoteSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopInfoActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_NOTE)
                .putExtras(extras);
    }

    @DeepLink(ShopAppLink.SHOP_INFO)
    public static Intent getCallingIntentInfoSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopInfoActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_INFO)
                .putExtras(extras);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getIntent().getStringExtra(ShopParamConstant.EXTRA_SHOP_ID);
        tabPosition = getIntent().getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_INFO);
        viewPager.setCurrentItem(tabPosition);
        initInjector();
        shopInfoPresenter.attachView(this);
        getShopInfo();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_info_page;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        tabLayout.addOnTabSelectedListener(getTabsListener());
        tabLayout.setupWithViewPager(viewPager);
        loadingStateView = findViewById(R.id.layout_loading_state);
        errorStateView = findViewById(R.id.layout_error_state);
        containerPager = findViewById(R.id.container_view_pager);
        textRetryError = findViewById(R.id.message_retry);
        buttonRetryError = findViewById(R.id.button_retry);
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        String titleArray[] = {
                getString(R.string.shop_info_title_tab_shop_info),
                getString(R.string.shop_info_title_tab_note),
        };
        return new ShopInfoPagerAdapter(getSupportFragmentManager(), titleArray);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        shopPageTracking.eventBackPressedShopInfo(shopId);
    }

    @NonNull
    private TabLayout.OnTabSelectedListener getTabsListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(shopPageTracking != null) {
                    shopPageTracking.eventClickTabShopInfo(getTitlePage(tab.getPosition()), shopId);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // no op
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // no op
            }
        };
    }

    @Override
    protected int getPageLimit() {
        return PAGE_LIMIT;
    }

    private void getShopInfo() {
        setViewState(VIEW_LOADING);
        shopInfoPresenter.getShopInfo(shopId);
    }

    @Override
    public void onSuccessGetShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
        updateTitle(MethodChecker.fromHtml(shopInfo.getInfo().getShopName()).toString());
        if (viewPager.getAdapter() instanceof ShopInfoPagerAdapter) {
            ShopInfoPagerAdapter adapter = (ShopInfoPagerAdapter) viewPager.getAdapter();
            ((ShopInfoFragment) adapter.getRegisteredFragment(TAB_POSITION_INFO)).updateShopInfo(shopInfo);
            ((ShopNoteListFragment) adapter.getRegisteredFragment(TAB_POSITION_NOTE)).updateShopInfo(shopInfo);
        }
        setViewState(VIEW_CONTENT);
    }

    @Override
    public void onErrorGetShopInfo(Throwable e) {
        setViewState(VIEW_ERROR);
        textRetryError.setText(ErrorHandler.getErrorMessage(this, e));
        buttonRetryError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getShopInfo();
            }
        });
    }

    private void onShareShop() {
        if (shopInfo != null) {
            ((ShopModuleRouter) getApplication()).goToShareShop(this, shopId, shopInfo.getInfo().getShopUrl(),
                    getString(R.string.shop_label_share_formatted, shopInfo.getInfo().getShopName(), shopInfo.getInfo().getShopLocation()));
        }
    }

    public void setViewState(int viewState) {
        switch (viewState) {
            case VIEW_CONTENT:
                loadingStateView.setVisibility(View.GONE);
                errorStateView.setVisibility(View.GONE);
                containerPager.setVisibility(View.VISIBLE);
                break;
            case VIEW_LOADING:
                loadingStateView.setVisibility(View.VISIBLE);
                errorStateView.setVisibility(View.GONE);
                containerPager.setVisibility(View.INVISIBLE);
                break;
            case VIEW_ERROR:
                loadingStateView.setVisibility(View.GONE);
                errorStateView.setVisibility(View.VISIBLE);
                containerPager.setVisibility(View.INVISIBLE);
                break;
            default:
                loadingStateView.setVisibility(View.GONE);
                errorStateView.setVisibility(View.GONE);
                containerPager.setVisibility(View.VISIBLE);
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shop_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            onShareShop();
        }
        return super.onOptionsItemSelected(item);
    }

    public CharSequence getTitlePage(int position) {
        switch (position) {
            case 0:
                return getString(R.string.shop_info_title_tab_shop_info);
            case 1:
                return getString(R.string.shop_info_title_tab_note);
            default:
                return "";
        }
    }

    @Override
    public ShopComponent getComponent() {
        return ShopComponentInstance.getComponent(getApplication());
    }

    private void initInjector() {
        DaggerShopInfoComponent
                .builder()
                .shopInfoModule(new ShopInfoModule())
                .shopComponent(getComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shopInfoPresenter != null) {
            shopInfoPresenter.detachView();
        }
    }
}