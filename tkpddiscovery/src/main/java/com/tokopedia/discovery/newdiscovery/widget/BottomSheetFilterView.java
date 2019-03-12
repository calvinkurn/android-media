package com.tokopedia.discovery.newdiscovery.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.discovery.model.Category;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.discovery.model.Search;
import com.tokopedia.design.keyboard.KeyboardHelper;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdynamicfilter.AbstractDynamicFilterDetailActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterCategoryActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterLocationActivity;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.BottomSheetDynamicFilterTypeFactoryImpl;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDbHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.BottomSheetDynamicFilterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.core.discovery.model.Option.KEY_CATEGORY;
import static com.tokopedia.core.discovery.model.Option.METRIC_INTERNATIONAL;

/**
 * Created by henrypriyono on 12/03/18.
 */

public class BottomSheetFilterView extends BaseCustomView implements BottomSheetDynamicFilterView {

    public static final String FILTER_CHECKED_STATE_PREF = "filter_checked_state";
    public static final String FILTER_TEXT_PREF = "filter_text";
    public static final String FILTER_SHOWN_IN_MAIN_PREF = "filter_shown_in_main";
    public static final String FILTER_SELECTED_CATEGORY_ROOT_ID_PREF = "filter_selected_category_root_id";
    public static final String FILTER_SELECTED_CATEGORY_ID_PREF = "filter_selected_category_id";
    public static final String FILTER_SELECTED_CATEGORY_NAME_PREF = "filter_selected_category_name";
    public static final String FILTER_FLAG_HELPER = "filter_flag_helper";
    public static final String FILTER_SEARCH_PARAMETER = "filter_search_parameter";

    private RecyclerView filterMainRecyclerView;
    private DynamicFilterAdapter filterMainAdapter;
    private TextView buttonReset;
    private View buttonClose;
    private TextView buttonFinish;
    private View loadingView;
    private View bottomSheetLayout;
    private UserLockBottomSheetBehavior bottomSheetBehavior;
    private View rootView;

    private Callback callback;
    @Deprecated
    private HashMap<String, Boolean> savedCheckedState = new HashMap<>();
    @Deprecated
    private HashMap<String, String> savedTextInput = new HashMap<>();
    private HashMap<String, Boolean> shownInMainState = new HashMap<>();

    private int selectedExpandableItemPosition;
    @Deprecated
    private String selectedCategoryId;
    @Deprecated
    private String selectedCategoryName;
    @Deprecated
    private String selectedCategoryRootId;
    private boolean isAutoTextChange = false;

    private int pressedSliderMinValueState = -1;
    private int pressedSliderMaxValueState = -1;

    private HashMap<String, String> searchParameter = new HashMap<>();
    private HashMap<String, Boolean> flagFilterHelper = new HashMap<>();

    public BottomSheetFilterView(@NonNull Context context) {
        super(context);
        init();
    }

