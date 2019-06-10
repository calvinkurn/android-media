package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import android.support.annotation.DrawableRes;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionTypeFactory;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;

/**
 * Created by henrypriyono on 10/31/17.
 */

public class EmptySearchModel implements Visitable<BrowseSectionTypeFactory> {

    @DrawableRes
    private int imageRes;
    private String title;
    private String content;
    private String buttonText;
    private FilterFlagSelectedModel filterFlagSelectedModel;

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public FilterFlagSelectedModel getFilterFlagSelectedModel() {
        return filterFlagSelectedModel;
    }

    public void setFilterFlagSelectedModel(FilterFlagSelectedModel filterFlagSelectedModel) {
        this.filterFlagSelectedModel = filterFlagSelectedModel;
    }

    @Override
    public int type(BrowseSectionTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
