package com.tokopedia.seller.goldmerchant.statistic.view.model;

/**
 * @author normansyahputa on 7/13/17.
 */

public class UnFinishedTransactionViewModel {
    private long totalTransactionCount;

    private String onHoldAmountText;

    private long onHoldCount;
    private long resoCount;
    private long onHoldAmount;

    public UnFinishedTransactionViewModel(long onHoldCount, long resoCount, long onHoldAmount, String formatAmount) {
        this.onHoldCount = onHoldCount;
        this.resoCount = resoCount;
        this.onHoldAmount = onHoldAmount;

        totalTransactionCount = resoCount + onHoldCount;

        this.onHoldAmountText = String.format(formatAmount, onHoldAmount);
    }


}
