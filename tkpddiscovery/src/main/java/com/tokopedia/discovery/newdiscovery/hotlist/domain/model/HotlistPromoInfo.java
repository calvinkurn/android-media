package com.tokopedia.discovery.newdiscovery.hotlist.domain.model;

/**
 * Created by nakama on 12/5/17.
 */

public class HotlistPromoInfo {

    private String title;
    private String voucherCode;
    private String minimunTransaction;
    private String validDate;
    private String urlTermCondition;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getMinimunTransaction() {
        return minimunTransaction;
    }

    public void setMinimunTransaction(String minimunTransaction) {
        this.minimunTransaction = minimunTransaction;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setPromoPeriod(String validDate) {
        this.validDate = validDate;
    }

    public String getUrlTermCondition() {
        return urlTermCondition;
    }

    public void setUrlTermCondition(String urlTermCondition) {
        this.urlTermCondition = urlTermCondition;
    }
}
