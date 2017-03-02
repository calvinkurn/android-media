package com.tokopedia.seller.shopscore.view.fragment;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailItemViewModel;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailStateEnum;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailSummaryViewModel;

import java.util.List;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public interface ShopScoreDetailView extends CustomerView {

    void renderShopScoreDetail(List<ShopScoreDetailItemViewModel> viewModel);

    void renderShopScoreSummary(ShopScoreDetailSummaryViewModel viewModel);

    void renderShopScoreState(ShopScoreDetailStateEnum shopScoreDetailStateEnum);

    void showProgressDialog();

    void dismissProgressDialog();

    void emptyState();
}
