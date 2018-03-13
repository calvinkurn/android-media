package com.tokopedia.discovery.newdiscovery.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.list.widget.AlphabeticalSideBar;
import com.tokopedia.design.search.EmptySearchResultView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterDetailAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactoryImpl;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterDetailView;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.tokopedia.core.discovery.model.Option.KEY_CATEGORY;
import static com.tokopedia.core.discovery.model.Option.METRIC_INTERNATIONAL;

/**
 * Created by henrypriyono on 12/03/18.
 */

public class BottomSheetFilterView extends BaseCustomView implements DynamicFilterView, DynamicFilterDetailView {

    public static final String EXTRA_SELECTED_FILTERS = "EXTRA_SELECTED_FILTERS";
    public static final String EXTRA_FILTER_LIST = "EXTRA_FILTER_LIST";

    public static final String FILTER_CHECKED_STATE_PREF = "filter_checked_state";
    public static final String FILTER_TEXT_PREF = "filter_text";
    public static final String FILTER_SELECTED_CATEGORY_ROOT_ID_PREF = "filter_selected_category_root_id";
    public static final String FILTER_SELECTED_CATEGORY_ID_PREF = "filter_selected_category_id";
    public static final String FILTER_SELECTED_CATEGORY_NAME_PREF = "filter_selected_category_name";

    private LinearLayout bottomSheetFilterMain;
    private LinearLayout bottomSheetFilterDetail;
    private ImageButton filterDetailTopBarCloseButton;
    private TextView filterDetailTopBarTitle;
    private TextView filterDetailTopBarButtonReset;
    private FrameLayout filterDetailSearchContainer;
    private EditText filterDetailSearch;
    private RecyclerView filterDetailRecyclerView;
    private AlphabeticalSideBar filterDetailSidebar;
    private EmptySearchResultView filterDetailEmptySearchResultView;
    private TextView filterResultCountText;
    private RecyclerView filterMainRecyclerView;
    private DynamicFilterAdapter filterMainAdapter;
    private DynamicFilterDetailAdapter filterDetailAdapter;
    private OptionSearchFilter searchFilter;
    private TextView buttonReset;
    private View buttonClose;
    private View bottomSheetLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private View rootView;

    private Callback callback;

    private TextWatcher filterDetailSearchTextWatcher;
    private boolean isPriceValueChangedSinceButtonPressed = false;
    private List<Option> activeFilterDetailOptionList;
    private HashMap<String, Boolean> savedCheckedState = new HashMap<>();
    private HashMap<String, String> savedTextInput = new HashMap<>();

    private int selectedExpandableItemPosition;
    private String selectedCategoryId;
    private String selectedCategoryName;
    private String selectedCategoryRootId;
    private boolean isAutoTextChange = false;

