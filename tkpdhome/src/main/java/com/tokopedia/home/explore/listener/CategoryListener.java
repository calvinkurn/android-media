package com.tokopedia.home.explore.listener;

import com.tokopedia.home.explore.domain.model.CategoryLayoutRowModel;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public interface CategoryListener {
    void onMarketPlaceItemClicked(CategoryLayoutRowModel data);

    void onDigitalItemClicked(CategoryLayoutRowModel data);

    void onGimickItemClicked(CategoryLayoutRowModel data);

    void onApplinkClicked(CategoryLayoutRowModel data);
}
