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
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.keyboard.KeyboardHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.model.Category;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdynamicfilter.AbstractDynamicFilterDetailActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterCategoryActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterLocationActivity;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.BottomSheetDynamicFilterTypeFactoryImpl;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDbHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.BottomSheetDynamicFilterView;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.core.discovery.model.Option.KEY_CATEGORY;

/**
 * Created by henrypriyono on 12/03/18.
 */

public class BottomSheetFilterView extends BaseCustomView implements BottomSheetDynamicFilterView {

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

    private int selectedExpandableItemPosition;

    private FilterController filterController;

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
        String categoryId = filterController.getFilterValue(SearchApiConst.SC);
        Category selectedCategory = FilterHelper.getSelectedCategoryDetails(filter, categoryId);
        String selectedCategoryRootId = selectedCategory != null ? selectedCategory.getCategoryRootId() : "";

        callback.launchFilterCategoryPage(filter, selectedCategoryRootId, categoryId);
    }

    private void enrichWithInputState(Filter filter) {
        for (Option option : filter.getOptions()) {
            option.setInputState(
                    String.valueOf(filterController.getFilterViewState(option.getUniqueId()))
            );
        }
    }

    @Override
    public Boolean loadLastCheckedState(Option option) {
        return filterController.getFilterViewState(option);
    }

    @Override
    public void saveCheckedState(Option option, Boolean isChecked) {
        saveCheckedState(option, isChecked, "");
    }

    public void saveCheckedState(Option option, Boolean isChecked, String filterTitle) {
        SearchTracking.eventSearchResultFilterJourney(getContext(), filterTitle, option.getName(), false, isChecked);
        filterController.setFilter(option, isChecked);
        applyFilter();
    }

    public void updateResetButtonVisibility() {
        if (buttonReset != null) {
            buttonReset.setVisibility(filterController.isFilterActive() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void removeSavedTextInput(String uniqueId) {
        filterController.setFilter(OptionHelper.generateOptionFromUniqueId(uniqueId), false, true);

        String key = OptionHelper.parseKeyFromUniqueId(uniqueId);
        SearchTracking.eventSearchResultFilterJourney(getContext(), key, "", false, false);
        updateResetButtonVisibility();
    }

    @Override
    public void saveTextInput(String uniqueId, String textInput) {
        Option textInputOption = OptionHelper.generateOptionFromUniqueId(uniqueId);
        textInputOption.setValue(textInput);
        filterController.setFilter(textInputOption, true, true);

        String key = textInputOption.getKey();
        SearchTracking.eventSearchResultFilterJourney(getContext(), key, textInput, false, true);
        updateResetButtonVisibility();
    }

    @Override
    public List<Option> getSelectedOptions(Filter filter) {
        return filterController.getSelectedAndPopularOptions(filter);
    }

    @Override
    public void removeSelectedOption(Option option) {
        removeSelectedOption(option, "");
    }

    @Override
    public void removeSelectedOption(Option option, String filterTitle) {
        if (KEY_CATEGORY.equals(option.getKey())) {
            SearchTracking.eventSearchResultFilterJourney(getContext(), filterTitle, option.getName(), false, false);
            filterController.setFilter(option, false, true);
            applyFilter();
        } else {
            saveCheckedState(option, false, filterTitle);
        }
    }

    private void resetAllFilter() {
        filterController.resetAllFilters();
        filterMainAdapter.notifyDataSetChanged();

        applyFilter();
    }

    public void initFilterBottomSheet() {
        initBottomSheetListener();
        initFilterMainRecyclerView();
    }

    private void initFilterMainRecyclerView() {
        filterController = new FilterController();
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

    public void loadFilterItems(List<Filter> filterList, Map<String, String> searchParameter) {
        List<Filter> initializedFilterList = FilterHelper.initializeFilterList(filterList);

        filterController.initFilterController(searchParameter, initializedFilterList);
        updateResetButtonVisibility();
        filterMainAdapter.setFilterList(initializedFilterList);
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
        buttonFinish.setOnClickListener(v -> closeView());
        buttonReset.setOnClickListener(v -> resetAllFilter());
    }

    @Override
    public void onPriceSliderRelease(int minValue, int maxValue) {
        if (filterController.isSliderValueHasChanged(minValue, maxValue)) {
            applyFilter();
        }
    }

    @Override
    public void onPriceSliderPressed(int minValue, int maxValue) {
        filterController.saveSliderValueStates(minValue, maxValue);
    }

    @Override
    public void onPriceEditedFromTextInput(int minValue, int maxValue) {
        applyFilter();
    }

    @Override
    public boolean isSelectedCategory(Option option) {
        return filterController.getFilterViewState(option);
    }

    @Override
    public void selectCategory(Option option, String filterTitle) {
        SearchTracking.eventSearchResultFilterJourney(getContext(), filterTitle, option.getName(), false, true);
        filterController.setFilter(option, true, true);
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

    public void applyFilter() {
        updateResetButtonVisibility();
        loadingView.setVisibility(View.VISIBLE);
        buttonFinish.setText("");
        callback.onApplyFilter(filterController.getFilterParameter());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            updateResetButtonVisibility();
        }
    }

    private void handleResultFromDetailPage(Intent data) {
        List<Option> optionList
                = data.getParcelableArrayListExtra(AbstractDynamicFilterDetailActivity.EXTRA_RESULT);

        filterController.setFilter(optionList);
        applyFilterFromDetailPage();
    }

    private void handleResultFromLocationPage() {
        Observable.create(
                (Observable.OnSubscribe<List<Option>>) subscriber -> subscriber.onNext(FilterDbHelper.loadLocationFilterOptions()))
                .subscribeOn(Schedulers.newThread())
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
                        filterController.setFilter(optionList);
                        applyFilterFromDetailPage();
                    }
                });
    }

    private void handleResultFromCategoryPage(Intent data) {
        String selectedCategoryId = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_ID);

        Category category = FilterHelper.getSelectedCategoryDetailsFromFilterList(filterMainAdapter.getFilterList(), selectedCategoryId);

        String selectedCategoryName = category != null ? category.getCategoryName() : "";
        Option categoryOption = OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryName);

        filterController.setFilter(categoryOption, true, true);
        applyFilterFromDetailPage();
    }

    private void applyFilterFromDetailPage() {
        filterMainAdapter.notifyItemChanged(selectedExpandableItemPosition);
        applyFilter();
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

    @Override
    public String getFilterValue(String key) {
        return filterController.getFilterValue(key);
    }

    @Override
    public boolean getFilterViewState(String uniqueId) {
        return filterController.getFilterViewState(uniqueId);
    }

    public interface Callback {
        void onApplyFilter(Map<String, String> filterParameter);
        void onShow();
        void onHide();
        boolean isSearchShown();
        void hideKeyboard();

        void launchFilterCategoryPage(Filter filter, String selectedCategoryRootId, String selectedCategoryId);
        void launchFilterDetailPage(Filter filter);
    }
}
