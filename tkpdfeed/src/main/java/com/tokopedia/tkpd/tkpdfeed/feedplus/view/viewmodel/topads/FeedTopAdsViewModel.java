package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.List;

/**
 * @author by nisie on 10/30/17.
 */

public class FeedTopAdsViewModel implements Visitable<FeedPlusTypeFactory> {

    public static final int TOP_ADS_POSITION_TYPE = -56;
    private final List<Visitable> list;
    private int position;

    public FeedTopAdsViewModel(List<Visitable> list) {
        this.list = list;
        setPosition(TOP_ADS_POSITION_TYPE);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Visitable> getList() {
        return list;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
