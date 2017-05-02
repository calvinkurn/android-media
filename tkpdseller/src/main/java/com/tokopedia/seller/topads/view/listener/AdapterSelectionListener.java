package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

/**
 * Created by normansyahputa on 2/13/17.
 */

public interface AdapterSelectionListener<T> {
    void onChecked(int position, T data);

    void onUnChecked(int position, T data);

    boolean isSelected(TopAdsProductViewModel data);
}
