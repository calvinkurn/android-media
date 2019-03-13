package com.tokopedia.discovery.newdynamicfilter.view;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;

import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 8/16/17.
 */

public interface DynamicFilterView {
    void onExpandableItemClicked(Filter filter);

    @Deprecated
    Boolean loadLastCheckedState(Option option);
    @Deprecated
    void saveCheckedState(Option option, Boolean isChecked);

    @Deprecated
    void removeSavedTextInput(String key);
    @Deprecated
    void saveTextInput(String key, String textInput);

    List<Option> getSelectedOptions(Filter filter);
    void removeSelectedOption(Option option);

    void updateLastRangeValue(int minValue, int maxValue);
    void onPriceSliderRelease(int minValue, int maxValue);
    void onPriceSliderPressed(int minValue, int maxValue);

    void onPriceEditedFromTextInput(int minValue, int maxValue);

    void setFilterValue(Option option, boolean value, boolean isAppliedImmediately);
    void setFilterValue(Option option, String value, boolean isAppliedImmediately);
    String getFilterValue(String key);

    void setFlagFilterHelper(Option option, boolean value, boolean isAppliedImmediately);
    boolean getFlagFilterHelperValue(String key);

    void applyFilter(Map<String, String> searchParameterWithFilter);
}
