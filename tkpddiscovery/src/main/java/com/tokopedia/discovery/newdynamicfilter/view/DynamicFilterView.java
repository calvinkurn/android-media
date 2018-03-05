package com.tokopedia.discovery.newdynamicfilter.view;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;

import java.util.List;

/**
 * Created by henrypriyono on 8/16/17.
 */

public interface DynamicFilterView {
    void onExpandableItemClicked(Filter filter);

    Boolean loadLastCheckedState(Option option);
    void saveCheckedState(Option option, Boolean isChecked);

    String removeSavedTextInput(String key);
    void saveTextInput(String key, String textInput);

    List<Option> getSelectedOptions(Filter filter);
    void removeSelectedOption(Option option);

    void updateLastRangeValue(int minValue, int maxValue);
    void onPriceSliderRelease();
    void onPriceSliderPressed();
}
