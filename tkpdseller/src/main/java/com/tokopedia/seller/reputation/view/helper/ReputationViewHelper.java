package com.tokopedia.seller.reputation.view.helper;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.R;
import com.tokopedia.seller.home.view.ReputationView;

/**
 * @author normansyahputa on 3/17/17.
 */
public class ReputationViewHelper {

    private TextView textHeaderReputation;
    private ReputationView sellerReputationHeaderView;

    public ReputationViewHelper(View itemView) {
        textHeaderReputation = itemView.findViewById(R.id.text_header_reputation);
        sellerReputationHeaderView = itemView.findViewById(R.id.seller_reputation_header_view);

        textHeaderReputation.setText(R.string.shop_reputation);
    }

    public void renderData(ShopModel shopModel) {
        ReputationView.ReputationViewModel reputationViewModel = new ReputationView.ReputationViewModel();
        reputationViewModel.typeMedal = shopModel.stats.shopBadgeLevel.set;
        reputationViewModel.levelMedal = shopModel.stats.shopBadgeLevel.level;
        reputationViewModel.reputationPoints = shopModel.stats.shopReputationScore;
        reputationViewModel.stats = shopModel.stats;

        sellerReputationHeaderView.init(reputationViewModel);
    }
}
