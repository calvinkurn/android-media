package com.tokopedia.discovery.newdynamicfilter.view;

import com.tokopedia.discovery.common.data.Option;

public interface BottomSheetDynamicFilterView extends DynamicFilterView {
    boolean isSelectedCategory(Option option);
    void selectCategory(Option option, String filterTitle);
    void saveCheckedState(Option option, Boolean isChecked, String filterTitle);
    void removeSelectedOption(Option option, String filterTitle);
}
