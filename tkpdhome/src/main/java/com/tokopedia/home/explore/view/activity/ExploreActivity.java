package com.tokopedia.home.explore.view.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.home.R;
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.home.explore.view.fragment.ExploreFragment;

public class ExploreActivity extends BaseTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(ExploreActivity.this, R.color.tab_indicator_beli));
                        break;
                    case 1:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(ExploreActivity.this, R.color.tab_indicator_bayar));
                        break;
                    case 2:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(ExploreActivity.this, R.color.tab_indicator_pesan));
                        break;
                    case 3:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(ExploreActivity.this, R.color.tab_indicator_ajukan));
                        break;
                    case 4:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(ExploreActivity.this, R.color.tab_indicator_jual));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupTabIcon(TabLayout.Tab tab, int icon, String label) {
        View view = LayoutInflater.from(this).inflate(R.layout.explore_tab_item, null, false);
        TextView labelTxt = view.findViewById(R.id.label);
        ImageView iconView = view.findViewById(R.id.icon);
        iconView.setImageResource(icon);
        labelTxt.setText(label);
        tab.setCustomView(view);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_explore;
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        return new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ExploreFragment.newInstance();
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        super.setupFragment(savedinstancestate);
        setupTabIcon(tabLayout.getTabAt(0), R.drawable.ic_beli, getString(R.string.beli));
        setupTabIcon(tabLayout.getTabAt(1), R.drawable.ic_bayar, getString(R.string.bayar));
        setupTabIcon(tabLayout.getTabAt(2), R.drawable.ic_pesan, getString(R.string.pesan));
        setupTabIcon(tabLayout.getTabAt(3), R.drawable.ic_ajukan, getString(R.string.ajukan));
        setupTabIcon(tabLayout.getTabAt(4), R.drawable.ic_jual, getString(R.string.jual));
    }

    @Override
    protected int getPageLimit() {
        return 3;
    }


}
