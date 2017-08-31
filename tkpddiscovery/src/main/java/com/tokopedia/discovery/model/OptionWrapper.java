package com.tokopedia.discovery.model;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.list.item.AlphabeticallySortableItem;

/**
 * Created by henrypriyono on 8/31/17.
 */

public class OptionWrapper implements AlphabeticallySortableItem {
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
    public String getSortableKey() {
        return option.getName().toLowerCase();
    }

    @Override
    public char getFirstCharacter() {
        return option.getName().toUpperCase().charAt(0);
    }
}
