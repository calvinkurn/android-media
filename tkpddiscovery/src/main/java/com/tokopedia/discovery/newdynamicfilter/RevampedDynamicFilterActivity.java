package com.tokopedia.discovery.newdynamicfilter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.ToastNetworkHandler;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.design.keyboard.KeyboardHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.common.data.Filter;
import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.discovery.model.Category;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactoryImpl;
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController;
import com.tokopedia.discovery.newdynamicfilter.helper.DynamicFilterDbManager;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDbHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDetailActivityRouter;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.discovery.model.Option.KEY_CATEGORY;

/**
 * Created by henrypriyono on 8/8/17.
 */

public class RevampedDynamicFilterActivity extends BaseActivity implements DynamicFilterView {

    public static final int REQUEST_CODE = 219;
    public static final String EXTRA_SELECTED_FILTERS = "EXTRA_SELECTED_FILTERS";
    public static final String EXTRA_FILTER_LIST = "EXTRA_FILTER_LIST";
    public static final String EXTRA_SELECTED_FLAG_FILTER = "EXTRA_SELECTED_FLAG_FILTER";
    public static final String EXTRA_FILTER_PARAMETER = "EXTRA_FILTER_PARAMETER";

    public static final String FILTER_CHECKED_STATE_PREF = "filter_checked_state";
    public static final String FILTER_TEXT_PREF = "filter_text";
    public static final String FILTER_SELECTED_CATEGORY_ROOT_ID_PREF = "filter_selected_category_root_id";
    public static final String FILTER_SELECTED_CATEGORY_ID_PREF = "filter_selected_category_id";
    public static final String FILTER_SELECTED_CATEGORY_NAME_PREF = "filter_selected_category_name";

    public static Intent createInstance(Context context, String filterID, HashMap<String, String> searchParameter, @Nullable FilterFlagSelectedModel flagFilterHelper) {
        Intent intent = new Intent(context, RevampedDynamicFilterActivity.class);
        intent.putExtra(EXTRA_FILTER_LIST, filterID);
        intent.putExtra(EXTRA_FILTER_PARAMETER, searchParameter);

        if(flagFilterHelper != null) {
            intent.putExtra(EXTRA_SELECTED_FLAG_FILTER, flagFilterHelper);
        }

        return intent;
    }

    private static final String TAG = RevampedDynamicFilterActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private DynamicFilterAdapter adapter;
    private TextView buttonApply;
    private TextView buttonReset;
    private View buttonClose;
    private View mainLayout;
    private View loadingView;

    private FilterController filterController;
    HashMap<String, Boolean> savedCheckedState = new HashMap<>();
    HashMap<String, String> savedTextInput = new HashMap<>();

