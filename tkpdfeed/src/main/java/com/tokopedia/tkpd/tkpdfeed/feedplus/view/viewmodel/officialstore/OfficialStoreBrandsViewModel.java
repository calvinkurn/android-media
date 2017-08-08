package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 7/26/17.
 */

public class OfficialStoreBrandsViewModel implements Visitable<FeedPlusTypeFactory> {

    private ArrayList<OfficialStoreViewModel> listStore;

    public ArrayList<OfficialStoreViewModel> getListStore() {
        return listStore;
    }

    @Override
    public int type(FeedPlusTypeFactory feedPlusTypeFactory) {
        return feedPlusTypeFactory.type(this);
    }

    public OfficialStoreBrandsViewModel(ArrayList<OfficialStoreViewModel> listStore) {
        this.listStore = listStore;
    }
}
