package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class DigitalsViewModel implements Visitable<HomeTypeFactory> {

    private String title;
    private int sectionId;

    public DigitalsViewModel(String title, int sectionId) {
        this.title = title;
        this.sectionId = sectionId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
