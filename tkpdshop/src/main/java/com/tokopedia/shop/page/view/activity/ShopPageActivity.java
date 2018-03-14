package com.tokopedia.shop.page.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.bottomsheet.BottomSheetCustomContentView;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTracking;
import com.tokopedia.shop.common.constant.ShopAppLink;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.favourite.view.activity.ShopFavouriteListActivity;
import com.tokopedia.shop.info.view.activity.ShopInfoActivity;
import com.tokopedia.shop.page.di.component.DaggerShopPageComponent;
import com.tokopedia.shop.page.di.module.ShopPageModule;
import com.tokopedia.shop.page.view.holder.ShopPageHeaderViewHolder;
import com.tokopedia.shop.page.view.listener.ShopPageView;
import com.tokopedia.shop.page.view.model.ShopPageViewModel;
import com.tokopedia.shop.page.view.presenter.ShopPagePresenter;
import com.tokopedia.shop.page.view.widget.ShopPageViewPager;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;
import com.tokopedia.shop.page.view.adapter.ShopPagePagerAdapter;
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment;
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView;

import javax.inject.Inject;

/**
 * Created by nathan on 2/3/18.
 */

public class ShopPageActivity extends BaseTabActivity implements ShopPagePromoWebView.Listener, ShopPageHeaderViewHolder.Listener, HasComponent<ShopComponent>, ShopPageView {

    public static final String APP_LINK_EXTRA_SHOP_ID = "shop_id";
    private static final String SHOP_ID = "EXTRA_SHOP_ID";
    private static final String SHOP_DOMAIN = "EXTRA_SHOP_DOMAIN";
    private static final String SHOP_STATUS_FAVOURITE = "SHOP_STATUS_FAVOURITE";
    private static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";
    private static final int REQUEST_CODER_USER_LOGIN = 100;
    private static final int PAGE_LIMIT = 3;
    private static final int TAB_POSITION_HOME = 0;
    private static final int TAB_POSITION_TALK = 1;
    private static final int TAB_POSITION_REVIEW = 2;
    private static final int VIEW_CONTENT = 1;
    private static final int VIEW_LOADING = 2;
    private static final int VIEW_ERROR = 3;
    @Inject
    ShopPagePresenter shopPagePresenter;
    @Inject
    ShopPageTracking shopPageTracking;

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private View loadingStateView;
    private View errorStateView;
    private View containerPager;
    private TextView textRetryError;
    private TextView buttonRetryError;

    private ShopPageHeaderViewHolder shopPageViewHolder;

    private String shopId;
    private String shopDomain;

