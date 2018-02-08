package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 10/30/17.
 */

public class KolRecommendationViewModel implements Visitable<FeedPlusTypeFactory> {

    String url;
    String title;
    ArrayList<KolRecommendItemViewModel> listRecommend;
    private int page;
    private int rowNumber;
    private boolean swapAdapter;

    public KolRecommendationViewModel(String url,
                                      String title,
                                      ArrayList<KolRecommendItemViewModel> listRecommend) {
        this.url = url;
        this.title = title;
        this.listRecommend = listRecommend;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<KolRecommendItemViewModel> getListRecommend() {
        return listRecommend;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setListRecommend(ArrayList<KolRecommendItemViewModel> listRecommend) {
        this.listRecommend = listRecommend;
    }

    public void setSwapAdapter(boolean swapAdapter) {
        this.swapAdapter = swapAdapter;
    }

    public boolean isSwapAdapter() {
        return swapAdapter;
    }

}
