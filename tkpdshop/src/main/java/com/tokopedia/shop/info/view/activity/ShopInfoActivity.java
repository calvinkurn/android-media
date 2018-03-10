package com.tokopedia.shop.info.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.constant.ShopAppLink;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment;
import com.tokopedia.shop.note.view.fragment.ShopNoteListFragment;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopInfoActivity extends BaseTabActivity implements HasComponent<ShopComponent> {

    private static final int PAGE_LIMIT = 2;
    private static final String EXTRA_STATE_TAB_POSITION = "extra_tab_position";
    private static final int TAB_POSITION_NOTE = 1;
    private static final int TAB_POSITION_INFO = 0;
    private String shopId;
    private int tabPosition;

    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopInfoActivity.class);
        intent.putExtra(ShopParamConstant.SHOP_ID, shopId);
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
        shopId = getIntent().getStringExtra(ShopParamConstant.SHOP_ID);
        tabPosition = getIntent().getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_INFO);
        viewPager.setCurrentItem(tabPosition);
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
                        return getString(R.string.shop_info_title_tab_shop_info);
                    case 1:
                        return getString(R.string.shop_info_title_tab_note);
                    default:
                        return super.getPageTitle(position);
                }
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return ShopInfoFragment.createInstance(shopId);
                    case 1:
                        return ShopNoteListFragment.createInstance(shopId);
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
        return ShopComponentInstance.getComponent(getApplication());
    }
}
