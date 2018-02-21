package com.tokopedia.shop.info.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.view.listener.ShopPageView;
import com.tokopedia.shop.info.view.presenter.ShopPagePresenter;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment;

import java.util.Random;

import javax.inject.Inject;

/**
 * Created by nathan on 2/3/18.
 */

public class ShopPageActivity extends BaseTabActivity implements HasComponent<ShopComponent>, ShopPageView {

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

    private static final int SHOP_OFFICIAL_VALUE = 1;
    private static final int PAGE_LIMIT = 3;
    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_DOMAIN = "SHOP_DOMAIN";

    private ImageView backgroundImageView;

    private ImageView shopIconImageView;
    private ImageView shopStatusImageView;
    private ImageView locationImageView;
    private TextView shopNameTextView;
    private TextView shopInfoLocationTextView;
    private LinearLayout containerClickInfo;

    private ShopReputationView shopReputationView;

    private RatingBar qualityRatingBar;
    private TextView qualityValueTextView;

    private ImageView speedImageView;
    private TextView speedValueTextView;

    private Button buttonManageShop;
    private Button buttonAddProduct;
    private Button buttonChatSeller;
    private Button buttonShopPage;
    private Button button;

    private String shopId;
    private String shopDomain;

    private ShopComponent component;
    private ShopModuleRouter shopModuleRouter;

    @Inject
    ShopPagePresenter shopPagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        shopPagePresenter.attachView(this);
        shopId = getIntent().getStringExtra(SHOP_ID);
        shopDomain = getIntent().getStringExtra(SHOP_DOMAIN);
        shopPagePresenter.getShopInfo(shopId);
        if (getApplication() != null && getApplication() instanceof ShopModuleRouter) {
            shopModuleRouter = (ShopModuleRouter) getApplication();
        }
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

        qualityRatingBar = findViewById(R.id.rating_bar_product_quality);
        qualityValueTextView = findViewById(R.id.text_view_product_quality_value);
        speedImageView = findViewById(R.id.image_view_speed);
        speedValueTextView = findViewById(R.id.text_view_speed_value);

        shopReputationView = findViewById(R.id.shop_reputation_view);



        containerClickInfo = findViewById(R.id.container_click_info);
        buttonManageShop = findViewById(R.id.button_manage_shop);
        buttonAddProduct = findViewById(R.id.button_add_product);
        buttonChatSeller = findViewById(R.id.button_chat_seller);
        buttonShopPage = findViewById(R.id.button_shop_page);
        button = findViewById(R.id.button);
        tabLayout.setupWithViewPager(viewPager);
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
                        return ShopProductListLimitedFragment.createInstance(shopId);
                    case 1:
                        if (shopModuleRouter != null) {
                            return shopModuleRouter.getShopReputationFragmentShop(shopId, shopDomain);
                        }
                        return ShopProductListLimitedFragment.createInstance(shopId);
                    case 2:
                        if (shopModuleRouter != null) {
                            return shopModuleRouter.getShopTalkFragment();
                        }
                        return ShopProductListLimitedFragment.createInstance(shopId);
                    default:
                        return ShopProductListLimitedFragment.createInstance(shopId);
                }
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
    public void onSuccessGetShopInfo(final ShopInfo shopInfo) {
        ImageHandler.LoadImage(backgroundImageView, shopInfo.getInfo().getShopCover());
        shopNameTextView.setText(MethodChecker.fromHtml(shopInfo.getInfo().getShopName()).toString());
        ImageHandler.loadImageCircle2(shopIconImageView.getContext(), shopIconImageView, shopInfo.getInfo().getShopAvatar());

        if (TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsOfficial())) {
            displayOfficialStoreView(shopInfo);
        } else if (TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsGold())) {
            displayGoldMerchantView(shopInfo);
        } else {
            displayRegularMerchantView(shopInfo);
        }

        containerClickInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ShopInfoActivity.createIntent(view.getContext(), shopInfo.getInfo().getShopId());
                view.getContext().startActivity(intent);
            }
        });

        findViewById(R.id.reputation_click_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Reputation Click", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.product_quality_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Product Quality Click", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.speed_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Speed Click", Toast.LENGTH_LONG).show();
                button.setVisibility(new Random().nextBoolean() ? View.VISIBLE : View.GONE);
            }
        });

        buttonManageShop.setVisibility(View.GONE);
        buttonAddProduct.setVisibility(View.GONE);
        buttonChatSeller.setVisibility(View.GONE);
        buttonShopPage.setVisibility(View.GONE);

        if (shopPagePresenter.getUserSession().getShopId().equals(shopInfo.getInfo().getShopId())) {
            buttonManageShop.setVisibility(View.VISIBLE);
            buttonAddProduct.setVisibility(View.VISIBLE);
        } else {
            buttonChatSeller.setVisibility(View.VISIBLE);
            buttonShopPage.setVisibility(View.VISIBLE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.getContext().startActivity(ShopProductListActivity.createIntent(button.getContext(), shopId));
            }
        });
    }

    private void displayOfficialStoreView(ShopInfo shopInfo) {
        shopStatusImageView.setImageResource(R.drawable.ic_badge_shop_gm);
        locationImageView.setImageResource(R.drawable.ic_info_authorized);
        shopInfoLocationTextView.setText(getString(R.string.shop_page_label_authorized));
    }

    private void displayGoldMerchantView(ShopInfo shopInfo) {
        shopStatusImageView.setImageResource(R.drawable.ic_badge_shop_gm);
        displaygeneralShop(shopInfo);
    }

    private void displayRegularMerchantView(ShopInfo shopInfo) {
        shopStatusImageView.setImageResource(R.drawable.ic_badge_shop_regular);
        displaygeneralShop(shopInfo);
    }

    private void displaygeneralShop(ShopInfo shopInfo) {
        locationImageView.setImageResource(R.drawable.ic_info_location);
        shopInfoLocationTextView.setText(shopInfo.getInfo().getShopLocation());
        int set = (int) shopInfo.getStats().getShopBadgeLevel().getSet();
        int level = (int) shopInfo.getStats().getShopBadgeLevel().getLevel();
        shopReputationView.setValue(set, level, shopInfo.getStats().getShopReputationScore());

        qualityValueTextView.setText(shopInfo.getRatings().getQuality().getAverage());
        long ratingStar = shopInfo.getRatings().getQuality().getRatingStar();
        qualityRatingBar.setMax(5);
        qualityRatingBar.setRating(ratingStar);
    }

    @Override
    public void onErrorGetShopInfo(Throwable e) {

    }

    @Override
    public void onSuccessGetReputationSpeed(ReputationSpeed reputationSpeed) {
        speedValueTextView.setText(reputationSpeed.getRecent1Month().getSpeedLevelDescription());
    }

    @Override
    public void onErrorGetReputationSpeed(Throwable e) {

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