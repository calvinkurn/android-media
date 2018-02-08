package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.categorynav.domain.model.Category;
import com.tokopedia.discovery.categorynav.view.adapter.CategoryChildAdapter;
import com.tokopedia.discovery.categorynav.view.adapter.CategoryParentAdapter;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by henrypriyono on 8/24/17.
 */

public class DynamicFilterCategoryActivity extends AppCompatActivity
        implements CategoryParentAdapter.OnItemClickListener, CategoryChildAdapter.OnItemClickListener {

    public static final int REQUEST_CODE = 221;
    public static final String EXTRA_SELECTED_CATEGORY_ID = "EXTRA_SELECTED_CATEGORY_ID";
    public static final String EXTRA_SELECTED_CATEGORY_ROOT_ID = "EXTRA_SELECTED_CATEGORY_ROOT_ID";
    public static final String EXTRA_SELECTED_CATEGORY_NAME = "EXTRA_SELECTED_CATEGORY_NAME";

    private static final String EXTRA_DEFAULT_CATEGORY_ID = "EXTRA_DEFAULT_CATEGORY_ID";
    private static final String EXTRA_DEFAULT_CATEGORY_ROOT_ID = "EXTRA_DEFAULT_CATEGORY_ROOT_ID";
    private static final String EXTRA_OPTION_LIST = "EXTRA_OPTION_LIST";
    private static final int DEFAULT_OFFSET = 170;

    List<Category> categoryList;
    private RecyclerView rootRecyclerView;
    private RecyclerView childRecyclerView;
    private View buttonClose;
    private CategoryParentAdapter categoryParentAdapter;
    private CategoryChildAdapter categoryChildAdapter;
    private String defaultCategoryId;
    private String defaultCategoryRootId;

    public static void moveTo(AppCompatActivity activity,
                              List<Option> optionList,
                              String defaultCategoryRootId,
                              String defaultCategoryId) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterCategoryActivity.class);
            intent.putExtra(EXTRA_OPTION_LIST, Parcels.wrap(optionList));
            intent.putExtra(EXTRA_DEFAULT_CATEGORY_ROOT_ID, defaultCategoryRootId);
            intent.putExtra(EXTRA_DEFAULT_CATEGORY_ID, defaultCategoryId);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_filter_category);
        fetchDataFromIntent();
        bindView();
        loadFilterItems();
    }

    private void fetchDataFromIntent() {
        defaultCategoryId
                = getIntent().getStringExtra(DynamicFilterCategoryActivity.EXTRA_DEFAULT_CATEGORY_ID);
        defaultCategoryRootId
                = getIntent().getStringExtra(DynamicFilterCategoryActivity.EXTRA_DEFAULT_CATEGORY_ROOT_ID);

         List<Option> optionList = Parcels.unwrap(
                getIntent().getParcelableExtra(EXTRA_OPTION_LIST));

        categoryList = OptionHelper.convertToCategoryList(optionList);
    }

    private void bindView() {
        rootRecyclerView = (RecyclerView) findViewById(R.id.category_root_recyclerview);
        childRecyclerView = (RecyclerView) findViewById(R.id.category_child_recyclerview);
        buttonClose = findViewById(R.id.top_bar_close_button);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadFilterItems() {
        if (TextUtils.isEmpty(defaultCategoryRootId)) {
            defaultCategoryRootId = categoryList.get(0).getId();
        }

        categoryParentAdapter = new CategoryParentAdapter(this, defaultCategoryRootId);
        categoryParentAdapter.setDataList(categoryList);

        LinearLayoutManager rootLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rootRecyclerView.setLayoutManager(rootLayoutManager);

        rootRecyclerView.setAdapter(categoryParentAdapter);
        int defaultRootPosition = categoryParentAdapter.getPositionById(defaultCategoryRootId);
        rootLayoutManager.scrollToPositionWithOffset(defaultRootPosition, DEFAULT_OFFSET);

        categoryChildAdapter = new CategoryChildAdapter(this);
        categoryChildAdapter.setLastSelectedCategoryId(defaultCategoryId);
        categoryChildAdapter.clear();
        categoryChildAdapter.addAll(categoryList.get(defaultRootPosition).getChildren());

        LinearLayoutManager childLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        childRecyclerView.setLayoutManager(childLayoutManager);

        childRecyclerView.setAdapter(categoryChildAdapter);

        if (!TextUtils.isEmpty(defaultCategoryId)) {
            categoryChildAdapter.toggleSelectedChildbyId(defaultCategoryId);
            childLayoutManager.scrollToPositionWithOffset(categoryChildAdapter.getActivePosition(), DEFAULT_OFFSET);
        }
    }

    @Override
    public void onItemClicked(Category category, int position) {
        categoryParentAdapter.setActiveId(category.getId());
        categoryParentAdapter.notifyDataSetChanged();
        categoryChildAdapter.clear();
        categoryChildAdapter.addAll(category.getChildren());
    }

    @Override
    public void onChildClicked(Category category) {
        if (category.getHasChild()) {
            categoryChildAdapter.toggleSelectedChildbyId(category.getId());
        } else {
            applyFilter(category);
        }
    }

    private void applyFilter(Category category) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_CATEGORY_ID, category.getId());
        intent.putExtra(EXTRA_SELECTED_CATEGORY_NAME, category.getName());
        intent.putExtra(EXTRA_SELECTED_CATEGORY_ROOT_ID, categoryParentAdapter.getActiveId());
        setResult(RESULT_OK, intent);
        finish();
    }
}
