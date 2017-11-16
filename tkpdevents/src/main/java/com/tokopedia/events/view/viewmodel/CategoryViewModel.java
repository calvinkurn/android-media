package com.tokopedia.events.view.viewmodel;

import java.util.List;

/**
 * Created by ashwanityagi on 16/11/17.
 */

public class CategoryViewModel {
    private String title;
    private String name;
    private List<CategoryItemsViewModel> items = null;

    public CategoryViewModel(String title, String name, List<CategoryItemsViewModel> items) {
        this.title=title;
        this.name=name;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryItemsViewModel> getItems() {
        return items;
    }

    public void setItems(List<CategoryItemsViewModel> items) {
        this.items = items;
    }
}
