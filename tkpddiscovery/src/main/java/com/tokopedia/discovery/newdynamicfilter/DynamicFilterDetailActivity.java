package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterDetailAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/16/17.
 */

public class DynamicFilterDetailActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 220;
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    protected static String EXTRA_OPTION_LIST = "EXTRA_OPTION_LIST";
    protected static String EXTRA_SEARCH_HINT = "EXTRA_SEARCH_HINT";
    protected static String EXTRA_IS_SEARCHABLE = "EXTRA_IS_SEARCHABLE";

    View searchInputContainer;
    EditText searchInputView;
    RecyclerView recyclerView;
    DynamicFilterDetailAdapter adapter;
    List<Option> optionList;
    OptionSearchFilter searchFilter;
    TextView buttonApply;
    TextView buttonReset;
    View buttonClose;

    private boolean isSearchable;
    private String searchHint;

    public static void moveTo(AppCompatActivity fragmentActivity,
                              List<Option> optionList,
                              boolean isSearchable,
                              String searchHint) {

        if (fragmentActivity != null) {
            Intent intent = new Intent(fragmentActivity, DynamicFilterDetailActivity.class);
            intent.putExtra(EXTRA_OPTION_LIST, Parcels.wrap(optionList));
            intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable);
            intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
            fragmentActivity.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_filter_detail);
        fetchDataFromIntent();
        bindView();
        initRecyclerView();
        loadFilterItems();
        initSearchView();
    }

    private void fetchDataFromIntent() {
        optionList = Parcels.unwrap(
                getIntent().getParcelableExtra(EXTRA_OPTION_LIST));
        isSearchable = getIntent().getBooleanExtra(EXTRA_IS_SEARCHABLE, false);
        searchHint = getIntent().getStringExtra(EXTRA_SEARCH_HINT);
    }

    private void bindView() {
        recyclerView = (RecyclerView) findViewById(R.id.filter_detail_recycler_view);
        searchInputView = (EditText) findViewById(R.id.filter_detail_search);
        searchInputContainer = findViewById(R.id.filter_detail_search_container);
        buttonClose = findViewById(R.id.top_bar_close_button);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        buttonReset = (TextView) findViewById(R.id.top_bar_button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter();
            }
        });
        buttonApply = (TextView) findViewById(R.id.button_apply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });
    }

    private void initSearchView() {
        if (!isSearchable) {
            searchInputContainer.setVisibility(View.GONE);
            return;
        }

        searchInputContainer.setVisibility(View.VISIBLE);
        searchInputView.setHint(searchHint);
        searchInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getSearchFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private OptionSearchFilter getSearchFilter() {
        if (searchFilter == null) {
            searchFilter = new OptionSearchFilter(optionList);
        }
        return searchFilter;
    }

    private void initRecyclerView() {
        adapter = getAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.line_separator_medium));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    protected DynamicFilterDetailAdapter getAdapter() {
        return new DynamicFilterDetailAdapter();
    }

    private void loadFilterItems() {
        adapter.setOptionList(optionList);
    }

    private void applyFilter() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, Parcels.wrap(optionList));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void resetFilter() {
        adapter.resetAllOptionsInputState();
    }

    public class OptionSearchFilter extends android.widget.Filter {
        private ArrayList<Option> sourceData;

        public OptionSearchFilter(List<Option> optionList) {
            sourceData = new ArrayList<>();
            synchronized (this) {
                sourceData.addAll(optionList);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterSeq = constraint.toString().toLowerCase();

            FilterResults result = new FilterResults();

            if (!TextUtils.isEmpty(filterSeq)) {

                ArrayList<Option> filter = new ArrayList<>();

                for (Option option : sourceData) {
                    if (option.getName().toLowerCase().contains(filterSeq)) {
                        filter.add(option);
                    }
                }

                result.values = filter;
                result.count = filter.size();

            } else {

                synchronized (this) {
                    result.values = sourceData;
                    result.count = sourceData.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Option> resultList = (List<Option>) results.values;
            adapter.setOptionList(resultList);
        }
    }
}
