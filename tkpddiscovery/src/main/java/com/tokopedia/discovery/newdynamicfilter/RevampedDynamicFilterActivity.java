package com.tokopedia.discovery.newdynamicfilter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.ToastNetworkHandler;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.helper.KeyboardHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactoryImpl;
import com.tokopedia.discovery.newdynamicfilter.helper.DynamicFilterDbManager;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDbHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDetailActivityRouter;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.discovery.model.Option.KEY_CATEGORY;
import static com.tokopedia.core.discovery.model.Option.METRIC_INTERNATIONAL;

/**
 * Created by henrypriyono on 8/8/17.
 */

public class RevampedDynamicFilterActivity extends BaseActivity implements DynamicFilterView {

    public static final int REQUEST_CODE = 219;
    public static final String EXTRA_SELECTED_FILTERS = "EXTRA_SELECTED_FILTERS";
    public static final String EXTRA_FILTER_LIST = "EXTRA_FILTER_LIST";
    public static final String EXTRA_SELECTED_FLAG_FILTER = "EXTRA_SELECTED_FLAG_FILTER";

    public static final String FILTER_CHECKED_STATE_PREF = "filter_checked_state";
    public static final String FILTER_TEXT_PREF = "filter_text";
    public static final String FILTER_SELECTED_CATEGORY_ROOT_ID_PREF = "filter_selected_category_root_id";
    public static final String FILTER_SELECTED_CATEGORY_ID_PREF = "filter_selected_category_id";
    public static final String FILTER_SELECTED_CATEGORY_NAME_PREF = "filter_selected_category_name";

    private static final String TAG = RevampedDynamicFilterActivity.class.getSimpleName();

    RecyclerView recyclerView;
    DynamicFilterAdapter adapter;
    TextView buttonApply;
    TextView buttonReset;
    View buttonClose;
    View mainLayout;
    View loadingView;

    HashMap<String, Boolean> savedCheckedState = new HashMap<>();
    HashMap<String, String> savedTextInput = new HashMap<>();

    private int selectedExpandableItemPosition;
    private String selectedCategoryId;
    private String selectedCategoryName;
    private String selectedCategoryRootId;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Deprecated
    public static void moveTo(AppCompatActivity activity,
                              List<Filter> filterCategoryList) {
        if (activity != null) {
            Intent intent = new Intent(activity, RevampedDynamicFilterActivity.class);
            intent.putParcelableArrayListExtra(EXTRA_FILTER_LIST, new ArrayList<>(filterCategoryList));
            activity.startActivityForResult(intent, REQUEST_CODE);
            activity.overridePendingTransition(com.tokopedia.core.R.anim.pull_up, android.R.anim.fade_out);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revamped_dynamic_filter);
        bindView();
        initKeyboardVisibilityListener();
        initRecyclerView();
        loadLastFilterState(savedInstanceState);
        loadFilterItems();
    }

