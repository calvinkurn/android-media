package com.tokopedia.discovery.newdynamicfilter.view;

import com.tokopedia.core.discovery.model.Option;

public interface BottomSheetDynamicFilterView extends DynamicFilterView {
    @Deprecated
    boolean isSelectedCategory(Option option);
    @Deprecated
    void selectCategory(Option option, String filterTitle);
    void saveCheckedState(Option option, Boolean isChecked, String filterTitle);
    @Deprecated
    void removeSelectedOption(Option option, String filterTitle);
}
