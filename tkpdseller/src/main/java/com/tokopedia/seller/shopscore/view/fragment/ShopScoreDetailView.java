package com.tokopedia.seller.shopscore.view.fragment;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailViewModel;

import java.util.List;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public interface ShopScoreDetailView extends CustomerView {

    void renderShopScoreDetail(List<ShopScoreDetailViewModel> viewModel);
}
