package com.tokopedia.seller.shop.open.view.adapter.expandableadapter.listeners;

public interface ExpandCollapseListener<T> {

    void onGroupExpanded(int positionStart, int itemCount);

    void onGroupCollapsed(int positionStart, int itemCount);

    int getChildCount(T group);

}
