package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.recentview.RecentViewViewHolder;

import java.util.ArrayList;

/**
 * @author by nisie on 7/3/17.
 */

public class RecentViewViewModel implements Visitable<FeedPlusTypeFactory> {

    private final ArrayList<RecentViewProductViewModel> listProduct;

    public RecentViewViewModel(ArrayList<RecentViewProductViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public ArrayList<RecentViewProductViewModel> getListProduct() {
        return listProduct;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
