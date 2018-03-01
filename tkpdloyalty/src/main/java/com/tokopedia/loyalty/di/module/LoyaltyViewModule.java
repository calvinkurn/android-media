package com.tokopedia.loyalty.di.module;

import android.app.Activity;
import android.app.FragmentManager;

import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.view.adapter.LoyaltyPagerAdapter;
import com.tokopedia.loyalty.view.data.LoyaltyPagerItem;
import com.tokopedia.loyalty.view.fragment.PromoCodeFragment;
import com.tokopedia.loyalty.view.fragment.PromoCouponFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 30/11/17.
 */

@Module
public class LoyaltyViewModule {

    private final Activity activity;

    public LoyaltyViewModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    FragmentManager provideFragmentManager() {
        return activity.getFragmentManager();
    }

    @Provides
    LoyaltyPagerAdapter provideLoyaltyPagerAdapter(FragmentManager fragmentManager) {
        return new LoyaltyPagerAdapter(fragmentManager);
    }

    @Provides
    @LoyaltyScope
    @Named("coupon_active")
    List<LoyaltyPagerItem> provideLoyaltyPagerItemListCouponActive() {
        List<LoyaltyPagerItem> loyaltyPagerItemList = new ArrayList<>();
        loyaltyPagerItemList.add(
                new LoyaltyPagerItem.Builder()
                        .fragment(PromoCodeFragment.newInstance(
                                activity.getIntent()
                                        .getExtras()
                                        .getString(
                                                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM, ""),
                                activity.getIntent()
                                        .getExtras()
                                        .getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY, "")))
                        .position(0)
                        .tabTitle("Kode Promo")
                        .build()
        );
        loyaltyPagerItemList.add(
                new LoyaltyPagerItem.Builder()
                        .fragment(PromoCouponFragment.newInstance(
                                activity.getIntent()
                                        .getExtras()
                                        .getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM, ""),
                                activity.getIntent()
                                        .getExtras()
                                        .getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY, "")))
                        .position(0)
                        .tabTitle("Kupon Saya")
                        .build()
        );
        return loyaltyPagerItemList;
    }

    @Provides
    @LoyaltyScope
    @Named("coupon_not_active")
    List<LoyaltyPagerItem> provideLoyaltyPagerItemListCouponNotActive() {
        List<LoyaltyPagerItem> loyaltyPagerItemList = new ArrayList<>();
        loyaltyPagerItemList.add(
                new LoyaltyPagerItem.Builder()
                        .fragment(PromoCodeFragment.newInstance(
                                activity.getIntent()
                                        .getExtras()
                                        .getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM, ""),
                                activity.getIntent()
                                        .getExtras()
                                        .getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY, "")))
                        .position(0)
                        .tabTitle("Kode Promo")
                        .build()
        );
        return loyaltyPagerItemList;
    }

}
