package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

/**
 * Created by yfsx on 08/01/18.
 */

public class KolCtaDomain {
    private final String imageUrl;
    private final String applink;
    private final String buttonTitle;
    private final String textHeader;
    private final String textDescription;

    public KolCtaDomain(String imageUrl, String applink, String buttonTitle, String textHeader, String textDescription) {
        this.imageUrl = imageUrl;
        this.applink = applink;
        this.buttonTitle = buttonTitle;
        this.textHeader = textHeader;
        this.textDescription = textDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getApplink() {
        return applink;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public String getTextHeader() {
        return textHeader;
    }

    public String getTextDescription() {
        return textDescription;
    }
}
