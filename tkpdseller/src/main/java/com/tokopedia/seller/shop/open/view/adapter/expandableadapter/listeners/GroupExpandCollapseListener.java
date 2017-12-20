package com.tokopedia.seller.shop.open.view.adapter.expandableadapter.listeners;

public interface GroupExpandCollapseListener<T> {

  void onGroupExpanded(T group);

  void onGroupCollapsed(T group);
}
