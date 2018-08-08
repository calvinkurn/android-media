package com.tokopedia.discovery.model;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.list.item.SectionDividedItem;

/**
 * Created by henrypriyono on 8/31/17.
 */

public class OptionWrapper implements SectionDividedItem {
    private Option option;

    public OptionWrapper(Option option) {
        this.option = option;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    @Override
    public int getSectionId() {
        if (option.isPopular()) {
            return POPULAR_SECTION_ID;
        }

        int sectionId = option.getName().toUpperCase().charAt(0);
        if (sectionId >= 'A') {
            return sectionId;
        } else {
            return '#';
        }
    }
}
