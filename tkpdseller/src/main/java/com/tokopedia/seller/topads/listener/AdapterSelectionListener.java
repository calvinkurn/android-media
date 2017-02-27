package com.tokopedia.seller.topads.listener;

import com.tokopedia.seller.topads.view.models.TopAdsProductViewModel;

/**
 * Created by normansyahputa on 2/13/17.
 */

public interface AdapterSelectionListener<T> {
    void onChecked(int position, T data);

    void onUnChecked(int position, T data);

    boolean isSelected(TopAdsProductViewModel data);
}
