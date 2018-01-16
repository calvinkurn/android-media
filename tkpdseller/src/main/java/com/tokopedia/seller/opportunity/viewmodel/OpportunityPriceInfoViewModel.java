package com.tokopedia.seller.opportunity.viewmodel;

/**
 * Created by normansyahputa on 1/15/18.
 */

public class OpportunityPriceInfoViewModel {
    String title, strikeThroughText, NonStrikeThroughText;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStrikeThroughText() {
        return strikeThroughText;
    }

    public void setStrikeThroughText(String strikeThroughText) {
        this.strikeThroughText = strikeThroughText;
    }

    public String getNonStrikeThroughText() {
        return NonStrikeThroughText;
    }

    public void setNonStrikeThroughText(String nonStrikeThroughText) {
        NonStrikeThroughText = nonStrikeThroughText;
    }
}