    private int pressedSliderMinValueState = -1;
    private int pressedSliderMaxValueState = -1;

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
        filterMainRecyclerView = (RecyclerView) rootView.findViewById(R.id.dynamic_filter_recycler_view);
        buttonClose = rootView.findViewById(R.id.top_bar_close_button);
        buttonReset = (TextView) rootView.findViewById(R.id.top_bar_button_reset);
        bottomSheetLayout = this;
        bottomSheetFilterMain = (LinearLayout) rootView.findViewById( R.id.bottom_sheet_filter_main );
        bottomSheetFilterDetail = (LinearLayout) rootView.findViewById( R.id.bottom_sheet_filter_detail );
        filterDetailTopBarCloseButton = (ImageButton) rootView.findViewById( R.id.filter_detail_top_bar_close_button );
        filterDetailTopBarTitle = (TextView) rootView.findViewById( R.id.filter_detail_top_bar_title );
        filterDetailTopBarButtonReset = (TextView) rootView.findViewById( R.id.filter_detail_top_bar_button_reset );
        filterDetailSearchContainer = (FrameLayout) rootView.findViewById( R.id.filter_detail_search_container );
        filterDetailSearch = (EditText) rootView.findViewById( R.id.filter_detail_search );
        filterDetailRecyclerView = (RecyclerView) rootView.findViewById( R.id.filter_detail_recycler_view );
        filterDetailSidebar = (AlphabeticalSideBar) rootView.findViewById( R.id.filter_detail_sidebar );
        filterDetailEmptySearchResultView = (EmptySearchResultView) rootView.findViewById( R.id.filter_detail_empty_search_result_view );
        filterResultCountText = rootView.findViewById(R.id.filter_result_count);
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
        savedCheckedState = (HashMap<String, Boolean>) savedInstanceState.getSerializable(FILTER_CHECKED_STATE_PREF);
        savedTextInput = (HashMap<String, String>) savedInstanceState.getSerializable(FILTER_TEXT_PREF);
        selectedCategoryId = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_ID_PREF);
        selectedCategoryName = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_NAME_PREF);
        selectedCategoryRootId = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_ROOT_ID_PREF);
    }

    public void setFilterResultCount(String formattedResultCount) {
        filterResultCountText.setText(String.format(getContext().getString(R.string.result_count_template_text), formattedResultCount));
    }

    public void closeView() {
        if (bottomSheetBehavior != null
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void onExpandableItemClicked(Filter filter) {
        activeFilterDetailOptionList = filter.getOptions();
        enrichWithInputState(filter);
        initFilterDetailTopBar(filter);
        initFilterDetailSearchView(filter);
        initFilterDetailRecyclerView();
        loadFilterDetailItems(activeFilterDetailOptionList);
        showFilterDetailPage();
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
        applyFilter();
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

    private void resetFilterDetail() {
        resetAllOptionState();
        loadFilterDetailItems(activeFilterDetailOptionList);
        clearSearchInput();
        callback.hideKeyboard();
    }

    private void resetAllOptionState() {
        for (Option option : activeFilterDetailOptionList) {
            option.setInputState("");
            OptionHelper.saveOptionInputState(option, savedCheckedState, savedTextInput);
        }
    }

    private void clearSearchInput() {
        isAutoTextChange = true;
        filterDetailSearch.setText("");
        isAutoTextChange = false;
        filterDetailEmptySearchResultView.setVisibility(View.GONE);
    }

    private void resetAllFilter() {
        resetSelectedCategory();
        clearPriceRangeRecentValue();
        savedCheckedState.clear();
        savedTextInput.clear();
        filterMainAdapter.notifyDataSetChanged();
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
        initFilterMainRecyclerView();
        loadLastFilterState(savedInstanceState);
    }

    private void initFilterMainRecyclerView() {
        DynamicFilterTypeFactory dynamicFilterTypeFactory = new DynamicFilterTypeFactoryImpl(this);
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
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void loadFilterItems(List<Filter> filterList) {
        List<Filter> list = new ArrayList<>();
        list.addAll(filterList);
        removeCategoryFilter(list);
        removeFiltersWithEmptyOption(list);
        mergeSizeFilterOptionsWithSameValue(list);
        removeBrandFilterOptionsWithSameValue(list);
        filterMainAdapter.setFilterList(list);
    }

    private void removeCategoryFilter(List<Filter> filterList) {
        Iterator<Filter> iterator = filterList.iterator();
        while (iterator.hasNext()) {
            Filter filter = iterator.next();
            if (filter.isCategoryFilter()) {
                iterator.remove();
                break;
            }
        }
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

    @Override
    public void onItemCheckedChanged(Option option, boolean isChecked) {
        option.setInputState(Boolean.toString(isChecked));
        OptionHelper.saveOptionInputState(option, savedCheckedState, savedTextInput);
        callback.hideKeyboard();
    }

    private void initBottomSheetListener() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
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
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeView();
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllFilter();
                applyFilter();
            }
        });
        filterDetailTopBarButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFilterDetail();
            }
        });
        filterDetailTopBarCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFilterDetailPage();
                applyFilter();
            }
        });
    }

    private void showFilterDetailPage() {
        bottomSheetFilterDetail.setVisibility(View.VISIBLE);
        bottomSheetFilterMain.setVisibility(View.INVISIBLE);
    }

    private boolean isFilterDetailShown() {
        return isBottomSheetShown() && bottomSheetFilterDetail.getVisibility() == View.VISIBLE;
    }

    private void initFilterDetailTopBar(Filter filter) {
        filterDetailTopBarTitle.setText(filter.getTitle());
    }

    private void initFilterDetailSearchView(final Filter filter) {
        boolean isSearchable = filter.getSearch().getSearchable() == 1;
        if (!isSearchable) {
            filterDetailSearchContainer.setVisibility(View.GONE);
            return;
        }
        filterDetailSearchContainer.setVisibility(View.VISIBLE);
        searchFilter = null;

        if (filterDetailSearchTextWatcher != null) {
            filterDetailSearch.removeTextChangedListener(filterDetailSearchTextWatcher);
            filterDetailSearchTextWatcher = null;
        }

        clearSearchInput();

        filterDetailSearchTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isAutoTextChange) {
                    getSearchFilter(filter).filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        filterDetailSearch.addTextChangedListener(filterDetailSearchTextWatcher);
        filterDetailSearch.setHint(filter.getSearch().getPlaceholder());
    }

    private OptionSearchFilter getSearchFilter(Filter filter) {
        if (searchFilter == null) {
            searchFilter = new OptionSearchFilter(filter.getOptions());
        }
        return searchFilter;
    }

    private void initFilterDetailRecyclerView() {
        filterDetailAdapter = new DynamicFilterDetailAdapter(this);
        filterDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(filterDetailRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_line_separator));
        filterDetailRecyclerView.addItemDecoration(dividerItemDecoration);
        filterDetailRecyclerView.setAdapter(filterDetailAdapter);
        filterDetailRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    callback.hideKeyboard();
                }
            }
        });
    }

    private void hideFilterDetailPage() {
        bottomSheetFilterMain.setVisibility(View.VISIBLE);
        bottomSheetFilterDetail.setVisibility(View.INVISIBLE);
        refreshFilterMainPage();
    }

    private void refreshFilterMainPage() {
        filterMainAdapter.notifyDataSetChanged();
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

    private void loadFilterDetailItems(List<Option> resultList) {
        filterDetailAdapter.setOptionList(resultList);
    }

    public boolean isBottomSheetShown() {
        return bottomSheetBehavior != null
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN;
    }

    public boolean onBackPressed() {
        if (isFilterDetailShown()) {
            hideFilterDetailPage();
            applyFilter();
            return true;
        } else if (isBottomSheetShown()) {
            closeView();
            return true;
        } else {
            return false;
        }
    }

    private void applyFilter() {
        HashMap<String, String> selectedFilter = generateSelectedFilterMap();
        callback.onApplyFilter(selectedFilter);
    }

    public interface Callback {
        void onApplyFilter(HashMap<String, String> selectedFilter);
        void onShow();
        void onHide();
        boolean isSearchShown();
        void hideKeyboard();
    }

    private class OptionSearchFilter extends android.widget.Filter {
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
                filterDetailEmptySearchResultView.setSearchCategory(filterDetailTopBarTitle.getText().toString());
                filterDetailEmptySearchResultView.setSearchQuery(constraint.toString());
                filterDetailEmptySearchResultView.setVisibility(View.VISIBLE);
            } else {
                filterDetailEmptySearchResultView.setVisibility(View.GONE);
            }

            loadFilterDetailItems(resultList);
        }
    }
}
