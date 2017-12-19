package com.tokopedia.seller.shop.open.view.adapter.expandableadapter.listeners;

import com.tokopedia.seller.shop.open.view.adapter.expandableadapter.models.ExpandableGroup;

public interface GroupExpandCollapseListener {

  void onGroupExpanded(ExpandableGroup group);

  void onGroupCollapsed(ExpandableGroup group);
}
