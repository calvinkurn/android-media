package com.tokopedia.discovery.newdynamicfilter.view;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;

/**
 * Created by henrypriyono on 8/16/17.
 */

public interface DynamicFilterView {
    void onExpandableItemClicked(Filter filter);
    String getSelectedFilter(String key);
    void saveSelectedFilter(String key, String value);
    void removeSelectedFilter(String key);

    Boolean getLastCheckedState(Option option);
    void saveCheckedState(Option option, Boolean isChecked);
    void removeCheckedState(String key);

    String getTextInput(String key);
    void saveTextInput(String key, String textInput);
    void removeTextInput(String key);
}
