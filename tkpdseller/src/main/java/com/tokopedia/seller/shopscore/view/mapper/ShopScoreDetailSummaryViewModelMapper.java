package com.tokopedia.seller.shopscore.view.mapper;

import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailSummaryDomainModel;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailSummaryViewModel;

/**
 * @author sebastianuskh on 3/2/17.
 */
public class ShopScoreDetailSummaryViewModelMapper {
    public static ShopScoreDetailSummaryViewModel map(ShopScoreDetailDomainModel domainModels) {
        ShopScoreDetailSummaryViewModel viewModel = new ShopScoreDetailSummaryViewModel();
        ShopScoreDetailSummaryDomainModel summaryModel = domainModels.getSummaryModel();

        viewModel.setColor(summaryModel.getColor());
        viewModel.setValue(summaryModel.getValue());
        viewModel.setText(summaryModel.getText());

        return viewModel;
    }
}
