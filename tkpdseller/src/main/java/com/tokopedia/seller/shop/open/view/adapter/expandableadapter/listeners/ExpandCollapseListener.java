package com.tokopedia.seller.shop.open.view.adapter.expandableadapter.listeners;

public interface ExpandCollapseListener {

  void onGroupExpanded(int positionStart, int itemCount);

  void onGroupCollapsed(int positionStart, int itemCount);
}