    private void bindView() {
        recyclerView = (RecyclerView) findViewById(R.id.dynamic_filter_recycler_view);
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
                resetAllFilter();
            }
        });
        buttonApply = (TextView) findViewById(R.id.button_finish);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });
        mainLayout = findViewById(R.id.main_layout);
        loadingView = findViewById(R.id.loading_view);
    }

    private void initKeyboardVisibilityListener() {
        KeyboardHelper.setKeyboardVisibilityChangedListener(mainLayout, new KeyboardHelper.OnKeyboardVisibilityChangedListener() {
            @Override
            public void onKeyboardShown() {
                buttonApply.setVisibility(View.GONE);
            }

            @Override
            public void onKeyboardHide() {
                buttonApply.setVisibility(View.VISIBLE);
                mainLayout.requestFocus();
            }
        });
    }

    private void initRecyclerView() {
        DynamicFilterTypeFactory dynamicFilterTypeFactory = new DynamicFilterTypeFactoryImpl(this);
        adapter = new DynamicFilterAdapter(dynamicFilterTypeFactory);
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
                    KeyboardHandler.hideSoftKeyboard(RevampedDynamicFilterActivity.this);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void loadFilterItems() {
        compositeSubscription.add(
                Observable.just(new DynamicFilterDbManager())
                        .map(new Func1<DynamicFilterDbManager, List<Filter>>() {
                            @Override
                            public List<Filter> call(DynamicFilterDbManager manager) {
                                String data = manager.getValueString(getIntent().getStringExtra(EXTRA_FILTER_LIST));
                                if (data == null) {
                                    throw new RuntimeException("error get filter cache");
                                } else {
                                    Type listType = new TypeToken<List<Filter>>() {}.getType();
                                    Gson gson = new Gson();
                                    return gson.fromJson(data, listType);
                                }
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<Filter>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {
                                throwable.printStackTrace();
                                ToastNetworkHandler.showToast(RevampedDynamicFilterActivity.this, getString(R.string.error_get_local_dynamic_filter));
                                finish();
                            }

                            @Override
                            public void onNext(List<Filter> list) {
                                removeFiltersWithEmptyOption(list);
                                mergeSizeFilterOptionsWithSameValue(list);
                                removeBrandFilterOptionsWithSameValue(list);
                                adapter.setFilterList(list);
                            }
                        })
        );
    }

    private void removeFiltersWithEmptyOption(List<Filter> filterList) {
        Iterator<Filter> iterator = filterList.iterator();
        while (iterator.hasNext()) {
            Filter filter = iterator.next();
            if (filter.getOptions().isEmpty() && !filter.isSeparator()) {
                iterator.remove();
            }
        }
    }

    private void mergeSizeFilterOptionsWithSameValue(List<Filter> filterList) {
        Filter sizeFilter = getSizeFilter(filterList);
        if (sizeFilter == null) {
            return;
        }

        List<Option> sizeFilterOptions = sizeFilter.getOptions();
        Iterator<Option> iterator = sizeFilterOptions.iterator();
        Map<String, Option> optionMap = new HashMap<>();

        while (iterator.hasNext()) {
            Option option = iterator.next();
            Option existingOption = optionMap.get(option.getValue());
            if (existingOption != null) {
                existingOption.setName(existingOption.getName() + " / " + getFormattedSizeName(option));
                iterator.remove();
            } else {
                option.setName(getFormattedSizeName(option));
                option.setMetric("");
                optionMap.put(option.getValue(), option);
            }
        }
    }

    private Filter getSizeFilter(List<Filter> filterList) {
        for (Filter filter : filterList) {
            if (filter.isSizeFilter()) return filter;
        }
        return null;
    }

    private void removeBrandFilterOptionsWithSameValue(List<Filter> filterList) {
        Filter brandFilter = getBrandFilter(filterList);
        if (brandFilter == null) {
            return;
        }

        List<Option> brandFilterOptions = brandFilter.getOptions();
        Iterator<Option> iterator = brandFilterOptions.iterator();
        Map<String, Option> optionMap = new HashMap<>();

        while (iterator.hasNext()) {
            Option option = iterator.next();
            Option existingOption = optionMap.get(option.getValue());
            if (existingOption != null) {
                iterator.remove();
            } else {
                optionMap.put(option.getValue(), option);
            }
        }
    }

    private Filter getBrandFilter(List<Filter> filterList) {
        for (Filter filter : filterList) {
            if (filter.isBrandFilter()) return filter;
        }
        return null;
    }

    private String getFormattedSizeName(Option option) {
        if (METRIC_INTERNATIONAL.equals(option.getMetric())) {
            return option.getName();
        } else {
            return option.getName() + " " + option.getMetric();
        }
    }

    private void loadLastFilterState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            recoverLastFilterState(savedInstanceState);
        } else {
            loadLastFilterStateFromPreference();
        }
    }

    @SuppressWarnings("unchecked")
    private void recoverLastFilterState(Bundle savedInstanceState) {
        savedCheckedState = (HashMap<String, Boolean>) savedInstanceState.getSerializable(FILTER_CHECKED_STATE_PREF);
        savedTextInput = (HashMap<String, String>) savedInstanceState.getSerializable(FILTER_TEXT_PREF);
        selectedCategoryId = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_ID_PREF);
        selectedCategoryName = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_NAME_PREF);
        selectedCategoryRootId = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_ROOT_ID_PREF);
    }

    private void loadLastFilterStateFromPreference() {
        FilterFlagSelectedModel model = getIntent().getParcelableExtra(EXTRA_SELECTED_FLAG_FILTER);
        if (model != null) {
            savedCheckedState = model.getSavedCheckedState();
            savedTextInput = model.getSavedTextInput();
            selectedCategoryId = model.getCategoryId();
            selectedCategoryName = model.getSelectedCategoryName();
            selectedCategoryRootId = model.getSelectedCategoryRootId();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, com.tokopedia.core.R.anim.push_down);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FILTER_CHECKED_STATE_PREF, savedCheckedState);
        outState.putSerializable(FILTER_TEXT_PREF, savedTextInput);
        outState.putString(FILTER_SELECTED_CATEGORY_ID_PREF, selectedCategoryId);
        outState.putString(FILTER_SELECTED_CATEGORY_ROOT_ID_PREF, selectedCategoryRootId);
        outState.putString(FILTER_SELECTED_CATEGORY_NAME_PREF, selectedCategoryName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AbstractDynamicFilterDetailActivity.REQUEST_CODE:
                    handleResultFromDetailPage(data);
                    break;
                case DynamicFilterLocationActivity.REQUEST_CODE:
                    handleResultFromLocationPage();
                    break;
                case DynamicFilterCategoryActivity.REQUEST_CODE:
                    handleResultFromCategoryPage(data);
                    break;
            }
            adapter.notifyItemChanged(selectedExpandableItemPosition);
        }
        hideLoading();
    }

    private void handleResultFromDetailPage(Intent data) {
        List<Option> optionList
                = data.getParcelableArrayListExtra(AbstractDynamicFilterDetailActivity.EXTRA_RESULT);
        for (Option option : optionList) {
            OptionHelper.saveOptionInputState(option, savedCheckedState, savedTextInput);
        }
    }

    private void handleResultFromLocationPage() {
        Observable.create(new Observable.OnSubscribe<List<Option>>() {
            @Override
            public void call(Subscriber<? super List<Option>> subscriber) {
                subscriber.onNext(FilterDbHelper.loadLocationFilterOptions());
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Option>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Option> optionList) {
                for (Option option : optionList) {
                    OptionHelper.saveOptionInputState(option, savedCheckedState, savedTextInput);
                }
                adapter.notifyItemChanged(selectedExpandableItemPosition);
                hideLoading();
            }
        });
    }

    private void handleResultFromCategoryPage(Intent data) {
        selectedCategoryId
                = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_ID);
        selectedCategoryRootId
                = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_ROOT_ID);
        selectedCategoryName
                = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_NAME);
    }

    @Override
    public void onPriceSliderRelease() {

    }

    private void applyFilter() {
        renderFilterResult();
        finish();
    }

    private void renderFilterResult() {
        HashMap<String, String> selectedFilter = generateSelectedFilterMap();

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_FLAG_FILTER, getFilterFlagSelected());
        intent.putExtra(EXTRA_SELECTED_FILTERS, selectedFilter);
        setResult(RESULT_OK, intent);
    }

    private FilterFlagSelectedModel getFilterFlagSelected() {
        FilterFlagSelectedModel model = new FilterFlagSelectedModel();
        model.setSavedCheckedState(savedCheckedState);
        model.setSavedTextInput(savedTextInput);
        model.setCategoryId(selectedCategoryId);
        model.setSelectedCategoryRootId(selectedCategoryRootId);
        model.setSelectedCategoryName(selectedCategoryName);
        return model;
    }

    private HashMap<String, String> generateSelectedFilterMap() {
        HashMap<String, String> selectedFilterMap = new HashMap<>();

        if (!TextUtils.isEmpty(selectedCategoryId)) {
            selectedFilterMap.put(KEY_CATEGORY, selectedCategoryId);
        }

        selectedFilterMap.putAll(savedTextInput);

        for (Map.Entry<String, Boolean> entry : savedCheckedState.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                appendToMap(selectedFilterMap, entry.getKey());
            }
        }

        return selectedFilterMap;
    }

    private void appendToMap(HashMap<String, String> selectedFilterMap, String uniqueId) {
        String checkBoxKey = OptionHelper.parseKeyFromUniqueId(uniqueId);
        String checkBoxValue = OptionHelper.parseValueFromUniqueId(uniqueId);
        String mapValue = selectedFilterMap.get(checkBoxKey);
        if (TextUtils.isEmpty(mapValue)) {
            mapValue = checkBoxValue;
        } else {
            mapValue += "," + checkBoxValue;
        }
        selectedFilterMap.put(checkBoxKey, mapValue);
    }

    public void resetAllFilter() {
        resetSelectedCategory();
        clearPriceRangeRecentValue();
        savedCheckedState.clear();
        savedTextInput.clear();
        adapter.notifyDataSetChanged();
    }

    private void resetSelectedCategory() {
        selectedCategoryId = null;
        selectedCategoryRootId = null;
        selectedCategoryName = null;
    }

    private void clearPriceRangeRecentValue() {
        Filter priceFilter = getPriceFilter();
        if (priceFilter == null) {
            return;
        }

        for (Option option : priceFilter.getOptions()) {
            if(Option.KEY_PRICE_MIN.equals(option.getKey())
                    || Option.KEY_PRICE_MAX.equals(option.getKey())) {
                option.setValue("");
            }
        }
    }

    private Filter getPriceFilter() {
        List<Filter> filterList = adapter.getFilterList();
        for (Filter filter : filterList) {
            if (filter.isPriceFilter()) return filter;
        }
        return null;
    }

    @Override
    public void onExpandableItemClicked(Filter filter) {
        showLoading();
        selectedExpandableItemPosition = adapter.getItemPosition(filter);
        if (filter.isCategoryFilter()) {
            FilterDetailActivityRouter
                    .launchCategoryActivity(this, filter, selectedCategoryRootId, selectedCategoryId);
        } else {
            enrichWithInputState(filter);
            FilterDetailActivityRouter.launchDetailActivity(this, filter);
        }
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    private void enrichWithInputState(Filter filter) {
        for (Option option : filter.getOptions()) {
            option.setInputState(
                    OptionHelper.loadOptionInputState(option, savedCheckedState, savedTextInput)
            );
        }
    }

    @Override
    public Boolean loadLastCheckedState(Option option) {
        return savedCheckedState.get(option.getUniqueId());
    }

    @Override
    public void saveCheckedState(Option option, Boolean isChecked) {
        savedCheckedState.put(option.getUniqueId(), isChecked);
    }

    @Override
    public String removeSavedTextInput(String key) {
        return savedTextInput.remove(key);
    }

    @Override
    public void saveTextInput(String key, String textInput) {
        savedTextInput.put(key, textInput);
    }

    @Override
    public List<Option> getSelectedOptions(Filter filter) {
        List<Option> selectedOptions = new ArrayList<>();

        if (filter.isCategoryFilter() && isCategorySelected()) {
            selectedOptions.add(getSelectedCategoryAsOption());
        } else {
            selectedOptions.addAll(getCheckedOptionList(filter));
        }
        return selectedOptions;
    }

    private boolean isCategorySelected() {
        return !TextUtils.isEmpty(selectedCategoryRootId) &&
                !TextUtils.isEmpty(selectedCategoryId) &&
                !TextUtils.isEmpty(selectedCategoryName);
    }

    private Option getSelectedCategoryAsOption() {
        return OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryName);
    }

    private List<Option> getCheckedOptionList(Filter filter) {
        List<Option> checkedOptions = new ArrayList<>();

        for (Option option : filter.getOptions()) {
            if (Boolean.TRUE.equals(loadLastCheckedState(option))) {
                checkedOptions.add(option);
            }
        }
        return checkedOptions;
    }

    @Override
    public void removeSelectedOption(Option option) {
        if (KEY_CATEGORY.equals(option.getKey())) {
            resetSelectedCategory();
        } else {
            saveCheckedState(option, false);
        }
    }

    @Override
    public void updateLastRangeValue(int minValue, int maxValue) {
        Filter priceFilter = getPriceFilter();
        if (priceFilter == null) {
            return;
        }

        for (Option option : priceFilter.getOptions()) {
            if(Option.KEY_PRICE_MIN.equals(option.getKey())) {
                option.setValue(String.valueOf(minValue));
            } else if(Option.KEY_PRICE_MAX.equals(option.getKey())) {
                option.setValue(String.valueOf(maxValue));
            }
        }
    }

    public static Intent createInstance(Context context, String filterID, FilterFlagSelectedModel model) {
        Intent intent = new Intent(context, RevampedDynamicFilterActivity.class);
        intent.putExtra(EXTRA_FILTER_LIST, filterID);
        if (model != null) {
            intent.putExtra(EXTRA_SELECTED_FLAG_FILTER, model);
        }
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
