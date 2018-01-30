package com.tokopedia.home.explore.listener;

import com.tokopedia.home.explore.domain.model.CategoryLayoutRowModel;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public interface CategoryListener {
    void onMarketPlaceItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);

    void onDigitalItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);

    void onGimickItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);

    void onApplinkClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);
}
