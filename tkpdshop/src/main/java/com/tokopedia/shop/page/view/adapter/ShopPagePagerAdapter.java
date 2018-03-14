package com.tokopedia.shop.page.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragment;
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView;

/**
 * Created by normansyahputa on 3/13/18.
 */

public class ShopPagePagerAdapter extends FragmentStatePagerAdapter {
    private final String[] title;
    private final ShopModuleRouter shopModuleRouter;
    private final ShopPagePromoWebView.Listener listener;
    private final String shopId;
    private final String shopDomain;
    private SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<Fragment>();

    public ShopPagePagerAdapter(FragmentManager fm,
                                String[] title,
                                ShopModuleRouter shopModuleRouter,
                                ShopPagePromoWebView.Listener listener,
                                String shopId,
                                String shopDomain
    ) {
        super(fm);
        this.title = title;
        this.shopModuleRouter = shopModuleRouter;
        this.listener = listener;
        this.shopId = shopId;
        this.shopDomain = shopDomain;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return title[0];
            case 1:
                return title[1];
            case 2:
                return title[2];
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ShopProductListLimitedFragment shopProductListLimitedFragment = ShopProductListLimitedFragment.createInstance();
                shopProductListLimitedFragment.setPromoWebViewListener(listener);
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
        ShopProductListLimitedFragment shopProductListLimitedFragment = ShopProductListLimitedFragment.createInstance();
        shopProductListLimitedFragment.setPromoWebViewListener(listener);
        return shopProductListLimitedFragment;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object o = super.instantiateItem(container, position);
        registeredFragments.put(position, (Fragment) o);
        return o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
