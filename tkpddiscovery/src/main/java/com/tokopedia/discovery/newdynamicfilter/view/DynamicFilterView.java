package com.tokopedia.discovery.newdynamicfilter.view;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;

/**
 * Created by henrypriyono on 8/16/17.
 */

public interface DynamicFilterView {
    void onExpandableItemClicked(Filter filter);

    Boolean loadLastCheckedState(Option option);
    void saveCheckedState(Option option, Boolean isChecked);

    String loadLastTextInput(String key);
    void saveTextInput(String key, String textInput);
}
