package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

import java.util.List;

/**
 * @author ricoharisin .
 */

public class FeedResult {

    private List<DataFeedDomain> dataFeedDomainList;
    private int dataSource;
    public static final int SOURCE_LOCAL = 1;
    public static final int SOURCE_CLOUD = 2;
    private boolean hasNext;

    public FeedResult(List<DataFeedDomain> dataFeedDomainList, int dataSource, boolean hasNext) {
        this.dataFeedDomainList = dataFeedDomainList;
        this.dataSource = dataSource;
        this.hasNext = hasNext;
    }

    public List<DataFeedDomain> getDataFeedDomainList() {
        return dataFeedDomainList;
    }

    public void setDataFeedDomainList(List<DataFeedDomain> dataFeedDomainList) {
        this.dataFeedDomainList = dataFeedDomainList;
    }

    public int getDataSource() {
        return dataSource;
    }

    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