    private int selectedExpandableItemPosition;
    private String selectedCategoryId;
    private String selectedCategoryName;
    private String selectedCategoryRootId;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

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
        recyclerView = findViewById(R.id.dynamic_filter_recycler_view);
        buttonClose = findViewById(R.id.top_bar_close_button);
        buttonClose.setOnClickListener(v -> onBackPressed());
        buttonReset = findViewById(R.id.top_bar_button_reset);
        buttonReset.setOnClickListener(v -> resetAllFilter());
        buttonApply = findViewById(R.id.button_finish);
        buttonApply.setOnClickListener(v -> applyFilter());
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
        filterController = new FilterController();
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
                        .map(this::getFilterListFromDbManager)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getSubsriberGetFilterListFromDb())
        );
    }

    private List<Filter> getFilterListFromDbManager(DynamicFilterDbManager manager) throws RuntimeException {
        String data = DynamicFilterDbManager.getFilterData(this, getIntent().getStringExtra(EXTRA_FILTER_LIST));
        if (data == null) {
            throw new RuntimeException("error get filter cache");
        } else {
            Type listType = new TypeToken<List<Filter>>() {}.getType();
            Gson gson = new Gson();
            return gson.fromJson(data, listType);
        }
    }

    private Subscriber<List<Filter>> getSubsriberGetFilterListFromDb() {
        return new Subscriber<List<Filter>>() {
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
            public void onNext(List<Filter> filterList) {
                List<Filter> initializedFilterList = FilterHelper.initializeFilterList(filterList);
                filterController.initFilterController(getSearchParameterFromIntent(), initializedFilterList);
                adapter.setFilterList(initializedFilterList);
            }
        };
    }

    private Map<String, String> getSearchParameterFromIntent() {
        Map<?, ?> searchParameterMapIntent = (Map<?, ?>)getIntent().getSerializableExtra(EXTRA_FILTER_PARAMETER);

        Map<String, String> searchParameter = new HashMap<>(searchParameterMapIntent.size());

        for(Map.Entry<?, ?> entry: searchParameterMapIntent.entrySet()) {
            searchParameter.put(entry.getKey().toString(), entry.getValue().toString());
        }

        return searchParameter;
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
        overridePendingTransition(android.R.anim.fade_in, com.tokopedia.core2.R.anim.push_down);
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

        filterController.setFilter(optionList);

        for(Option option : optionList) {
            OptionHelper.saveOptionInputState(option, savedCheckedState, savedTextInput);
        }
    }

    private void handleResultFromLocationPage() {
        Observable.create(
                (Observable.OnSubscribe<List<Option>>) subscriber -> subscriber.onNext(FilterDbHelper.loadLocationFilterOptions(RevampedDynamicFilterActivity.this)))
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
                        for (Option option : optionList) {
                            OptionHelper.saveOptionInputState(option, savedCheckedState, savedTextInput);
                        }

                        filterController.setFilter(optionList);
                        adapter.notifyItemChanged(selectedExpandableItemPosition);
                        hideLoading();
                    }
                });
    }

    private void handleResultFromCategoryPage(Intent data) {
        selectedCategoryId = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_ID);
        selectedCategoryName = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_NAME);
        selectedCategoryRootId = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_ROOT_ID);

        Category category = FilterHelper.getSelectedCategoryDetailsFromFilterList(adapter.getFilterList(), selectedCategoryId);

        String selectedCategoryNameFromList = category != null ? category.getCategoryName() : "";
        Option categoryOption = OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryNameFromList);

        filterController.setFilter(categoryOption, true, true);
    }

    @Override
    public void onPriceSliderRelease(int minValue, int maxValue) {

    }

    @Override
    public void onPriceSliderPressed(int minValue, int maxValue) {

    }

    @Override
    public void onPriceEditedFromTextInput(int minValue, int maxValue) {

    }

    public void applyFilter() {
        renderFilterResult();
        finish();
    }

    private void renderFilterResult() {
        Intent intent = new Intent();
        HashMap<String, String> filterParameterHashMap = new HashMap<>(filterController.getParameter());
        HashMap<String, String> activeFilterParameterHashMap = new HashMap<>(filterController.getActiveFilterMap());

        intent.putExtra(EXTRA_FILTER_PARAMETER, filterParameterHashMap);
        intent.putExtra(EXTRA_SELECTED_FILTERS, activeFilterParameterHashMap);
        intent.putExtra(EXTRA_SELECTED_FLAG_FILTER, getFilterFlagSelected());

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

    public void resetAllFilter() {
        filterController.resetAllFilters();
        adapter.notifyDataSetChanged();
    }

    private void resetSelectedCategory() {
        selectedCategoryId = null;
        selectedCategoryRootId = null;
        selectedCategoryName = null;
    }

    @Override
    public void onExpandableItemClicked(Filter filter) {
        showLoading();
        selectedExpandableItemPosition = adapter.getItemPosition(filter);
        if (filter.isCategoryFilter()) {
            launchFilterCategoryPage(filter);
        } else {
            enrichWithInputState(filter);
            FilterDetailActivityRouter.launchDetailActivity(this, filter);
        }
    }

    private void launchFilterCategoryPage(Filter filter) {
        String categoryId = filterController.getFilterValue(SearchApiConst.SC);
        Category selectedCategory = FilterHelper.getSelectedCategoryDetails(filter, categoryId);
        String selectedCategoryRootId = selectedCategory != null ? selectedCategory.getCategoryRootId() : "";

        FilterDetailActivityRouter
                .launchCategoryActivity(this, filter, selectedCategoryRootId, categoryId);
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
        filterController.setFilter(option, isChecked);
        savedCheckedState.put(option.getUniqueId(), isChecked);
    }

    @Override
    public void removeSavedTextInput(String uniqueId) {
        filterController.setFilter(OptionHelper.generateOptionFromUniqueId(uniqueId), false, true);

        String optionKey = OptionHelper.parseKeyFromUniqueId(uniqueId);
        savedTextInput.remove(optionKey);
    }

    @Override
    public void saveTextInput(String uniqueId, String textInput) {
        Option textInputOption = OptionHelper.generateOptionFromUniqueId(uniqueId);
        textInputOption.setValue(textInput);
        filterController.setFilter(textInputOption, true, true);

        String key = textInputOption.getKey();
        savedTextInput.put(key, textInput);
    }

    @Override
    public List<Option> getSelectedOptions(Filter filter) {
        return filterController.getSelectedOptions(filter);
    }

    @Override
    public void removeSelectedOption(Option option) {
        if (KEY_CATEGORY.equals(option.getKey())) {
            resetSelectedCategory();
            filterController.setFilter(option, false, true);
        } else {
            saveCheckedState(option, false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    @Override
    public String getFilterValue(String key) {
        return filterController.getFilterValue(key);
    }

    @Override
    public boolean getFilterViewState(String uniqueId) {
        return filterController.getFilterViewState(uniqueId);
    }
}
