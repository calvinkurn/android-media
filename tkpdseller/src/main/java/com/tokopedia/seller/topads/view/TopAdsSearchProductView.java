package com.tokopedia.seller.topads.view;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.view.models.TypeBasedModel;

import java.util.List;

/**
 * Created by normansyahputa on 2/20/17.
 */

public interface TopAdsSearchProductView extends CustomerView {
    void loadData(List<TypeBasedModel> datas);

    void loadMore(List<TypeBasedModel> datas);

    void dismissSnackbar();

    boolean isExistingGroup();
}
