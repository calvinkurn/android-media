package com.tokopedia.seller.goldmerchant.statistic.view.model;

/**
 * Created by normansyahputa on 7/18/17.
 */

public class GMTransactionGraphMergeModel {
    public GMTransactionGraphViewModel gmTransactionGraphViewModel;
    public UnFinishedTransactionViewModel unFinishedTransactionViewModel;
    public GMGraphViewModel gmTopAdsAmountViewModel;
    private boolean isGoldMerchant;

    public void setIsGoldMerchant(boolean isGoldMerchant) {
        this.isGoldMerchant = isGoldMerchant;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }
}
