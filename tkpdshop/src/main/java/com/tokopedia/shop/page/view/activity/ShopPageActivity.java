package com.tokopedia.shop.page.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.common.constant.ShopAppLink;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.info.view.activity.ShopInfoActivity;
import com.tokopedia.shop.page.di.component.DaggerShopPageComponent;
import com.tokopedia.shop.page.di.module.ShopPageModule;
import com.tokopedia.shop.page.view.listener.ShopPageView;
import com.tokopedia.shop.page.view.model.ShopPageViewModel;
import com.tokopedia.shop.page.view.presenter.ShopPagePresenter;
import com.tokopedia.shop.page.view.widget.ShopPageSubDetailView;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment;

import javax.inject.Inject;

/**
 * Created by nathan on 2/3/18.
 */

public class ShopPageActivity extends BaseTabActivity implements HasComponent<ShopComponent>, ShopPageView {

    public static final int MAX_RATING_STAR = 5;
    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_DOMAIN = "shop_domain";
    public static final String SHOP_STATUS_FAVOURITE = "SHOP_STATUS_FAVOURITE";

    private static final int REPUTATION_SPEED_LEVEL_VERY_FAST = 5;
    private static final int REPUTATION_SPEED_LEVEL_FAST = 4;
    private static final int REPUTATION_SPEED_LEVEL_NORMAL = 3;
    private static final int REPUTATION_SPEED_LEVEL_SLOW = 2;
    private static final int REPUTATION_SPEED_LEVEL_VERY_SLOW = 1;
    private static final int REPUTATION_SPEED_LEVEL_DEFAULT = 0;
    private static final int PAGE_LIMIT = 3;
    private static final String EXTRA_STATE_TAB_POSITION = "extra_tab_position";
    private static final int TAB_POSITION_HOME = 0;
    private static final int TAB_POSITION_TALK = 1;
    private static final int TAB_POSITION_REVIEW = 2;

