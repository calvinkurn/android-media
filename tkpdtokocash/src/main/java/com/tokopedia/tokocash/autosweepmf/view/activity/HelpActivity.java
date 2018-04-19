package com.tokopedia.tokocash.autosweepmf.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.autosweepmf.view.adapter.HelpPagerAdapter;

import java.util.Arrays;

/**
 * Help page basically to show auto sweep works,uses and policy. This screen is contain a tabbed based ViewPager
 */
public class HelpActivity extends BaseSimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        ViewPager viewPager = findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager.setAdapter(new HelpPagerAdapter(getSupportFragmentManager(), Arrays.asList(TkpdBaseURL.AutoSweep.WEB_LINK_MF_USE,
                TkpdBaseURL.AutoSweep.WEB_LINK_MF_RETURN_POLICY)));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        updateTitle(getString(R.string.mf_title_autosweep_help));
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_help;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, HelpActivity.class);
    }
}
