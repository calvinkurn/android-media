package com.tokopedia.transaction.purchase.model;

/**
 * Created by herdimac on 4/12/16.
 */
public class TxGetPaymentItem {

    private String page;
    private String perPage;
    private String userID;

    public TxGetPaymentItem(String pg, String perPg, String uid) {
        page = pg;
        perPage = perPg;
        userID = uid;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPerPage() {
        return perPage;
    }

    public void setPerPage(String perPage) {
        this.perPage = perPage;
    }
}
