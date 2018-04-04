package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class CategorySectionViewModel implements Visitable<HomeTypeFactory> {

    private List<LayoutSections> sectionList;

    public CategorySectionViewModel() {
        sectionList = new ArrayList<>();
    }

    public List<LayoutSections> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<LayoutSections> sectionList) {
        this.sectionList = sectionList;
    }

    public void addSection(LayoutSections section){
        this.sectionList.add(section);
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
