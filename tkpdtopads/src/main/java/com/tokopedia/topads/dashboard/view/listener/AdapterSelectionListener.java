package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

/**
 * Created by normansyahputa on 2/13/17.
 */

public interface AdapterSelectionListener<T> {
    void onChecked(int position, T data);

    void onUnChecked(int position, T data);

    boolean isSelected(TopAdsProductViewModel data);
}
