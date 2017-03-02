package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

/**
 * Created by normansyahputa on 2/13/17.
 */

public interface FragmentItemSelection extends AdapterSelectionListener<TopAdsProductViewModel> {
    void notifyUnchecked(TopAdsProductViewModel topAdsProductViewModel);

    boolean isExistingGroup();
}
