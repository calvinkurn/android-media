package com.tokopedia.seller.topads.listener;

import com.tokopedia.seller.topads.view.models.TopAdsProductViewModel;

/**
 * Created by normansyahputa on 2/13/17.
 */

public interface FragmentItemSelection extends AdapterSelectionListener<TopAdsProductViewModel> {
    void notifyUnchecked(TopAdsProductViewModel topAdsProductViewModel);

    boolean isExistingGroup();
}
