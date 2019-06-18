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

    void removeSavedTextInput(String uniqueId);
    void saveTextInput(String uniqueId, String textInput);

    List<Option> getSelectedOptions(Filter filter);
    void removeSelectedOption(Option option);

    void onPriceSliderRelease(int minValue, int maxValue);
    void onPriceSliderPressed(int minValue, int maxValue);

    void onPriceEditedFromTextInput(int minValue, int maxValue);

    String getFilterValue(String key);
    boolean getFilterViewState(String uniqueId);
}
