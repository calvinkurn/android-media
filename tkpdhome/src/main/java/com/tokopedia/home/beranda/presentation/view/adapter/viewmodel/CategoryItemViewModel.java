package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;
import com.tokopedia.home.explore.domain.model.CategoryLayoutRowModel;

import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class CategoryItemViewModel implements Visitable<HomeTypeFactory> {

    private int sectionId;
    private String title;
    private List<CategoryLayoutRowModel> itemList;

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

    public List<CategoryLayoutRowModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<CategoryLayoutRowModel> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
