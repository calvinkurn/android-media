package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

/**
 * Created by yfsx on 02/01/18.
 */

public class ContentProductViewModel implements Visitable<FeedPlusTypeFactory> {
    private String imageUrl;
    private String applink;
    private String buttonTitle;
    private String textHeader;
    private String textDescription;
    private boolean isContentProductShowing;

    public ContentProductViewModel(String imageUrl, String applink, String buttonTitle, String textHeader, String textDescription, boolean isContentProductShowing) {
        this.imageUrl = imageUrl;
        this.applink = applink;
        this.buttonTitle = buttonTitle;
        this.textHeader = textHeader;
        this.textDescription = textDescription;
        this.isContentProductShowing = isContentProductShowing;
    }

    public ContentProductViewModel(boolean isContentProductShowing) {
        this.isContentProductShowing = isContentProductShowing;
    }

    public boolean isContentProductShowing() {
        return isContentProductShowing;
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

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
