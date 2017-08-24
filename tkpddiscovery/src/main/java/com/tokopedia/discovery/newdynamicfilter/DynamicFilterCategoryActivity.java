package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    public static final String EXTRA_RESULT_SELECTED_CATEGORY_ID = "EXTRA_RESULT_SELECTED_CATEGORY_ID";
    public static final String EXTRA_RESULT_SELECTED_CATEGORY_NAME = "EXTRA_RESULT_SELECTED_CATEGORY_NAME";

    private static final String EXTRA_OPTION_LIST = "EXTRA_OPTION_LIST";

    List<Category> categoryList;
    private RecyclerView rootRecyclerView;
    private RecyclerView childRecyclerView;
    private View buttonClose;
    private CategoryParentAdapter categoryParentAdapter;
    private CategoryChildAdapter categoryChildAdapter;

    public static void moveTo(AppCompatActivity activity,
                              List<Option> optionList) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterCategoryActivity.class);
            intent.putExtra(EXTRA_OPTION_LIST, Parcels.wrap(optionList));
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
        Category defaultSelectedCategory = categoryList.get(0);
        categoryParentAdapter = new CategoryParentAdapter(this, defaultSelectedCategory.getId());
        categoryParentAdapter.setDataList(categoryList);
        rootRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rootRecyclerView.setAdapter(categoryParentAdapter);

        categoryChildAdapter = new CategoryChildAdapter(this);
        categoryChildAdapter.clear();
        categoryChildAdapter.addAll(defaultSelectedCategory.getChildren());
        childRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        childRecyclerView.setAdapter(categoryChildAdapter);
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
        intent.putExtra(EXTRA_RESULT_SELECTED_CATEGORY_ID, category.getId());
        intent.putExtra(EXTRA_RESULT_SELECTED_CATEGORY_NAME, category.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
}
