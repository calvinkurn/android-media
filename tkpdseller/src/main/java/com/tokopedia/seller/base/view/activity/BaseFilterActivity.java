package com.tokopedia.seller.base.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.fragment.BaseFilterContentFragment;
import com.tokopedia.seller.base.view.fragment.TopAdsFilterListFragment;
import com.tokopedia.seller.base.view.listener.BaseFilterContentViewListener;
import com.tokopedia.seller.base.view.model.FilterTitleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 5/26/17.
 *         just move to new architecture.
 */
public abstract class BaseFilterActivity extends TActivity implements TopAdsFilterListFragment.Callback, BaseFilterContentFragment.Callback {
    protected TopAdsFilterListFragment topAdsFilterListFragment;
    protected List<Fragment> filterContentFragmentList;
    protected int selectedPosition = 0;
    Fragment currentContentFragment;
    private Button submitButton;

    protected abstract List<Fragment> getFilterContentList();

    protected abstract Intent getDefaultIntentResult();

    protected int getLayoutId() {
        return R.layout.activity_top_ads_filter;
    }

    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFilterChangedResult();
                finish();
            }
        });
        filterContentFragmentList = getFilterContentList();
        topAdsFilterListFragment = TopAdsFilterListFragment.createInstance(getFilterTitleItemList(), selectedPosition);
        topAdsFilterListFragment.setCallback(this);
        currentContentFragment = filterContentFragmentList.get(selectedPosition);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_filter_list, topAdsFilterListFragment, TopAdsFilterListFragment.class.getSimpleName())
                .commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_filter_content, currentContentFragment, TopAdsFilterListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        }
        setContentView(getLayoutId());
        initView();
    }

    protected void setupBundlePass(Bundle extras) {

    }

    private void changeContent(Fragment filterContentFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // fragmentTransaction.replace(R.id.container_filter_content, filterContentFragment);
        fragmentTransaction.hide(currentContentFragment);
        if (!filterContentFragment.isAdded()) {
            fragmentTransaction.add(R.id.container_filter_content, filterContentFragment);
        } else {
            fragmentTransaction.show(filterContentFragment);
        }
        currentContentFragment = filterContentFragment;
        fragmentTransaction.commit();

        if (filterContentFragment instanceof BaseFilterContentViewListener) {
            ((BaseFilterContentViewListener) filterContentFragment).setCallback(this);
        }
    }

    private ArrayList<FilterTitleItem> getFilterTitleItemList() {
        ArrayList<FilterTitleItem> filterTitleItemList = new ArrayList<>();
        for (Fragment filterContentFragment : filterContentFragmentList) {
            if (filterContentFragment instanceof BaseFilterContentViewListener) {
                FilterTitleItem filterTitleItem = new FilterTitleItem();
                filterTitleItem.setTitle(((BaseFilterContentViewListener) filterContentFragment).getTitle(this));
                filterTitleItem.setActive(((BaseFilterContentViewListener) filterContentFragment).isActive());
                filterTitleItemList.add(filterTitleItem);
            }
        }
        return filterTitleItemList;
    }

    @Override
    public void onStatusChanged(boolean active) {
        topAdsFilterListFragment.setActive(selectedPosition, active);
    }

    @Override
    public void onItemSelected(int position) {
        selectedPosition = position;
        changeContent(filterContentFragmentList.get(position));
    }

    protected Intent setFilterChangedResult() {
        Intent intent = getDefaultIntentResult();
        // overwrite with changes
        for (Fragment topAdsFilterContentFragment : filterContentFragmentList) {
            // only process the added fragment, not-added-fragment has no adapter created.
            if (topAdsFilterContentFragment.isAdded()) {
                if (topAdsFilterContentFragment instanceof BaseFilterContentViewListener)
                    ((BaseFilterContentViewListener) topAdsFilterContentFragment).addResult(intent);
            }
        }
        setResult(Activity.RESULT_OK, intent);
        return intent;
    }


    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_ads_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_close) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public <C extends Fragment> C getCurrentFragment(int position, Class<C> classType) {
        if (filterContentFragmentList != null && filterContentFragmentList.size() > 0) {
            if (position >= 0 && position <= filterContentFragmentList.size() - 1) {
                return classType.cast(filterContentFragmentList.get(position));
            } else {
                throw new RuntimeException("please pass valid position");
            }
        } else {
            throw new RuntimeException("empty fragment list");
        }
    }
}