    private ShopComponent component;
    private ShopModuleRouter shopModuleRouter;
    private int tabPosition;
    private Menu menu;
    private ShopInfo shopInfo;
    private String shopName;

    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopPageActivity.class);
        intent.putExtra(SHOP_ID, shopId);
        return intent;
    }

    public static Intent createIntentWithDomain(Context context, String shopDomain) {
        Intent intent = new Intent(context, ShopPageActivity.class);
        intent.putExtra(SHOP_DOMAIN, shopDomain);
        return intent;
    }

    @DeepLink(ShopAppLink.SHOP)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopPageActivity.class)
                .setData(uri.build())
                .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                .putExtras(extras);
    }

    @DeepLink(ShopAppLink.SHOP_TALK)
    public static Intent getCallingIntentTalkSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopPageActivity.class)
                .setData(uri.build())
                .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_TALK)
                .putExtras(extras);
    }

    @DeepLink(ShopAppLink.SHOP_REVIEW)
    public static Intent getCallingIntentReviewSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopPageActivity.class)
                .setData(uri.build())
                .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_REVIEW)
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(SHOP_ID);
        shopDomain = getIntent().getStringExtra(SHOP_DOMAIN);
        if (getApplication() != null && getApplication() instanceof ShopModuleRouter) {
            shopModuleRouter = (ShopModuleRouter) getApplication();
        }
        super.onCreate(savedInstanceState);
        initInjector();
        shopPagePresenter.attachView(this);
        tabPosition = getIntent().getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME);
        viewPager.setCurrentItem(tabPosition);
        getShopInfo();
        updateShopDiscussionIntent();
    }

    private void getShopInfo() {
        if (!TextUtils.isEmpty(shopId)) {
            shopPagePresenter.getShopInfo(shopId);
        } else {
            shopPagePresenter.getShopInfoByDomain(shopDomain);
        }
        setViewState(VIEW_LOADING);
    }

    /**
     * Old Discussion fragment need this intent, need updated code
     * com.tokopedia.core.shopInfo.presenter.ShopTalkPresenterImpl
     */
    @Deprecated
    private void updateShopDiscussionIntent() {
        getIntent().putExtra("shop_id", shopId);
        getIntent().putExtra("shop_domain", shopDomain);
    }

    private void initInjector() {
        DaggerShopPageComponent
                .builder()
                .shopPageModule(new ShopPageModule())
                .shopComponent(getComponent())
                .build()
                .inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_page;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        shopPageViewHolder = new ShopPageHeaderViewHolder(findViewById(android.R.id.content), this);
        appBarLayout = findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        loadingStateView = findViewById(R.id.shop_page_loading_state);
        errorStateView = findViewById(R.id.shop_page_error_state);
        containerPager = findViewById(R.id.container_view_pager);
        textRetryError = findViewById(R.id.message_retry);
        buttonRetryError = findViewById(R.id.button_retry);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle tracking back pressed on toolbar
                shopPageTracking.eventBackPressed(getTitlePage(viewPager.getCurrentItem()), shopId,
                        shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(shopPageTracking != null) {
                    shopPageTracking.eventClickTabShopPage(getTitlePage(tab.getPosition()), shopId,
                            shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
        appBarLayout.addOnOffsetChangedListener(onAppbarOffsetChange());
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.font_black_primary_70));
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setTitle(" ");
    }

    private AppBarLayout.OnOffsetChangedListener onAppbarOffsetChange() {
        return new AppBarLayout.OnOffsetChangedListener() {
            private static final float OFFSET_TOOLBAR_TITLE_SHOWN = 1f;
            boolean toolbarTitleShown = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                float percentage = (float) Math.abs(verticalOffset) / (float) scrollRange;
                if (percentage < OFFSET_TOOLBAR_TITLE_SHOWN && toolbarTitleShown) {
                    showToolbarTitle(false);
                    toolbarTitleShown = false;
                } else if (percentage >= OFFSET_TOOLBAR_TITLE_SHOWN && !toolbarTitleShown) {
                    showToolbarTitle(true);
                    toolbarTitleShown = true;
                }
            }

            private void showToolbarTitle(boolean show) {
                String title = show ? shopName : " ";
                int icBackRes = show ? R.drawable.ic_action_back_grey : R.drawable.ic_action_back;
                int icShareRes = show ? R.drawable.ic_share_grey : R.drawable.ic_share_white;
                collapsingToolbarLayout.setTitle(title);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setHomeAsUpIndicator(icBackRes);
                }
                if (menu != null && menu.size() > 0) {
                    menu.findItem(R.id.action_share).setIcon(ContextCompat.getDrawable(ShopPageActivity.this, icShareRes));
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shop_page, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            onShareShop();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        String title[] = {
                getString(R.string.shop_info_title_tab_product),
                getString(R.string.shop_info_title_tab_review),
                getString(R.string.shop_info_title_tab_discussion)
        };
        return new ShopPagePagerAdapter(getSupportFragmentManager(),
                title,
                shopModuleRouter,
                this,
                shopId,
                shopDomain);
    }

    @Override
    protected int getPageLimit() {
        return PAGE_LIMIT;
    }

    @Override
    public void webViewTouched(boolean touched) {
        if (viewPager instanceof ShopPageViewPager) {
            CommonUtils.dumper("touched: " + touched);
            ((ShopPageViewPager) viewPager).setPagingEnabled(!touched);
        }
    }

    private void onShareShop() {
        shopPageTracking.eventClickShareShop(getTitlePage(viewPager.getCurrentItem()), shopId,
                shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
        ((ShopModuleRouter) getApplication()).goToShareShop(this, shopId, shopInfo.getInfo().getShopUrl(),
                getString(R.string.shop_label_share_formatted, shopName, shopInfo.getInfo().getShopLocation()));
    }

    @Override
    public void goToShopInfo() {
        Intent intent = ShopInfoActivity.createIntent(this, shopId);
        startActivity(intent);
    }

    @Override
    public void onTotalFavouriteClicked() {
        shopPageTracking.eventClickListFavourite(getTitlePage(viewPager.getCurrentItem()), shopId,
                shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
        Intent intent = ShopFavouriteListActivity.createIntent(this, shopId);
        startActivity(intent);
    }

    @Override
    public void onTotalProductClicked() {
        shopPageTracking.eventClickTotalProduct(getTitlePage(viewPager.getCurrentItem()), shopId, shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
        Intent intent = ShopProductListActivity.createIntent(this, shopId);
        startActivity(intent);
    }

    @Override
    public void onManageShopClicked() {
        shopPageTracking.eventClickShopSetting(getTitlePage(viewPager.getCurrentItem()), shopId);
        ((ShopModuleRouter) getApplication()).goToManageShop(this);
    }

    @Override
    public void onAddProductClicked() {
        shopPageTracking.eventClickAddProduct(getTitlePage(viewPager.getCurrentItem()), shopId);
        ((ShopModuleRouter) getApplication()).goToAddProduct(this);
    }

    @Override
    public void onChatSellerClicked() {
        shopPageTracking.eventClickMessageShop(getTitlePage(viewPager.getCurrentItem()), shopId, shopPagePresenter.isMyShop(shopId),
                ShopPageTracking.getShopType(shopInfo.getInfo()));
        if (shopInfo != null) {
            ((ShopModuleRouter) getApplication()).goToChatSeller(ShopPageActivity.this, shopId, shopName, shopInfo.getInfo().getShopAvatar());
        }
    }

    @Override
    public void onShopIconClicked() {
        goToShopInfo();
        shopPageTracking.eventClickShopLogo(getTitlePage(viewPager.getCurrentItem()), shopId,
                shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
    }

    @Override
    public void onShopInfoClicked() {
        goToShopInfo();
        shopPageTracking.eventClickShopInfo(getTitlePage(viewPager.getCurrentItem()), shopId, shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
    }

    @Override
    public void onShopNameClicked() {
        goToShopInfo();
        shopPageTracking.eventClickShopName(getTitlePage(viewPager.getCurrentItem()), shopId,
                shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
    }

    @Override
    public void displayQualityInfo(String qualityAverage, @DrawableRes int qualityRatingStarImageRes, String totalReview) {
        View qualityContentBottomSheet = getLayoutInflater().inflate(R.layout.partial_shop_page_bottom_sheet_product_quality, null);
        ImageView ratingBarImageView = qualityContentBottomSheet.findViewById(R.id.image_view_rating_bar);
        ratingBarImageView.setImageResource(qualityRatingStarImageRes);

        TextView averageTextView = qualityContentBottomSheet.findViewById(R.id.text_view_product_quality);
        averageTextView.setText(qualityAverage);

        TextView reviewInfoTextView = qualityContentBottomSheet.findViewById(R.id.text_view_review_info);
        reviewInfoTextView.setText(getString(R.string.shop_page_bottom_sheet_product_quality_review_info, totalReview));

        BottomSheetCustomContentView bottomSheetView = new BottomSheetCustomContentView(this);
        bottomSheetView.setCustomContentLayout(qualityContentBottomSheet);
        bottomSheetView.renderBottomSheet(new BottomSheetCustomContentView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getString(R.string.shop_page_bottom_sheet_product_quality_title))
                .setBody(getString(R.string.shop_page_bottom_sheet_product_quality_description))
                .setCloseButton(getString(R.string.see_shop_information))
                .build());
        bottomSheetView.setBtnCloseOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToShopInfo();
            }
        });
        bottomSheetView.show();
    }

    @Override
    public void displayReputationInfo(int reputationMedalType, int reputationLevel, String reputationScore) {
        View speedContentBottomSheet = getLayoutInflater().inflate(R.layout.partial_shop_page_bottom_sheet_reputation, null);
        ShopReputationView shopReputationView = speedContentBottomSheet.findViewById(R.id.shop_reputation_view);

        TextView reputationDesc = speedContentBottomSheet.findViewById(R.id.shop_reputation_desc);
        shopReputationView.setValue(reputationMedalType, reputationLevel, reputationScore);
        reputationDesc.setText(getString(R.string.dashboard_x_points, reputationScore));
        if (TextUtils.isEmpty(reputationScore)) {
            reputationDesc.setText(R.string.reputation_value_not_appear);
        } else {
            reputationDesc.setText(getString(R.string.reputation_point_format, reputationScore));
        }

        BottomSheetCustomContentView bottomSheetView = new BottomSheetCustomContentView(this);
        bottomSheetView.setCustomContentLayout(speedContentBottomSheet);
        bottomSheetView.renderBottomSheet(new BottomSheetCustomContentView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getString(R.string.shop_page_bottom_sheet_reputation_title))
                .setBody(getString(R.string.shop_page_bottom_sheet_reputation_description))
                .setCloseButton(getString(R.string.see_shop_information))
                .build());
        bottomSheetView.setBtnCloseOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToShopInfo();
            }
        });
        bottomSheetView.show();
    }

    @Override
    public void displayReputationSpeedInfo(@DrawableRes int speedIcon, int speedLevel, String speedLevelDescription) {
        shopPageTracking.eventClickShopSpeed(getTitlePage(viewPager.getCurrentItem()), shopId, shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
        View speedContentBottomSheet = getLayoutInflater().inflate(R.layout.partial_shop_page_bottom_sheet_speed, null);
        AppCompatImageView imageViewSpeed = speedContentBottomSheet.findViewById(R.id.image_view_speed_content);
        imageViewSpeed.setImageResource(speedIcon);
        TextView speedView = speedContentBottomSheet.findViewById(R.id.text_view_speed_info);
        if (!TextUtils.isEmpty(speedLevelDescription)) {
            speedView.setText(speedLevelDescription);
        } else {
            speedView.setText(R.string.shop_page_speed_shop_not_available);
        }

        BottomSheetCustomContentView bottomSheetView = new BottomSheetCustomContentView(this);
        bottomSheetView.setCustomContentLayout(speedContentBottomSheet);
        bottomSheetView.renderBottomSheet(new BottomSheetCustomContentView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getString(R.string.shop_page_bottom_sheet_speed_title))
                .setBody(getString(R.string.shop_page_bottom_sheet_speed_description))
                .setCloseButton(getString(R.string.see_shop_information))
                .build());
        bottomSheetView.setBtnCloseOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopPageTracking.eventClickShopSpeedInfo(getTitlePage(viewPager.getCurrentItem()), shopId, shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
                goToShopInfo();
            }
        });
        bottomSheetView.show();
    }

    @Override
    public void onToggleFavouriteShop(boolean favouriteShop) {
        shopPageTracking.eventClickFavouriteShop(getTitlePage(viewPager.getCurrentItem()), shopId, favouriteShop,
                shopPagePresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
        shopPagePresenter.toggleFavouriteShop(shopId);
    }

    @Override
    public void onSuccessGetShopPageInfo(final ShopPageViewModel shopPageViewModel) {
        setViewState(VIEW_CONTENT);
        shopInfo = shopPageViewModel.getShopInfo();
        shopName = MethodChecker.fromHtml(shopInfo.getInfo().getShopName()).toString();

        if (viewPager.getAdapter() instanceof ShopPagePagerAdapter) {
            ShopPagePagerAdapter adapter = (ShopPagePagerAdapter) viewPager.getAdapter();
            ((ShopProductListLimitedFragment) adapter.getRegisteredFragment(0))
                    .displayProduct(shopId, shopInfo.getInfo().getShopOfficialTop());
        }
        shopPageViewHolder.renderData(shopPageViewModel, shopPagePresenter.isMyShop(shopId));
        shopPageTracking.eventViewShopPage(getTitlePage(viewPager.getCurrentItem()), shopId);
    }

    @Override
    public void onErrorGetShopPageInfo(Throwable e) {
        setViewState(VIEW_ERROR);
        textRetryError.setText(ErrorHandler.getErrorMessage(this, e));
        buttonRetryError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getShopInfo();
            }
        });
    }

    @Override
    public void onSuccessToggleFavourite(boolean successValue) {
        if (successValue) {
            shopPageViewHolder.toggleFavouriteShopStatus();
            updateFavouriteResult();
        }
        shopPageViewHolder.updateFavouriteButtonView();
    }

    private void updateFavouriteResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SHOP_STATUS_FAVOURITE, shopPageViewHolder.isFavouriteShop());
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    public void onErrorToggleFavourite(Throwable e) {
        shopPageViewHolder.updateFavouriteButtonView();
        if (e instanceof UserNotLoginException) {
            Intent intent = ((ShopModuleRouter) getApplication()).getLoginIntent(this);
            startActivityForResult(intent, REQUEST_CODER_USER_LOGIN);
            return;
        }
        NetworkErrorHelper.showCloseSnackbar(this, ErrorHandler.getErrorMessage(this, e));
    }

    public void setViewState(int viewState) {
        switch (viewState) {
            case VIEW_CONTENT:
                loadingStateView.setVisibility(View.GONE);
                errorStateView.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.VISIBLE);
                containerPager.setVisibility(View.VISIBLE);
                break;
            case VIEW_LOADING:
                loadingStateView.setVisibility(View.VISIBLE);
                errorStateView.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.INVISIBLE);
                containerPager.setVisibility(View.INVISIBLE);
                break;
            case VIEW_ERROR:
                loadingStateView.setVisibility(View.GONE);
                errorStateView.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.INVISIBLE);
                containerPager.setVisibility(View.INVISIBLE);
                break;
            default:
                loadingStateView.setVisibility(View.GONE);
                errorStateView.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.VISIBLE);
                containerPager.setVisibility(View.VISIBLE);
                break;
        }
    }

    private String getTitlePage(int tabPosition){
        switch (tabPosition) {
            case 0:
                return getString(R.string.shop_info_title_tab_product);
            case 1:
                return getString(R.string.shop_info_title_tab_review);
            case 2:
                return getString(R.string.shop_info_title_tab_discussion);
            default:
                return "";
        }
    }

    @Override
    public ShopComponent getComponent() {
        if (component == null) {
            component = ShopComponentInstance.getComponent(getApplication());
        }
        return component;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shopPagePresenter != null) {
            shopPagePresenter.detachView();
        }
    }
}
