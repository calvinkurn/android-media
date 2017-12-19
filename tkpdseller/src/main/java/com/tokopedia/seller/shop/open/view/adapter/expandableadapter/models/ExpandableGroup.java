package com.tokopedia.seller.shop.open.view.adapter.expandableadapter.models;

import java.util.List;


public interface ExpandableGroup<T> {

    List<T> getChildItems();

    int getChildItemCount();
}
