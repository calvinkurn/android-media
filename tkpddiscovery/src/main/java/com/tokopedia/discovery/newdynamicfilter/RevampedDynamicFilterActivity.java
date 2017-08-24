package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.activity.FilterMapAtribut;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactoryImpl;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterDetailActivityRouter;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.core.discovery.model.Option.CATEGORY_KEY;

/**
 * Created by henrypriyono on 8/8/17.
 */

public class RevampedDynamicFilterActivity extends AppCompatActivity implements DynamicFilterView {

    public static final int REQUEST_CODE = 219;
    public static final String EXTRA_FILTERS = "EXTRA_FILTERS";

    public static final String FILTER_CHECKED_STATE_PREF = "filter_checked_state";
    public static final String FILTER_TEXT_PREF = "filter_text";

    private static final String FILTER_SELECTED_CATEGORY_ID_PREF = "filter_selected_category_id";
    private static final String FILTER_SELECTED_CATEGORY_NAME_PREF = "filter_selected_category_name";
    private static final String EXTRA_FILTER_LIST = "EXTRA_FILTER_LIST";

    RecyclerView recyclerView;
    DynamicFilterAdapter adapter;
    TextView buttonApply;
    TextView buttonReset;
    View buttonClose;

    HashMap<String, Boolean> savedCheckedState = new HashMap<>();
    HashMap<String, String> savedTextInput = new HashMap<>();

    private SharedPreferences preferences;
    private int selectedExpandableItemPosition;
    private String selectedCategoryId;
    private String selectedCategoryName;

    public static void moveTo(AppCompatActivity activity,
                              List<Filter> filterCategoryList) {
        if (activity != null) {
            Intent intent = new Intent(activity, RevampedDynamicFilterActivity.class);
            intent.putExtra(EXTRA_FILTER_LIST, Parcels.wrap(filterCategoryList));
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revamped_dynamic_filter);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        bindView();
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
    }

    private void initRecyclerView() {
        DynamicFilterTypeFactory dynamicFilterTypeFactory = new DynamicFilterTypeFactoryImpl(this);
        adapter = new DynamicFilterAdapter(dynamicFilterTypeFactory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.line_separator_medium));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    private void loadFilterItems() {
        List<Filter> filterList = Parcels.unwrap(
                getIntent().getParcelableExtra(EXTRA_FILTER_LIST));
        adapter.setFilterList(filterList);
    }

