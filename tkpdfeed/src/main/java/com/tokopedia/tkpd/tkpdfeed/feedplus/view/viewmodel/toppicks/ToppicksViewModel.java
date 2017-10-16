package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 8/11/17.
 */

public class ToppicksViewModel implements Visitable<FeedPlusTypeFactory> {

    private ArrayList<ToppicksItemViewModel> list;
    private int rowNumber;
    private int page;

    public ToppicksViewModel(ArrayList<ToppicksItemViewModel> list, int page) {
        this.list = list;
        this.page = page;
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

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
