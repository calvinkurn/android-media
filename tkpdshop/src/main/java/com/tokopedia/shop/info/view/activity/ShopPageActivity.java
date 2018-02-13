package com.tokopedia.shop.info.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.info.domain.interactor.GetSpeedReputationUseCase;
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment;
import com.tokopedia.shop.info.view.helper.ShopInfoHeaderViewHelper;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/3/18.
 */

public class ShopPageActivity extends BaseTabActivity  implements HasComponent<ShopComponent> {

    private static final int PAGE_LIMIT = 3;
    public static final String SHOP_INFO = "SHOP_INFO";

    public static Intent createIntent(Context context, String shopInfo) {
        Intent intent = new Intent(context, ShopPageActivity.class);
        intent.putExtra(SHOP_INFO, shopInfo);
        return intent;
    }

    private ShopComponent component;

    private ShopInfoHeaderViewHelper shopInfoHeaderViewHelper;

    @Inject
    GetShopInfoUseCase getShopInfoUseCase;

    @Inject
    UserSession userSession;

    GetSpeedReputationUseCase getSpeedReputationUseCase;

    private String shopInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(savedInstanceState == null){
            shopInfo = getIntent().getStringExtra(SHOP_INFO);
        }else{
            throw new RuntimeException("please pass shop id");
        }

        initInjector();
        super.onCreate(savedInstanceState);

        shopInfoHeaderViewHelper = new ShopInfoHeaderViewHelper(getWindow().getDecorView().getRootView(), userSession);

        if(getApplication() != null && getApplication() instanceof ShopModuleRouter){
            getSpeedReputationUseCase = new GetSpeedReputationUseCase(((ShopModuleRouter)getApplication()).getSpeedReputationUseCase());
        }
        getSpeedReputationUseCase.execute(RequestParams.EMPTY, new Subscriber<SpeedReputation>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SpeedReputation speedReputation) {
                shopInfoHeaderViewHelper.renderData(speedReputation);
            }
        });

        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopInfo), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                shopInfoHeaderViewHelper.renderData(shopInfo);
            }
        });
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
        return R.layout.activity_shop_tab;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        tabLayout.addOnTabSelectedListener(getTabsListener());
        tabLayout.setupWithViewPager(viewPager);
    }

    @NonNull
    private TabLayout.OnTabSelectedListener getTabsListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
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
                        return ShopInfoFragment.createInstance("");
                    case 1:
                        return ShopInfoFragment.createInstance("");
                    case 2:
                        return ShopInfoFragment.createInstance("");
                    default:
                        return null;
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
    public ShopComponent getComponent() {
        if (component == null) {
            component = ShopComponentInstance.getComponent(getApplication());
        }
        return component;
    }
}
