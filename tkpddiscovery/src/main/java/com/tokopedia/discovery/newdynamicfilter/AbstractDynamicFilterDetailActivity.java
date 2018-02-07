package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.search.EmptySearchResultView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterDetailView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by henrypriyono on 8/16/17.
 */

public abstract class AbstractDynamicFilterDetailActivity<T extends RecyclerView.Adapter>
        extends BaseActivity implements DynamicFilterDetailView {

    public static final int REQUEST_CODE = 220;
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    protected static final String EXTRA_OPTION_LIST = "EXTRA_OPTION_LIST";
    protected static final String EXTRA_SEARCH_HINT = "EXTRA_SEARCH_HINT";
    protected static final String EXTRA_IS_SEARCHABLE = "EXTRA_IS_SEARCHABLE";
    protected static final String EXTRA_PAGE_TITLE = "EXTRA_PAGE_TITLE";

    protected List<Option> optionList;
    protected T adapter;
    protected RecyclerView recyclerView;

    private View searchInputContainer;
    private EditText searchInputView;
    private EmptySearchResultView searchResultEmptyView;
    private OptionSearchFilter searchFilter;
    private TextView buttonApply;
    private TextView buttonReset;
    private View buttonClose;
    private TextView topBarTitle;
    private View loadingView;

    private boolean isSearchable;
    private String searchHint;
    private String pageTitle;
    private boolean isAutoTextChange = false;
    private Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_filter_detail);
        bindView();
        showLoading();
        subscription = retrieveOptionListData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        fetchDataFromIntent();
                        initTopBar();
                        initRecyclerView();
                        loadFilterItems(optionList);
                        initSearchView();
                        initListeners();
                        hideLoading();
                    }
                });
    }

    protected Observable<Boolean> retrieveOptionListData() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                optionList = getIntent().getParcelableArrayListExtra(EXTRA_OPTION_LIST);
                subscriber.onNext(true);
            }
        });
    }

    private void fetchDataFromIntent() {
        isSearchable = getIntent().getBooleanExtra(EXTRA_IS_SEARCHABLE, false);
        searchHint = getIntent().getStringExtra(EXTRA_SEARCH_HINT);
        pageTitle = getIntent().getStringExtra(EXTRA_PAGE_TITLE);
    }

    protected void bindView() {
        recyclerView = (RecyclerView) findViewById(R.id.filter_detail_recycler_view);
        searchInputView = (EditText) findViewById(R.id.filter_detail_search);
        searchInputContainer = findViewById(R.id.filter_detail_search_container);
        searchResultEmptyView = (EmptySearchResultView) findViewById(R.id.filter_detail_empty_search_result_view);
        topBarTitle = (TextView) findViewById(R.id.top_bar_title);
        buttonClose = findViewById(R.id.top_bar_close_button);
        buttonClose.setBackgroundResource(R.drawable.ic_filter_detail_back);
        buttonReset = (TextView) findViewById(R.id.top_bar_button_reset);
        buttonApply = (TextView) findViewById(R.id.button_apply);
        loadingView = findViewById(R.id.loading_view);
    }

    private void initListeners() {
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter();
            }
        });
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });
    }

    protected void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    protected void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    private void initTopBar() {
        topBarTitle.setText(pageTitle);
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
                if (!isAutoTextChange) {
                    getSearchFilter().filter(charSequence);
                }
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

    protected void initRecyclerView() {
        adapter = getAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_line_separator));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard();
                }
            }
        });
    }

    protected abstract T getAdapter();

    @Override
    public void onItemCheckedChanged(Option option, boolean isChecked) {
        option.setInputState(Boolean.toString(isChecked));
        hideKeyboard();
    }

    private void hideKeyboard() {
        KeyboardHandler.hideSoftKeyboard(this);
    }

    protected abstract void loadFilterItems(List<Option> options);

    protected void applyFilter() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(EXTRA_RESULT, new ArrayList<>(optionList));
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void resetFilter() {
        loadFilterItems(optionList);
        clearSearchInput();
        KeyboardHandler.hideSoftKeyboard(this);
        searchResultEmptyView.setVisibility(View.GONE);
        buttonApply.setVisibility(View.VISIBLE);
    }

    private void clearSearchInput() {
        isAutoTextChange = true;
        searchInputView.setText("");
        isAutoTextChange = false;
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

            if (resultList.isEmpty()) {
                searchResultEmptyView.setSearchCategory(pageTitle);
                searchResultEmptyView.setSearchQuery(constraint.toString());
                searchResultEmptyView.setVisibility(View.VISIBLE);
                buttonApply.setVisibility(View.GONE);
            } else {
                searchResultEmptyView.setVisibility(View.GONE);
                buttonApply.setVisibility(View.VISIBLE);
            }

            loadFilterItems(resultList);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