    public BottomSheetFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BottomSheetFilterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    private void init() {
        rootView = inflate(getContext(), R.layout.filter_bottom_sheet, this);
        filterMainRecyclerView = rootView.findViewById(R.id.dynamic_filter_recycler_view);
        buttonClose = rootView.findViewById(R.id.top_bar_close_button);
        buttonReset = rootView.findViewById(R.id.top_bar_button_reset);
        bottomSheetLayout = this;
        buttonFinish = rootView.findViewById(R.id.button_finish);
        loadingView = rootView.findViewById(R.id.filterProgressBar);
        initKeyboardVisibilityListener();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void loadLastFilterState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            recoverLastFilterState(savedInstanceState);
        }
    }

    @SuppressWarnings("unchecked")
    private void recoverLastFilterState(Bundle savedInstanceState) {
        shownInMainState = (HashMap<String, Boolean>) savedInstanceState.getSerializable(FILTER_SHOWN_IN_MAIN_PREF);
        flagFilterHelper = (HashMap<String, Boolean>) savedInstanceState.getSerializable(FILTER_FLAG_HELPER);
        searchParameter = (HashMap<String, String>) savedInstanceState.getSerializable(FILTER_SEARCH_PARAMETER);
    }

    public void setFilterResultCount(String formattedResultCount) {
        buttonFinish.setText(String.format(getContext().getString(R.string.bottom_sheet_filter_finish_button_template_text), formattedResultCount));
        loadingView.setVisibility(View.GONE);
    }

    public void closeView() {
        if (bottomSheetBehavior != null
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN
                && buttonFinish.getVisibility() == View.VISIBLE) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void onExpandableItemClicked(Filter filter) {
        selectedExpandableItemPosition = filterMainAdapter.getItemPosition(filter);
        if (filter.isCategoryFilter()) {
            launchFilterCategoryPage(filter);
        } else {
            enrichWithInputState(filter);
            callback.launchFilterDetailPage(filter);
        }
    }

    private void launchFilterCategoryPage(Filter filter) {
        String categoryId = getFilterValue(SearchApiConst.SC);
        Category selectedCategory = FilterHelper.getSelectedCategoryDetails(filter, categoryId);
        String selectedCategoryRootId = selectedCategory != null ? selectedCategory.getCategoryRootId() : "";

        callback.launchFilterCategoryPage(filter, selectedCategoryRootId, categoryId);
    }

    private void enrichWithInputState(Filter filter) {
        for (Option option : filter.getOptions()) {
            option.setInputState(
                    String.valueOf(getFlagFilterHelperValue(option.getUniqueId()))
            );
        }
    }

    @Override
    public Boolean loadLastCheckedState(Option option) {
        return savedCheckedState.get(option.getUniqueId());
    }

    @Override
    public void saveCheckedState(Option option, Boolean isChecked) {
        saveCheckedState(option, isChecked, "");
    }

    @Override
    public void saveCheckedState(Option option, Boolean isChecked, String filterTitle) {
        SearchTracking.eventSearchResultFilterJourney(getContext(), filterTitle, option.getName(), false, isChecked);
        if (isChecked) {
            savedCheckedState.put(option.getUniqueId(), true);
        } else {
            savedCheckedState.remove(option.getUniqueId());
        }
        updateResetButtonVisibility();
        applyFilter();
    }

    private void updateResetButtonVisibility() {
        if (buttonReset != null) {
            buttonReset.setVisibility(isFilterActive() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void removeSavedTextInput(String key) {
        SearchTracking.eventSearchResultFilterJourney(getContext(), key, "", false, false);
        savedTextInput.remove(key);
        updateResetButtonVisibility();
    }

    @Override
    public void saveTextInput(String key, String textInput) {
        savedTextInput.put(key, textInput);
        SearchTracking.eventSearchResultFilterJourney(getContext(), key, textInput, false, true);
        updateResetButtonVisibility();
    }

    @Override
    public List<Option> getSelectedOptions(Filter filter) {
        List<Option> selectedOptions = new ArrayList<>();
        List<Option> popularOptionList = getPopularOptionList(filter);

        if (filter.isCategoryFilter() && isCategorySelected() && !isSelectedCategoryInList(popularOptionList)) {
            selectedOptions.add(getSelectedCategoryAsOption(filter));
        } else {
            selectedOptions.addAll(getCustomSelectedOptionList(filter));
        }

        selectedOptions.addAll(popularOptionList);
        return selectedOptions;
    }

    private boolean isSelectedCategoryInList(List<Option> optionList) {
        if (TextUtils.isEmpty(selectedCategoryId)) {
            return false;
        }
        for (Option option : optionList) {
            if (selectedCategoryId.equals(option.getValue())) {
                return true;
            }
        }
        return false;
    }

    private boolean isCategorySelected() {
        return !TextUtils.isEmpty(selectedCategoryRootId) &&
                !TextUtils.isEmpty(selectedCategoryId) &&
                !TextUtils.isEmpty(selectedCategoryName);
    }

    private Option getSelectedCategoryAsOption(Filter filter) {
        String selectedCategoryId = getFilterValue(SearchApiConst.SC);
        Category category = FilterHelper.getSelectedCategoryDetails(filter, selectedCategoryId);
        String selectedCategoryName = category != null ? category.getCategoryName() : "";

        return OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryName);
    }

    private List<Option> getPopularOptionList(Filter filter) {
        List<Option> checkedOptions = new ArrayList<>();

        for (Option option : filter.getOptions()) {
            if (option.isPopular()) {
                checkedOptions.add(option);
            }
        }
        return checkedOptions;
    }

    private List<Option> getCustomSelectedOptionList(Filter filter) {
        List<Option> checkedOptions = new ArrayList<>();

        for (Option option : filter.getOptions()) {
            boolean isDisplayed = Boolean.TRUE.equals(loadLastCheckedState(option))
                    || Boolean.TRUE.equals(shownInMainState.get(option.getUniqueId()));

            if (isDisplayed && !option.isPopular()) {
                checkedOptions.add(option);
            }
        }
        return checkedOptions;
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
        removeSelectedOption(option, "");
    }

    @Override
    public void removeSelectedOption(Option option, String filterTitle) {
        if (KEY_CATEGORY.equals(option.getKey())) {
            SearchTracking.eventSearchResultFilterJourney(getContext(), filterTitle, option.getName(), false, false);
            resetSelectedCategory();
            updateResetButtonVisibility();
            applyFilter();
        } else {
            saveCheckedState(option, false, filterTitle);
        }
    }

    private void resetAllFilter() {
        resetSelectedCategory();
        clearPriceRangeRecentValue();
        savedCheckedState.clear();
        shownInMainState.clear();
        savedTextInput.clear();
        filterMainAdapter.notifyDataSetChanged();
        updateResetButtonVisibility();
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

    private void resetSelectedCategory() {
        selectedCategoryId = null;
        selectedCategoryRootId = null;
        selectedCategoryName = null;
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

    private boolean isPriceRangeValueSameAsSliderPressedState(int minValue, int maxValue) {
        return minValue == pressedSliderMinValueState && maxValue == pressedSliderMaxValueState;
    }

    private Filter getPriceFilter() {
        List<Filter> filterList = filterMainAdapter.getFilterList();
        for (Filter filter : filterList) {
            if (filter.isPriceFilter()) return filter;
        }
        return null;
    }

    public void initFilterBottomSheet(Bundle savedInstanceState) {
        initBottomSheetListener();
        initFilterMainRecyclerView();
        loadLastFilterState(savedInstanceState);
    }

    private void initFilterMainRecyclerView() {
        DynamicFilterTypeFactory dynamicFilterTypeFactory = new BottomSheetDynamicFilterTypeFactoryImpl(this);
        filterMainAdapter = new DynamicFilterAdapter(dynamicFilterTypeFactory);
        filterMainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(filterMainRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_line_separator));
        filterMainRecyclerView.addItemDecoration(dividerItemDecoration);
        filterMainRecyclerView.setAdapter(filterMainAdapter);
        filterMainRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    callback.hideKeyboard();
                }
            }
        });
    }

    public void launchFilterBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void loadFilterItems(List<Filter> filterList, HashMap<String, String> searchParameter) {
        this.searchParameter = new HashMap<>(searchParameter);
        loadFilterData(filterList);
    }

    private void updateFilterInputData(FilterFlagSelectedModel model) {
        shownInMainState.clear();

        if (model == null) {
            return;
        }
        savedCheckedState = model.getSavedCheckedState();
        savedTextInput = model.getSavedTextInput();
        selectedCategoryId = model.getCategoryId();
        selectedCategoryName = model.getSelectedCategoryName();
        selectedCategoryRootId = model.getSelectedCategoryRootId();
        updateResetButtonVisibility();
    }

    private void loadFilterData(List<Filter> filterList) {
        List<Filter> list = new ArrayList<>();
        list.addAll(filterList);
        removeFiltersWithEmptyOption(list);
        mergeSizeFilterOptionsWithSameValue(list);
        removeBrandFilterOptionsWithSameValue(list);
        filterMainAdapter.setFilterList(list);
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

    private Filter getRatingFilter(List<Filter> filterList) {
        for (Filter filter : filterList) {
            if (filter.isRatingFilter()) return filter;
        }
        return null;
    }

    private Option createFourAndAboveRatingOption() {
        Option option = new Option();
        option.setPopular(true);
        option.setInputState(Boolean.toString(false));
        option.setIconUrl("");
        option.setInputType(Option.INPUT_TYPE_CHECKBOX);
        option.setKey(Option.KEY_RATING);
        option.setName(Option.RATING_ABOVE_FOUR_NAME);
        option.setValue(Option.RATING_ABOVE_FOUR_VALUE);
        return option;
    }

    private Filter generateOthersFilter(List<Option> othersOptionList) {
        Filter filter = new Filter();
        makeAllOptionPopular(othersOptionList);
        filter.setOptions(othersOptionList);
        filter.setTitle(getContext().getString(R.string.other_filter_title));
        filter.setTemplateName(Filter.TEMPLATE_NAME_OTHER);

        Search search = new Search();
        search.setPlaceholder("");
        search.setSearchable(0);
        filter.setSearch(search);

        return filter;
    }

    private void makeAllOptionPopular(List<Option> list) {
        for (Option option : list) {
            option.setPopular(true);
        }
    }

    private void initBottomSheetListener() {
        bottomSheetBehavior = (UserLockBottomSheetBehavior) UserLockBottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN && !callback.isSearchShown()) {
                    callback.onHide();
                } else {
                    callback.onShow();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        buttonClose.setOnClickListener(v -> closeView());
        buttonFinish.setOnClickListener(view -> closeView());
        buttonReset.setOnClickListener(v -> {
            if (isFilterActive()) {
                resetAllFilter();
                applyFilter();
            }
        });
    }

    private boolean isFilterActive() {
        return !savedCheckedState.isEmpty() || !savedTextInput.isEmpty() || isCategorySelected();
    }

    @Override
    public void onPriceSliderRelease(int minValue, int maxValue) {
        if (!isPriceRangeValueSameAsSliderPressedState(minValue, maxValue)) {
            applyFilter();
        }
    }

    @Override
    public void onPriceSliderPressed(int minValue, int maxValue) {
        pressedSliderMinValueState = minValue;
        pressedSliderMaxValueState = maxValue;
    }

    @Override
    public void onPriceEditedFromTextInput(int minValue, int maxValue) {
        applyFilter();
    }

    @Override
    public boolean isSelectedCategory(Option option) {
        return !TextUtils.isEmpty(selectedCategoryId)
                &&  selectedCategoryId.equals(option.getValue());
    }

    @Override
    public void selectCategory(Option option, String filterTitle) {
        SearchTracking.eventSearchResultFilterJourney(getContext(), filterTitle, option.getName(), false, true);
        FilterFlagSelectedModel filterFlagSelectedModel = new FilterFlagSelectedModel();
        FilterHelper.populateWithSelectedCategory(filterMainAdapter.getFilterList(), filterFlagSelectedModel, option.getValue());
        selectedCategoryId = filterFlagSelectedModel.getCategoryId();
        selectedCategoryName = filterFlagSelectedModel.getSelectedCategoryName();
        selectedCategoryRootId = filterFlagSelectedModel.getSelectedCategoryRootId();
        updateResetButtonVisibility();
        applyFilter();
    }

    public boolean isBottomSheetShown() {
        return bottomSheetBehavior != null
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN;
    }

    public boolean onBackPressed() {
        if (isBottomSheetShown()) {
            closeView();
            return true;
        } else {
            return false;
        }
    }

    private void applyFilter() {
        loadingView.setVisibility(View.VISIBLE);
        buttonFinish.setText("");
        applyFlagFilterOnSearchParameter();
        callback.onApplyFilter(searchParameter);
    }

    private void applyFlagFilterOnSearchParameter() {
        for (Map.Entry<String, Boolean> entry : flagFilterHelper.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                appendToMap(entry.getKey());
            }
        }
    }

    private void appendToMap(String uniqueId) {
        String key = OptionHelper.parseKeyFromUniqueId(uniqueId);
        String value = OptionHelper.parseValueFromUniqueId(uniqueId);
        String appendedMapValue = appendMapValue(key, value);

        searchParameter.put(key, appendedMapValue);
    }

    private String appendMapValue(String key, String previousValue) {
        String value = searchParameter.get(key);

        if (TextUtils.isEmpty(value)) value = previousValue;
        else value += "," + previousValue;

        return value;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AbstractDynamicFilterDetailActivity.REQUEST_CODE:
                    handleResultFromDetailPage(data);
                    filterMainAdapter.notifyItemChanged(selectedExpandableItemPosition);
                    applyFilter();
                    break;
                case DynamicFilterLocationActivity.REQUEST_CODE:
                    handleResultFromLocationPage();
                    break;
                case DynamicFilterCategoryActivity.REQUEST_CODE:
                    handleResultFromCategoryPage(data);
                    filterMainAdapter.notifyItemChanged(selectedExpandableItemPosition);
                    applyFilter();
                    break;
            }
            updateResetButtonVisibility();
        }
    }

    private void handleResultFromDetailPage(Intent data) {
        List<Option> optionList
                = data.getParcelableArrayListExtra(AbstractDynamicFilterDetailActivity.EXTRA_RESULT);
        for (Option option : optionList) {
            OptionHelper.saveOptionInputState(option, flagFilterHelper);
            OptionHelper.saveOptionShownInMainState(option, shownInMainState);
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
                            OptionHelper.saveOptionInputState(option, flagFilterHelper);
                            OptionHelper.saveOptionShownInMainState(option, shownInMainState);
                        }
                        filterMainAdapter.notifyItemChanged(selectedExpandableItemPosition);
                        applyFilter();
                    }
                });
    }

    private void handleResultFromCategoryPage(Intent data) {
        String selectedCategoryId = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_ID);
        searchParameter.put(SearchApiConst.SC, selectedCategoryId);
    }

    private void initKeyboardVisibilityListener() {
        KeyboardHelper.setKeyboardVisibilityChangedListener(bottomSheetLayout, new KeyboardHelper.OnKeyboardVisibilityChangedListener() {
            @Override
            public void onKeyboardShown() {
                if (bottomSheetBehavior != null
                        && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    buttonFinish.setVisibility(View.GONE);
                }
            }

            @Override
            public void onKeyboardHide() {
                if (bottomSheetBehavior != null
                        && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    buttonFinish.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(FILTER_SHOWN_IN_MAIN_PREF, shownInMainState);
        outState.putSerializable(FILTER_FLAG_HELPER, flagFilterHelper);
        outState.putSerializable(FILTER_SEARCH_PARAMETER, searchParameter);
    }

    @Override
    public void setFlagFilterHelper(Option option, boolean value, boolean isAppliedImmediately) {
        saveOrRemoveFlagFilterHelper(option.getUniqueId(), value);

        updateViewAfterFilter(isAppliedImmediately);
    }

    public void saveOrRemoveFlagFilterHelper(String key, boolean value) {
        if(value) flagFilterHelper.put(key, true);
        else flagFilterHelper.remove(key);
    }

    @Override
    public void setFilterValue(Option option, boolean value, boolean isAppliedImmediately) {
        SearchTracking.eventSearchResultFilterJourney(getContext(), "", option.getName(), false, value);

        saveOrRemoveBooleanFromSearchParameter(option.getKey(), value);

        updateViewAfterFilter(isAppliedImmediately);
    }

    private void saveOrRemoveBooleanFromSearchParameter(String key, Boolean value) {
        if (value) searchParameter.put(key, value.toString());
        else searchParameter.remove(key);
    }

    @Override
    public void setFilterValue(Option option, String value, boolean isAppliedImmediately) {
        SearchTracking.eventSearchResultFilterJourney(getContext(), option.getKey(), value, false, !TextUtils.isEmpty(value));

        saveOrRemoveStringFromSearchParameter(option.getKey(), value);

        updateViewAfterFilter(isAppliedImmediately);
    }

    private void saveOrRemoveStringFromSearchParameter(String key, String value) {
        if(!TextUtils.isEmpty(value)) searchParameter.put(key, value);
        else searchParameter.remove(key);
    }

    private void updateViewAfterFilter(boolean isAppliedImmediately) {
        updateResetButtonVisibility();

        if(isAppliedImmediately) applyFilter();
    }

    @Override
    public String getFilterValue(String key) {
        if(searchParameter.containsKey(key)) return searchParameter.get(key);

        return "";
    }

    @Override
    public boolean getFlagFilterHelperValue(String key) {
        if(flagFilterHelper.containsKey(key)) {
            Boolean returnValue = flagFilterHelper.get(key);
            if(returnValue != null) return returnValue;
        }

        return false;
    }

    public interface Callback {
        void onApplyFilter(HashMap<String, String> searchParameter);
        void onShow();
        void onHide();
        boolean isSearchShown();
        void hideKeyboard();

        void launchFilterCategoryPage(Filter filter, String selectedCategoryRootId, String selectedCategoryId);
        void launchFilterDetailPage(Filter filter);
    }
}
