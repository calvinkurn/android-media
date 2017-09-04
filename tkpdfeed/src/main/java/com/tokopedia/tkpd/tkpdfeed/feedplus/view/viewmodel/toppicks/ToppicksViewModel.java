package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 8/11/17.
 */

public class ToppicksViewModel implements Visitable<FeedPlusTypeFactory> {

    private ArrayList<ToppicksItemViewModel> list;

    public ToppicksViewModel(ArrayList<ToppicksItemViewModel> list) {
        this.list = list;
    }

    public ArrayList<ToppicksItemViewModel> getList() {
        return list;
    }

    public void setList(ArrayList<ToppicksItemViewModel> list) {
        this.list = list;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
