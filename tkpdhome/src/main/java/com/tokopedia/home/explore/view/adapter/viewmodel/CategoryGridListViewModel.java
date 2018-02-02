package com.tokopedia.home.explore.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.explore.domain.model.CategoryLayoutRowModel;
import com.tokopedia.home.explore.view.adapter.TypeFactory;

import java.util.List;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class CategoryGridListViewModel implements Visitable<TypeFactory> {

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
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