    @Inject
    ShopPagePresenter shopPagePresenter;
    private ShopProductListLimitedFragment shopProductListLimitedFragment;
    private ImageView backgroundImageView;
    private ImageView shopIconImageView;
    private ImageView shopStatusImageView;
    private ImageView locationImageView;
    private TextView shopNameTextView;
    private TextView shopInfoLocationTextView;
    private LinearLayout shopTitleLinearLayout;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private ShopPageSubDetailView totalFavouriteDetailView;
    private ShopPageSubDetailView totalProductDetailView;
    private ShopPageSubDetailView reputationDetailView;
    private ShopPageSubDetailView productQualityDetailView;
    private ShopPageSubDetailView reputationSpeedDetailView;
    private ShopReputationView shopReputationView;
    private TextView totalFavouriteTextView;
    private TextView totalProductTextView;
    private RatingBar qualityRatingBar;
    private TextView qualityValueTextView;
    private ImageView speedImageView;
    private TextView speedValueTextView;
    private Button buttonManageShop;
    private Button buttonAddProduct;
    private Button buttonChatSeller;
    private Button buttonFavouriteShop;
    private String shopId;
    private String shopDomain;
    private boolean favouriteShop;
    private ShopComponent component;
    private ShopModuleRouter shopModuleRouter;
    private int tabPosition;
    private Menu menu;

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
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                .putExtras(extras);
    }


    @DeepLink(ShopAppLink.SHOP_TALK)
    public static Intent getCallingIntentTalkSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopPageActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_TALK)
                .putExtras(extras);
    }

    @DeepLink(ShopAppLink.SHOP_REVIEW)
    public static Intent getCallingIntentReviewSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopInfoActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_REVIEW)
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        shopPagePresenter.attachView(this);
        shopId = getIntent().getStringExtra(SHOP_ID);
        shopDomain = getIntent().getStringExtra(SHOP_DOMAIN);
        tabPosition = getIntent().getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME);
        if (!TextUtils.isEmpty(shopId)) {
            shopPagePresenter.getShopInfo(shopId);
        } else {
            shopPagePresenter.getShopInfoByDomain(shopDomain);
        }
        if (getApplication() != null && getApplication() instanceof ShopModuleRouter) {
            shopModuleRouter = (ShopModuleRouter) getApplication();
        }
        updateShopDiscussionIntent();
    }

    /**
     * Old Discussion fragment need this intent, need updated code
     * com.tokopedia.core.shopinfo.presenter.ShopTalkPresenterImpl
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
        backgroundImageView = findViewById(R.id.image_view_shop_background);
        shopIconImageView = findViewById(R.id.image_view_shop_icon);
        shopStatusImageView = findViewById(R.id.image_view_shop_status);
        shopNameTextView = findViewById(R.id.text_view_shop_name);
        locationImageView = findViewById(R.id.image_view_location);
        shopInfoLocationTextView = findViewById(R.id.text_view_location);
        shopTitleLinearLayout = findViewById(R.id.linear_layout_header_title);

        totalFavouriteDetailView = findViewById(R.id.sub_detail_view_total_favourite);
        totalProductDetailView = findViewById(R.id.sub_detail_view_total_product);
        reputationDetailView = findViewById(R.id.sub_detail_view_reputation);
        productQualityDetailView = findViewById(R.id.sub_detail_view_product_quality);
        reputationSpeedDetailView = findViewById(R.id.sub_detail_view_reputation_speed);

        totalFavouriteTextView = findViewById(R.id.text_view_total_favourite);
        totalProductTextView = findViewById(R.id.text_view_total_product);
        qualityRatingBar = findViewById(R.id.rating_bar_product_quality);
        qualityValueTextView = findViewById(R.id.text_view_product_quality_value);
        speedImageView = findViewById(R.id.image_view_speed);
        speedValueTextView = findViewById(R.id.text_view_speed_value);
        shopReputationView = findViewById(R.id.shop_reputation_view);

        buttonManageShop = findViewById(R.id.button_manage_shop);
        buttonAddProduct = findViewById(R.id.button_add_product);
        buttonChatSeller = findViewById(R.id.button_chat_seller);
        buttonFavouriteShop = findViewById(R.id.button_favourite_shop);
        appBarLayout = findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        shopTitleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ShopInfoActivity.createIntent(view.getContext(), shopId);
                view.getContext().startActivity(intent);
            }
        });

        reputationDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Reputation Click", Toast.LENGTH_LONG).show();
            }
        });
        productQualityDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Product Quality Click", Toast.LENGTH_LONG).show();
            }
        });
        reputationSpeedDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ShopProductListActivity.createIntent(ShopPageActivity.this, shopId));
            }
        });
        buttonFavouriteShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFavouriteShop.setEnabled(false);
                shopPagePresenter.toggleFavouriteShop(shopId);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(tabPosition);
        shopProductListLimitedFragment = ShopProductListLimitedFragment.createInstance();
        appBarLayout.addOnOffsetChangedListener(onAppbarOffsetChange());
        collapsingToolbarLayout.setTitle("");
    }

    private AppBarLayout.OnOffsetChangedListener onAppbarOffsetChange() {
        return new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    setCollapsedToolbar();
                    isShow = true;
                } else if (isShow) {
                    setExpandedToolbar();
                    isShow = false;
                }
            }
        };
    }

    private void setExpandedToolbar() {
        collapsingToolbarLayout.setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        if (menu != null && menu.size() > 0) {
            menu.findItem(R.id.action_share).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_share_white));
        }
    }

    public void setCollapsedToolbar() {
        collapsingToolbarLayout.setTitle(shopNameTextView.getText());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back_grey);
        if (menu != null && menu.size() > 0) {
            menu.findItem(R.id.action_share).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_share_grey));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shop_info, menu);
        this.menu = menu;
        return true;
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        return new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.shop_info_title_tab_product);
                    case 1:
                        return getString(R.string.shop_info_title_tab_review);
                    case 2:
                        return getString(R.string.shop_info_title_tab_discussion);
                    default:
                        return super.getPageTitle(position);
                }
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return shopProductListLimitedFragment;
                    case 1:
                        if (shopModuleRouter != null) {
                            return shopModuleRouter.getShopReputationFragmentShop(shopId, shopDomain);
                        }
                        break;
                    case 2:
                        if (shopModuleRouter != null) {
                            return shopModuleRouter.getShopTalkFragment();
                        }
                        break;
                }
                return shopProductListLimitedFragment;
            }

            @Override
            public int getCount() {
                return PAGE_LIMIT;
            }
        };
    }

    @Override
    protected int getPageLimit() {
        return PAGE_LIMIT;
    }

    @Override
    public void onSuccessGetShopPageInfo(final ShopPageViewModel shopPageViewModel) {
        updateShopInfo(shopPageViewModel.getShopInfo());
        updateReputationSpeed(shopPageViewModel.getReputationSpeed());
    }

    private void updateShopInfo(ShopInfo shopInfo) {
        shopId = shopInfo.getInfo().getShopId();
        favouriteShop = TextApiUtils.isValueTrue(shopInfo.getInfo().getShopAlreadyFavorited());
        shopProductListLimitedFragment.displayProduct(shopId, shopInfo.getInfo().getShopOfficialTop());

        ImageHandler.LoadImage(backgroundImageView, shopInfo.getInfo().getShopCover());
        shopNameTextView.setText(MethodChecker.fromHtml(shopInfo.getInfo().getShopName()).toString());
        ImageHandler.loadImageCircle2(shopIconImageView.getContext(), shopIconImageView, shopInfo.getInfo().getShopAvatar());

        if (TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsOfficial())) {
            displayAsOfficialStoreView(shopInfo);
        } else if (shopInfo.getInfo().isShopIsGoldBadge()) {
            displayAsGoldMerchantView(shopInfo);
            displayAsGeneralShop(shopInfo);
        } else {
            displayAsGeneralShop(shopInfo);
        }
        if (shopPagePresenter.getUserSession().getShopId().equals(shopId)) {
            displayAsBuyerShop();
        } else {
            displayAsSellerShop();
        }
        updateFavouriteButtonView();
    }

    private void displayAsOfficialStoreView(ShopInfo shopInfo) {
        shopStatusImageView.setVisibility(View.VISIBLE);
        shopStatusImageView.setImageResource(R.drawable.ic_badge_shop_official);
        locationImageView.setImageResource(R.drawable.ic_info_checked_grey);
        shopInfoLocationTextView.setText(getString(R.string.shop_page_label_authorized));

        totalFavouriteDetailView.setVisibility(View.VISIBLE);
        totalProductDetailView.setVisibility(View.VISIBLE);
        totalFavouriteTextView.setText(String.valueOf(shopInfo.getInfo().getShopTotalFavorit()));
        totalProductTextView.setText(String.valueOf(shopInfo.getInfo().getTotalActiveProduct()));
    }

    private void displayAsGoldMerchantView(ShopInfo shopInfo) {
        shopStatusImageView.setVisibility(View.VISIBLE);
        shopStatusImageView.setImageResource(R.drawable.ic_badge_shop_gm);
    }

    private void displayAsGeneralShop(ShopInfo shopInfo) {
        reputationDetailView.setVisibility(View.VISIBLE);
        productQualityDetailView.setVisibility(View.VISIBLE);
        locationImageView.setImageResource(R.drawable.ic_info_location_grey);
        shopInfoLocationTextView.setText(shopInfo.getInfo().getShopLocation());

        int set = (int) shopInfo.getStats().getShopBadgeLevel().getSet();
        int level = (int) shopInfo.getStats().getShopBadgeLevel().getLevel();
        shopReputationView.setValue(set, level, shopInfo.getStats().getShopReputationScore());

        qualityValueTextView.setText(shopInfo.getRatings().getQuality().getAverage());
        qualityRatingBar.setRating(shopInfo.getRatings().getQuality().getRatingStar());
        qualityRatingBar.setMax(MAX_RATING_STAR);
    }

    private void displayAsBuyerShop() {
        buttonManageShop.setVisibility(View.VISIBLE);
        buttonAddProduct.setVisibility(View.VISIBLE);
    }

    private void displayAsSellerShop() {
        buttonChatSeller.setVisibility(View.VISIBLE);
        buttonFavouriteShop.setVisibility(View.VISIBLE);
    }

    private void updateFavouriteButtonView() {
        buttonFavouriteShop.setText(favouriteShop ? getString(R.string.shop_page_label_already_favorite) : getString(R.string.shop_page_label_favorite));
    }

    @Override
    public void onErrorGetShopPageInfo(Throwable e) {

    }

    public void updateReputationSpeed(ReputationSpeed reputationSpeed) {
        speedImageView.setImageResource(getReputationSpeedIcon(reputationSpeed.getRecent1Month().getSpeedLevel()));
        speedValueTextView.setText(reputationSpeed.getRecent1Month().getSpeedLevelDescription());
    }

    @Override
    public void onSuccessToggleFavourite(boolean successValue) {
        if (successValue) {
            favouriteShop = !favouriteShop;
            updateFavouriteButtonView();
            updateFavouriteResult();
        }
        buttonFavouriteShop.setEnabled(true);
    }

    private void updateFavouriteResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SHOP_STATUS_FAVOURITE, favouriteShop);
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    public void onErrorToggleFavourite(Throwable e) {
        buttonFavouriteShop.setEnabled(true);
    }

    @DrawableRes
    private int getReputationSpeedIcon(int level) {
        switch (level) {
            case REPUTATION_SPEED_LEVEL_VERY_FAST:
                return R.drawable.ic_shop_reputation_speed_very_fast;
            case REPUTATION_SPEED_LEVEL_FAST:
                return R.drawable.ic_shop_reputation_speed_fast;
            case REPUTATION_SPEED_LEVEL_NORMAL:
                return R.drawable.ic_shop_reputation_speed_normal;
            case REPUTATION_SPEED_LEVEL_SLOW:
                return R.drawable.ic_shop_reputation_speed_slow;
            case REPUTATION_SPEED_LEVEL_VERY_SLOW:
                return R.drawable.ic_shop_reputation_speed_very_slow;
            default:
                return R.drawable.ic_shop_reputation_speed_default;
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
        shopPagePresenter.detachView();
    }
}