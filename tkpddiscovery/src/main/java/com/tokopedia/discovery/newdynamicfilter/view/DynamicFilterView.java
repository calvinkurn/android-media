package com.tokopedia.discovery.newdynamicfilter.view;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;

import java.util.List;

/**
 * Created by henrypriyono on 8/16/17.
 */

public interface DynamicFilterView {

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

    void onExpandableItemClicked(Filter filter);
    void applyFilter();
    void trackSearch(String filterName, String filterValue, boolean isActive);
}
