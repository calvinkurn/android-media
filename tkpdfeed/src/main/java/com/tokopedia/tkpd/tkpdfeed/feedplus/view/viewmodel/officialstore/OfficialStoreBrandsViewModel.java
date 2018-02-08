package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 7/26/17.
 */

public class OfficialStoreBrandsViewModel implements Visitable<FeedPlusTypeFactory> {

    private final int page;
    private ArrayList<OfficialStoreViewModel> listStore;
    private int rowNumber;

    public ArrayList<OfficialStoreViewModel> getListStore() {
        return listStore;
    }

    @Override
    public int type(FeedPlusTypeFactory feedPlusTypeFactory) {
        return feedPlusTypeFactory.type(this);
    }

    public OfficialStoreBrandsViewModel(ArrayList<OfficialStoreViewModel> listStore, int page) {
        this.listStore = listStore;
        this.page = page;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getPage() {
        return page;
    }
}