    private void loadLastFilterState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            recoverLastFilterState(savedInstanceState);
        } else {
            loadLastFilterStateFromPreference();
        }
    }

    private void recoverLastFilterState(Bundle savedInstanceState) {
        savedCheckedState = Parcels.unwrap(savedInstanceState.getParcelable(FILTER_CHECKED_STATE_PREF));
        savedTextInput = Parcels.unwrap(savedInstanceState.getParcelable(FILTER_TEXT_PREF));
        selectedCategoryId = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_ID_PREF);
        selectedCategoryName = savedInstanceState.getString(FILTER_SELECTED_CATEGORY_NAME_PREF);
    }

    private void loadLastFilterStateFromPreference() {
        String savedCheckedStateJson = preferences.getString(FILTER_CHECKED_STATE_PREF, new Gson().toJson(savedCheckedState));
        savedCheckedState = new Gson().fromJson(savedCheckedStateJson, savedCheckedState.getClass());

        String savedTextInputJson = preferences.getString(FILTER_TEXT_PREF, new Gson().toJson(savedTextInput));
        savedTextInput = new Gson().fromJson(savedTextInputJson, savedTextInput.getClass());

        selectedCategoryId = preferences.getString(FILTER_SELECTED_CATEGORY_ID_PREF, selectedCategoryId);
        selectedCategoryName = preferences.getString(FILTER_SELECTED_CATEGORY_NAME_PREF, selectedCategoryName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FILTER_CHECKED_STATE_PREF, Parcels.wrap(savedCheckedState));
        outState.putParcelable(FILTER_TEXT_PREF, Parcels.wrap(savedTextInput));
        outState.putString(FILTER_SELECTED_CATEGORY_ID_PREF, selectedCategoryId);
        outState.putString(FILTER_SELECTED_CATEGORY_NAME_PREF, selectedCategoryName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DynamicFilterDetailActivity.REQUEST_CODE:
                    handleResultFromDetailPage(data);
                    break;
                case DynamicFilterCategoryActivity.REQUEST_CODE:
                    handleResultFromCategoryPage(data);
                    break;
            }
        }
        adapter.notifyItemChanged(selectedExpandableItemPosition);
    }

    private void handleResultFromDetailPage(Intent data) {
        List<Option> optionList
                = Parcels.unwrap(data.getParcelableExtra(DynamicFilterDetailActivity.EXTRA_RESULT));
        for (Option option : optionList) {
            OptionHelper.saveOptionInputState(option, savedCheckedState, savedTextInput);
        }
    }

    private void handleResultFromCategoryPage(Intent data) {
        selectedCategoryId
                = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_RESULT_SELECTED_CATEGORY_ID);
        selectedCategoryName
                = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_RESULT_SELECTED_CATEGORY_NAME);
    }

    private void applyFilter() {
        writeFilterCheckedStateToPreference();
        writeFilterTextInputToPreference();
        writeSelectedCategoryToPreference();
        renderFilterResult();
        finish();
    }

    private void writeFilterCheckedStateToPreference() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FILTER_CHECKED_STATE_PREF, new Gson().toJson(savedCheckedState));
        editor.apply();
    }

    private void writeFilterTextInputToPreference() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FILTER_TEXT_PREF, new Gson().toJson(savedTextInput));
        editor.apply();
    }

    private void writeSelectedCategoryToPreference() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FILTER_SELECTED_CATEGORY_ID_PREF, selectedCategoryId);
        editor.putString(FILTER_SELECTED_CATEGORY_NAME_PREF, selectedCategoryName);
        editor.apply();
    }

    private void renderFilterResult() {
        HashMap<String, String> selectedFilter = generateSelectedFilterMap();

        Intent intent = new Intent();
        FilterMapAtribut.FilterMapValue filterMapValue = new FilterMapAtribut.FilterMapValue();
        filterMapValue.setValue(selectedFilter);
        intent.putExtra(EXTRA_FILTERS, filterMapValue);
        setResult(RESULT_OK, intent);
    }

    private HashMap<String, String> generateSelectedFilterMap() {
        HashMap<String, String> selectedFilterMap = new HashMap<>();

        if (!TextUtils.isEmpty(selectedCategoryId)) {
            selectedFilterMap.put(CATEGORY_KEY, selectedCategoryId);
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
        savedCheckedState.clear();
        savedTextInput.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onExpandableItemClicked(Filter filter) {
        selectedExpandableItemPosition = adapter.getItemPosition(filter);
        enrichWithInputState(filter);
        FilterDetailActivityRouter.launchDetailActivity(this, filter);
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
    public String loadLastTextInput(String key) {
        return savedTextInput.get(key);
    }

    @Override
    public void saveTextInput(String key, String textInput) {
        savedTextInput.put(key, textInput);
    }

    @Override
    public List<Option> getSelectedOptions(Filter filter) {
        List<Option> selectedOptions = new ArrayList<>();

        if (Filter.TEMPLATE_NAME_CATEGORY.equals(filter.getTemplateName())) {
            selectedOptions.add(getSelectedCategoryAsOption());
        } else {
            selectedOptions.addAll(getCheckedOptionList(filter));
        }
        return selectedOptions;
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
        if (CATEGORY_KEY.equals(option.getKey())) {
            selectedCategoryId = null;
            selectedCategoryName = null;
        } else {
            saveCheckedState(option, false);
        }
    }
}
