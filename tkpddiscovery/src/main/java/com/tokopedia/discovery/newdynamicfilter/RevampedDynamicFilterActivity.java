package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactoryImpl;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDetailActivityRouter;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by henrypriyono on 8/8/17.
 */

public class RevampedDynamicFilterActivity extends AppCompatActivity implements DynamicFilterView {

    protected static final int REQUEST_CODE = 219;

    private static String EXTRA_FILTER_LIST = "EXTRA_FILTER_LIST";

    RecyclerView recyclerView;
    DynamicFilterAdapter adapter;

    public static void moveTo(AppCompatActivity fragmentActivity,
                              List<Filter> filterCategoryList) {
        if (fragmentActivity != null) {
            Intent intent = new Intent(fragmentActivity, RevampedDynamicFilterActivity.class);
            intent.putExtra(EXTRA_FILTER_LIST, Parcels.wrap(filterCategoryList));
            fragmentActivity.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revamped_dynamic_filter);
        bindView();
        initRecyclerView();
        loadFilterItems();
    }

    private void bindView() {
        recyclerView = (RecyclerView) findViewById(R.id.dynamic_filter_recycler_view);
    }

    private void initRecyclerView() {
        DynamicFilterTypeFactory dynamicFilterTypeFactory = new DynamicFilterTypeFactoryImpl(this);
        adapter = new DynamicFilterAdapter(dynamicFilterTypeFactory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void loadFilterItems() {
        List<Filter> filterList = Parcels.unwrap(
                getIntent().getParcelableExtra(EXTRA_FILTER_LIST));
        adapter.setFilterList(filterList);
    }

    @Override
    public void onExpandableItemClicked(Filter filter) {
        FilterDetailActivityRouter.launchDetailActivity(this, filter);
    }
}
